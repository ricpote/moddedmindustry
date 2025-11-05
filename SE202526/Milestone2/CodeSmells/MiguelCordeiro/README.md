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

2º Code Smell - Duplicated Code:

Este code smell localiza-se em core/src/mindustry/core/World.java e foi escolhido como duplicated code porque existe duas funções que são quase idênticas mas só muda o que os dois primeiros if's dentro do ciclo fazem. Isto implica que se quisermos fazer uma mudança ou adicionar uma funcionalidade nesta classe temos de atualizar o código em vários lugares, o que não é nada prático. Para resolvermos este code smell podemos deixar o segundo método que retorna um boolean e no primeiro método chamamos o segundo método e ignoramos o boolean, pois terá o mesmo resultado uma vez que vai sair do ciclo quando retornaro boolean. Aqui esta o código:

public static void raycastEach(int x1, int y1, int x2, int y2, Raycaster cons){
        int x = x1, dx = Math.abs(x2 - x), sx = x < x2 ? 1 : -1;
        int y = y1, dy = Math.abs(y2 - y), sy = y < y2 ? 1 : -1;
        int e2, err = dx - dy;

        while(true){
            if(cons.accept(x, y)) break;
            if(x == x2 && y == y2) break;

            e2 = 2 * err;
            if(e2 > -dy){
                err -= dy;
                x += sx;
            }

            if(e2 < dx){
                err += dx;
                y += sy;
            }
        }
    }

    public static boolean raycast(int x1, int y1, int x2, int y2, Raycaster cons){
        int x = x1, dx = Math.abs(x2 - x), sx = x < x2 ? 1 : -1;
        int y = y1, dy = Math.abs(y2 - y), sy = y < y2 ? 1 : -1;
        int e2, err = dx - dy;

        while(true){
            if(cons.accept(x, y)) return true;
            if(x == x2 && y == y2) return false;

            e2 = 2 * err;
            if(e2 > -dy){
                err = err - dy;
                x = x + sx;
            }

            if(e2 < dx){
                err = err + dx;
                y = y + sy;
            }
        }
    }


3º Code Smell - Data Class:

Este code smell localiza-se em core/src/mindustry/game/EventType.java e foi escolhido como Data Class porque dentro da classe em si há outras classes que simplesmente guardam dados mas não executam nenhum método. Isto pode ser um sinal que essas mini-classes podem nem ser necessárias sendo que não têm nenhum método. Para resolver este code smell podemos transformá-las em record classes e adicionar métodos relacionados com a classe de forma a ter uma classe útil que simplesmente faz mais que guardar as variáveis. Aqui estão alguns exemplos dessas classes:


public static class SaveLoadEvent{
        public final boolean isMap;

        public SaveLoadEvent(boolean isMap){
            this.isMap = isMap;
        }
    }

    /** Called when a sector is destroyed by waves when you're not there. */
    public static class SectorLoseEvent{
        public final Sector sector;

        public SectorLoseEvent(Sector sector){
            this.sector = sector;
        }
    }

    /** Called when a sector is destroyed by waves when you're not there. */
    public static class SectorInvasionEvent{
        public final Sector sector;

        public SectorInvasionEvent(Sector sector){
            this.sector = sector;
        }
    }

