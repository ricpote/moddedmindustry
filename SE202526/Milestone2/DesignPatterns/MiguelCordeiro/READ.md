## Design Patterns

# Change log
- 6/11/2025 Miguel Cordeiro

# Factory Method

Path: core/src/mindustry/game/Waves.java
Package: mindustry.game;
Classe: Waves.java
Methods: get(), generate()

The class waves is responsible for creating the waves of enemies, hiding the creation of the individual waves from the other classes.

Code Snippet:
    
    public Seq<SpawnGroup> get(){
        if(spawns == null && dagger != null){
            spawns = Seq.with(
            new SpawnGroup(dagger){{
                end = 10;
                unitScaling = 2f;
                max = 30;
            }},

            new SpawnGroup(crawler){{
                begin = 4;
                end = 13;
                unitAmount = 2;
                unitScaling = 1.5f;
            }},

            new SpawnGroup(flare){{
                begin = 12;
                end = 16;
                unitScaling = 1f;
            }},

            new SpawnGroup(dagger){{
                begin = 11;
                unitScaling = 1.7f;
                spacing = 2;
                max = 4;
                shieldScaling = 25f;
            }},

            new SpawnGroup(pulsar){{
                begin = 13;
                spacing = 3;
                unitScaling = 0.5f;
                max = 25;
            }},

            new SpawnGroup(mace){{
                begin = 7;
                spacing = 3;
                unitScaling = 2;

                end = 30;
            }},

            new SpawnGroup(dagger){{
                begin = 12;
                unitScaling = 1;
                unitAmount = 4;
                spacing = 2;
                shieldScaling = 20f;
                max = 14;
            }},

            new SpawnGroup(mace){{
                begin = 28;
                spacing = 3;
                unitScaling = 1;
                end = 40;
                shieldScaling = 20f;
            }},

            new SpawnGroup(spiroct){{
                begin = 45;
                spacing = 3;
                unitScaling = 1;
                max = 10;
                shieldScaling = 30f;
                shields = 100;
                effect = StatusEffects.overdrive;
            }},

            new SpawnGroup(pulsar){{
                begin = 120;
                spacing = 2;
                unitScaling = 3;
                unitAmount = 5;
                effect = StatusEffects.overdrive;
            }},

            new SpawnGroup(flare){{
                begin = 16;
                unitScaling = 1;
                spacing = 2;
                shieldScaling = 20f;
                max = 20;
            }},

            new SpawnGroup(quasar){{
                begin = 82;
                spacing = 3;
                unitAmount = 4;
                unitScaling = 3;
                shieldScaling = 30f;
                effect = StatusEffects.overdrive;
            }},

            new SpawnGroup(pulsar){{
                begin = 41;
                spacing = 5;
                unitAmount = 1;
                unitScaling = 3;
                shields = 640f;
                max = 25;
            }},

            new SpawnGroup(fortress){{
                begin = 40;
                spacing = 5;
                unitAmount = 2;
                unitScaling = 2;
                max = 20;
                shieldScaling = 30;
            }},

            new SpawnGroup(nova){{
                begin = 35;
                spacing = 3;
                unitAmount = 4;
                effect = StatusEffects.overdrive;
                items = new ItemStack(Items.blastCompound, 60);
                end = 60;
            }},

            new SpawnGroup(dagger){{
                begin = 42;
                spacing = 3;
                unitAmount = 4;
                effect = StatusEffects.overdrive;
                items = new ItemStack(Items.pyratite, 100);
                end = 130;
                max = 30;
            }},

            new SpawnGroup(horizon){{
                begin = 40;
                unitAmount = 2;
                spacing = 2;
                unitScaling = 2;
                shieldScaling = 20;
            }},

            new SpawnGroup(flare){{
                begin = 50;
                unitAmount = 4;
                unitScaling = 3;
                spacing = 5;
                shields = 100f;
                shieldScaling = 10f;
                effect = StatusEffects.overdrive;
                max = 20;
            }},

            new SpawnGroup(zenith){{
                begin = 50;
                unitAmount = 2;
                unitScaling = 3;
                spacing = 5;
                max = 16;
                shieldScaling = 30;
            }},

            new SpawnGroup(nova){{
                begin = 53;
                unitAmount = 2;
                unitScaling = 3;
                spacing = 4;
                shieldScaling = 30;
            }},

            new SpawnGroup(atrax){{
                begin = 31;
                unitAmount = 4;
                unitScaling = 1;
                spacing = 3;
                shieldScaling = 10f;
            }},

            new SpawnGroup(scepter){{
                begin = 41;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 30;
                shieldScaling = 30f;
            }},

            new SpawnGroup(reign){{
                begin = 81;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 40;
                shieldScaling = 30f;
            }},

            new SpawnGroup(antumbra){{
                begin = 120;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 40;
                shieldScaling = 30f;
            }},

            new SpawnGroup(vela){{
                begin = 100;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 30;
                shieldScaling = 30f;
            }},

            new SpawnGroup(corvus){{
                begin = 145;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 35;
                shieldScaling = 30f;
                shields = 100;
            }},

            new SpawnGroup(horizon){{
                begin = 90;
                unitAmount = 2;
                unitScaling = 3;
                spacing = 4;
                shields = 40f;
                shieldScaling = 30f;
            }},

            new SpawnGroup(toxopid){{
                begin = 210;
                unitAmount = 1;
                unitScaling = 1;
                spacing = 35;
                shields = 1000;
                shieldScaling = 35f;
            }}
            );
        }
        return spawns == null ? new Seq<>() : spawns;
    }


Class Diagram:

<img width="594" height="696" alt="img" src="https://github.com/user-attachments/assets/7fe35bbb-9ca2-4049-9390-e70df9a15521" />


# Facade

Path: core/src/mindustry/core/Renderer.java
Package: mindustry.core;
Classes: Renderer.java
Methods: draw()

The class Renderer is responsible for rendering all elements(blocks, lights, minimap, fog, etc.) offering a simple interface to manage the entire rendering process without revealing the complexity of the sub-classes. 

Code Snippet:

    public final BlockRenderer blocks = new BlockRenderer();
    public final FogRenderer fog = new FogRenderer();
    public final MinimapRenderer minimap = new MinimapRenderer();
    public final OverlayRenderer overlays = new OverlayRenderer();
    public final LightRenderer lights = new LightRenderer();
    public final Pixelator pixelator = new Pixelator();
    public void draw(){
        Events.fire(Trigger.preDraw);
        MapPreviewLoader.checkPreviews();

        camera.update();

        if(Float.isNaN(camera.position.x) || Float.isNaN(camera.position.y)){
            camera.position.set(player);
        }

        graphics.clear(clearColor);
        Draw.reset();

        if(animateWater || animateShields){
            effectBuffer.resize(graphics.getWidth(), graphics.getHeight());
        }

        Draw.proj(camera);

        blocks.checkChanges();
        blocks.floor.checkChanges();
        blocks.processBlocks();

        Draw.sort(true);

        Events.fire(Trigger.draw);
        MapPreviewLoader.checkPreviews();

        if(renderer.pixelate){
            pixelator.register();
        }

        Draw.draw(Layer.background, this::drawBackground);
        Draw.draw(Layer.floor, blocks.floor::drawFloor);
        Draw.draw(Layer.block - 1, blocks::drawShadows);
        Draw.draw(Layer.block - 0.09f, () -> {
            blocks.floor.beginDraw();
            blocks.floor.drawLayer(CacheLayer.walls);
        });

        Draw.drawRange(Layer.blockBuilding, () -> Draw.shader(Shaders.blockbuild, true), Draw::shader);

        //render all matching environments
        for(var renderer : envRenderers){
            if((renderer.env & state.rules.env) == renderer.env){
                renderer.renderer.run();
            }
        }

        if(state.rules.lighting && drawLight){
            Draw.draw(Layer.light, lights::draw);
        }

        if(enableDarkness){
            Draw.draw(Layer.darkness, blocks::drawDarkness);
        }

        if(bloom != null){
            bloom.resize(graphics.getWidth(), graphics.getHeight());
            bloom.setBloomIntensity(settings.getInt("bloomintensity", 6) / 4f + 1f);
            bloom.blurPasses = settings.getInt("bloomblur", 1);
            Draw.draw(Layer.bullet - 0.02f, bloom::capture);
            Draw.draw(Layer.effect + 0.02f, bloom::render);
        }

        control.input.drawCommanded();

        Draw.draw(Layer.plans, overlays::drawBottom);

        if(animateShields && Shaders.shield != null){
            //TODO would be nice if there were a way to detect if any shields or build beams actually *exist* before beginning/ending buffers, otherwise you're just blitting and swapping shaders for nothing
            Draw.drawRange(Layer.shields, 1f, () -> effectBuffer.begin(Color.clear), () -> {
                effectBuffer.end();
                effectBuffer.blit(Shaders.shield);
            });

            Draw.drawRange(Layer.buildBeam, 1f, () -> effectBuffer.begin(Color.clear), () -> {
                effectBuffer.end();
                effectBuffer.blit(Shaders.buildBeam);
            });
        }

        float scaleFactor = 4f / renderer.getDisplayScale();

        //draw objective markers
        state.rules.objectives.eachRunning(obj -> {
            for(var marker : obj.markers){
                if(marker.world){
                    marker.draw(marker.autoscale ? scaleFactor : 1);
                }
            }
        });

        for(var marker : state.markers){
            if(marker.world){
                marker.draw(marker.autoscale ? scaleFactor : 1);
            }
        }

        Draw.reset();

        Draw.draw(Layer.overlayUI, overlays::drawTop);
        if(state.rules.fog) Draw.draw(Layer.fogOfWar, fog::drawFog);
        Draw.draw(Layer.space, () -> {
            if(launchAnimator == null || landTime <= 0f) return;
            launchAnimator.drawLaunch();
        });
        if(launchAnimator != null){
            Draw.z(Layer.space);
            launchAnimator.drawLaunchGlobalZ();
            Draw.reset();
        }

        Events.fire(Trigger.drawOver);
        blocks.drawBlocks();

        Groups.draw.draw(Drawc::draw);

        if(drawDebugHitboxes){
            DebugCollisionRenderer.draw();
        }

        Draw.reset();
        Draw.flush();
        Draw.sort(false);

        Events.fire(Trigger.postDraw);
    }

Class Diagram:

<img width="1175" height="583" alt="image" src="https://github.com/user-attachments/assets/de313556-efa0-40eb-9abc-1c243c2c4e6b" />


# Strategy

Path: core/src/mindustry/game/Rules.java
Package: mindustry.game;
Classes: Rules.java
Methods: unitCost(), unitDamage(),unitHealth(), blockHealth(), buildSpeed(), isBanned()

The class Rules is responsible for defining all game behaviors(pvp, waves, difficulty, game mode, etc.) encapsulating all game logic, including the sub-class Team Rules that has team-specific algorithms.

Code Snippet:

    public float unitBuildSpeed(Team team){
        return unitBuildSpeedMultiplier * teams.get(team).unitBuildSpeedMultiplier;
    }

    public float unitCost(Team team){
        return unitCostMultiplier * teams.get(team).unitCostMultiplier;
    }

    public float unitDamage(Team team){
        return unitDamageMultiplier * teams.get(team).unitDamageMultiplier;
    }

    

    public float unitHealth(Team team){
        //a 0 here would be a very bad idea.
        return Math.max(unitHealthMultiplier * teams.get(team).unitHealthMultiplier, 0.000001f);
    }

    public float unitCrashDamage(Team team){
        return unitDamage(team) * unitCrashDamageMultiplier * teams.get(team).unitCrashDamageMultiplier;
    }

    public float unitMineSpeed(Team team){
        return unitMineSpeedMultiplier * teams.get(team).unitMineSpeedMultiplier;
    }

    public float blockHealth(Team team){
        return blockHealthMultiplier * teams.get(team).blockHealthMultiplier;
    }
    public float blockDamage(Team team){
        return blockDamageMultiplier * teams.get(team).blockDamageMultiplier;
    }

    public float buildSpeed(Team team){
        return buildSpeedMultiplier * teams.get(team).buildSpeedMultiplier;
    }

    public boolean isBanned(Block block){
        return blockWhitelist != bannedBlocks.contains(block);
    }

    public boolean isBanned(UnitType unit){
        return unitWhitelist != bannedUnits.contains(unit);
    }

Class Diagram:

<img width="573" height="689" alt="Captura de ecrã 2025-11-06 171158" src="https://github.com/user-attachments/assets/7da7b05c-fd9e-4995-9102-a4bafc1c39e9" />


