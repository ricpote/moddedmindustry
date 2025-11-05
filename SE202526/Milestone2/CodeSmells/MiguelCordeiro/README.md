1º Code Smell - Long method:

Este code smell localiza-se em core/src/mindustry/core/Control.java e foi escolhido como long method pois o construtor da classe é constituído por 225 linhas de código tornando-se difícil de perceber o que faz e se pretendermos mudar alguma funcionalidade, esta mudança pode afetar o resto do método. Para resolver este code smell temos de factorizar parte do código num método independente, podemos então criar métodos que fazem a partes dos events listeners simplificando este long method.Aqui está o long method:
    public Control(){
        saves = new Saves();
        sound = new SoundControl();
        indicators = new AttackIndicators();

        Events.on(BuildDamageEvent.class, e -> {
            if(e.build.team == Vars.player.team()){
                indicators.add(e.build.tileX(), e.build.tileY());
            }
        });

        //show dialog saying that mod loading was skipped.
        Events.on(ClientLoadEvent.class, e -> {
            if(Vars.mods.skipModLoading() && Vars.mods.list().any()){
                Time.runTask(4f, () -> {
                    ui.showInfo("@mods.initfailed");
                });
            }
            checkAutoUnlocks();

            if((OS.isWindows && !OS.is64Bit && !Core.settings.getBool("nowarn32bit", false))){
                BaseDialog dialog = new BaseDialog("@warn.32bit.title");
                dialog.buttons.button("@ok", dialog::hide).size(120f, 64f);
                dialog.cont.add("@warn.32bit").labelAlign(Align.center, Align.center).wrap().grow().row();
                dialog.cont.check("@dontshowagain", val -> Core.settings.put("nowarn32bit", val));

                dialog.show();
            }
        });

        Events.on(StateChangeEvent.class, event -> {
            if((event.from == State.playing && event.to == State.menu) || (event.from == State.menu && event.to != State.menu)){
                Time.runTask(5f, platform::updateRPC);
            }
        });

        Events.on(PlayEvent.class, event -> {
            player.team(netServer.assignTeam(player));
            player.add();

            state.set(State.playing);
        });

        Events.on(WorldLoadEvent.class, event -> {
            if(Mathf.zero(player.x) && Mathf.zero(player.y)){
                Building core = player.bestCore();
                if(core != null){
                    player.set(core);
                    camera.position.set(core);
                }
            }else{
                camera.position.set(player);
            }
        });

        Events.on(SaveLoadEvent.class, event -> {
            input.checkUnit();
        });

        Events.on(ResetEvent.class, event -> {
            player.reset();
            toBePlaced.clear();
            indicators.clear();

            hiscore = false;
            saves.resetSave();
        });

        Events.on(WaveEvent.class, event -> {
            if(state.map.getHightScore() < state.wave){
                hiscore = true;
                state.map.setHighScore(state.wave);
            }

            Sounds.wave.play();
        });

        Events.on(GameOverEvent.class, event -> {
            state.stats.wavesLasted = state.wave;
            Effect.shake(5, 6, Core.camera.position.x, Core.camera.position.y);
            //the restart dialog can show info for any number of scenarios
            Call.gameOver(event.winner);
        });

        //add player when world loads regardless
        Events.on(WorldLoadEvent.class, e -> {
            player.add();
            //make player admin on any load when hosting
            if(net.active() && net.server()){
                player.admin = true;
            }
        });

        //autohost for pvp maps
        Events.on(WorldLoadEvent.class, event -> app.post(() -> {
            if(state.rules.pvp && !net.active()){
                try{
                    net.host(port);
                    player.admin = true;
                }catch(IOException e){
                    ui.showException("@server.error", e);
                    state.set(State.menu);
                }
            }
        }));

        Events.on(UnlockEvent.class, e -> {
            if(e.content.showUnlock()){
                ui.hudfrag.showUnlock(e.content);
            }

            checkAutoUnlocks();

            if(e.content instanceof SectorPreset){
                for(TechNode node : TechTree.all){
                    if(!node.content.unlocked() && node.objectives.contains(o -> o instanceof SectorComplete sec && sec.preset == e.content) && !node.objectives.contains(o -> !o.complete())){
                        ui.hudfrag.showToast(new TextureRegionDrawable(node.content.uiIcon), iconLarge, bundle.get("available"));
                    }
                }
            }
        });

        Events.on(SectorCaptureEvent.class, e -> {
            app.post(this::checkAutoUnlocks);

            if(!net.client() && e.sector.preset != null && e.sector.preset.isLastSector && e.initialCapture){
                Time.run(60f * 2f, () -> {
                    ui.campaignComplete.show(e.sector.planet);
                });
            }
        });

        //delete save on campaign game over
        Events.on(GameOverEvent.class, e -> {
            if(state.isCampaign() && !net.client() && !headless){

                //save gameover sate immediately
                if(saves.getCurrent() != null){
                    saves.getCurrent().save();
                }
            }
        });

        Events.run(Trigger.newGame, () -> {
            var core = player.bestCore();
            if(core == null) return;

            camera.position.set(core);
            player.set(core);

            float coreDelay = 0f;
            if(!settings.getBool("skipcoreanimation") && !state.rules.pvp){
                coreDelay = core.launchDuration();
                //delay player respawn so animation can play.
                player.deathTimer = Player.deathDelay - core.launchDuration();
                //TODO this sounds pretty bad due to conflict
                if(settings.getInt("musicvol") > 0){
                    //TODO what to do if another core with different music is already playing?
                    Music music = core.landMusic();
                    music.stop();
                    music.play();
                    music.setVolume(settings.getInt("musicvol") / 100f);
                }

                renderer.showLanding(core);
            }

            if(state.isCampaign()){
                if(state.rules.sector.info.importRateCache != null){
                    state.rules.sector.info.refreshImportRates(state.rules.sector.planet);
                }

                //don't run when hosting, that doesn't really work.
                if(state.rules.sector.planet.prebuildBase){
                    toBePlaced.clear();
                    float unitsPerTick = 2f;
                    float buildRadius = state.rules.enemyCoreBuildRadius * 1.5f;

                    //TODO if the save is unloaded or map is hosted, these blocks do not get built.
                    boolean anyBuilds = false;
                    for(var build : state.rules.defaultTeam.data().buildings.copy()){
                        if(!(build instanceof CoreBuild) && !build.block.privileged){
                            var ccore = build.closestCore();

                            if(ccore != null){
                                anyBuilds = true;

                                if(!net.active()){
                                    build.pickedUp();
                                    build.tile.remove();

                                    toBePlaced.add(build);

                                    Time.run(build.dst(ccore) / unitsPerTick + coreDelay, () -> {
                                        if(build.tile.build != build){
                                            placeLandBuild(build);

                                            toBePlaced.remove(build);
                                        }
                                    });
                                }else{
                                    //when already hosting, instantly build everything. this looks bad but it's better than a desync
                                    Fx.coreBuildBlock.at(build.x, build.y, 0f, build.block);
                                    build.block.placeEffect.at(build.x, build.y, build.block.size);
                                }
                            }
                        }
                    }

                    if(anyBuilds){
                        for(var ccore : state.rules.defaultTeam.data().cores){
                            Time.run(coreDelay, () -> {
                                Fx.coreBuildShockwave.at(ccore.x, ccore.y, buildRadius);
                            });
                        }
                    }
                }
            }
        });

        Events.on(SaveWriteEvent.class, e -> forcePlaceAll());
        Events.on(HostEvent.class, e -> forcePlaceAll());
        Events.on(HostEvent.class, e -> {
            state.set(State.playing);
        });
    }
