## Design Patterns

# Change log
- 6/11/2025 Miguel Cordeiro

# Factory Method

Path: core/src/mindustry/game/Waves.java
Package: mindustry.game;
Classes: SerpuloPlanetGenerator.java, AsteroidGenerator.java, WaveInfoDialog.java, Vars.java
Methods: init(), WaveInfoDialog(), postGenerate(Tiles tiles), generate()

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



