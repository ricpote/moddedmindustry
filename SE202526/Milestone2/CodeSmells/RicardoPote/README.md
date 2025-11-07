Large Class
Location:build/src/mindustry/input/InputHandler.java 
The InputHandler class is a clear example of a large class code smell that is proven by observing the values of WMC metric being 746 which just means this class does way to much at the same time this classes is responsibale for capturing drag and drops, build placements , camera movements ,etc these are to manyu responsabilities for only one class which causes the size of the class to ballon to the 2286 lines of codes it has.
Solution:The solution for this code smell is to refactor this class into many diffrent classes dividing by their responsabilities and moving this classes method´s into those new classes maybe something like one that captures input and then others that manage different events based on input. 



public abstract class InputHandler implements InputProcessor, GestureListener{
    static ObjectMap<Vec2, Seq<Unit>> queuedCommands = new ObjectMap<>();

    /** Used for dropping items. */
    final static float playerSelectRange = mobile ? 17f : 11f;
    final static float unitSelectRadScl = 1f;
    final static Seq<UnitStance> stancesOut = new Seq<>();
    final static IntSeq removed = new IntSeq();
    final static IntSet intSet = new IntSet();
    /** Maximum line length. */
    final static int maxLength = 100;
    final static Rect r1 = new Rect(), r2 = new Rect();
    final static Seq<Unit> tmpUnits = new Seq<>(false);
    final static Seq<Building> tmpBuildings = new Seq<>(false);
    final static KeyBind[] controlGroupBindings = {
    Binding.blockSelect01,
    Binding.blockSelect02,
    Binding.blockSelect03,
    Binding.blockSelect04,
    Binding.blockSelect05,
    Binding.blockSelect06,
    Binding.blockSelect07,
    Binding.blockSelect08,
    Binding.blockSelect09,
    Binding.blockSelect10
    };

    /** If true, there is a cutscene currently occurring in logic. */
    public boolean logicCutscene;
    public Vec2 logicCamPan = new Vec2();
    public float logicCamSpeed = 0.1f;
    public float logicCutsceneZoom = -1f;

    /** If any of these functions return true, input is locked. */
    public Seq<Boolp> inputLocks = Seq.with(() -> renderer.isCutscene(), () -> logicCutscene);
    public Interval controlInterval = new Interval();
    public @Nullable Block block;
    public boolean overrideLineRotation;
    public int rotation = 1;
    public boolean droppingItem;
    public float itemDepositCooldown;
    public Group uiGroup;
    public boolean isBuilding = true, buildWasAutoPaused = false, wasShooting = false;
    public @Nullable UnitType controlledType;
    public float recentRespawnTimer;

 









Feature Envy 
Location:core/src/core/logic.java
The logic class is a clear example of a feature envy code smell that is proven by a high value of ATFD metric along whit the presence of many of calls for internals of other classes examples of this are the interactions whit gamestate object where its atributes are repeatdly acces and altered
Solution:The solution for this code smell is simply to delegate the calue atribution done by this class upon gamestate class to gamestate class adding a method for these atributions for these classes.


public class Logic implements ApplicationListener{(examples of abuse these first part of method)

   

        //when loading a 'damaged' sector, propagate the damage
        Events.on(SaveLoadEvent.class, e -> {
            if(state.isCampaign()){
                state.rules.coreIncinerates = true;
                state.rules.canGameOver = true;
                state.rules.allowEditRules = false;

                //fresh map has no sector info
                if(!e.isMap){
                    SectorInfo info = state.rules.sector.info;
                    info.write();

                    //only simulate waves if the planet allows it
                    if(state.rules.sector.planet.allowWaveSimulation){
                        //how much wave time has passed
                        int wavesPassed = info.wavesPassed;

                        //wave has passed, remove all enemies, they are assumed to be dead
                        if(wavesPassed > 0){
                            Groups.unit.each(u -> {
                                if(u.team == state.rules.waveTeam){
                                    u.remove();
                                }
                            });
                        }

                        //simulate passing of waves
                        if(wavesPassed > 0){
                            //simulate wave counter moving forward
                            state.wave += wavesPassed;
                            state.wavetime = state.rules.waveSpacing * state.getPlanet().campaignRules.difficulty.waveTimeMultiplier;

                            SectorDamage.applyCalculatedDamage();
                        }
                    }

                    state.getSector().planet.applyRules(state.rules);

                    //reset values
                    info.damage = 0f;
                    info.wavesPassed = 0;
                    info.hasCore = true;
                    info.secondsPassed = 0;

                    state.rules.sector.saveInfo();
                }
            }
        });

        Events.on(PlayEvent.class, e -> {
            //reset weather on play
            var randomWeather = state.rules.weather.copy().shuffle();
            float sum = 0f;
            for(var weather : randomWeather){
                weather.cooldown = sum + Mathf.random(weather.maxFrequency);
                sum += weather.cooldown;
            }
            //tick resets on new save play
            state.tick = 0f;
        });

        Events.on(WorldLoadEvent.class, e -> {
            //enable infinite ammo for wave team by default
            state.rules.waveTeam.rules().infiniteAmmo = true;

            if(state.isCampaign()){
                //enable building AI on campaign unless the preset disables it

                state.rules.coreIncinerates = true;
                state.rules.infiniteResources = false;
                state.rules.allowEditRules = false;
                state.rules.allowEditWorldProcessors = false;
                if(state.getPlanet().enemyInfiniteItems){
                    state.rules.waveTeam.rules().infiniteResources = true;
                    state.rules.waveTeam.rules().fillItems = true;
                }
                state.rules.waveTeam.rules().buildSpeedMultiplier *= state.getPlanet().enemyBuildSpeedMultiplier;
            }

            //save settings
            Core.settings.manualSave();
        });

        //sync research
        Events.on(UnlockEvent.class, e -> {
            if(net.server()){
                Call.researched(e.content);
            }
        });

        Events.on(SectorCaptureEvent.class, e -> {
            if(!net.client() && e.sector == state.getSector() && e.sector.isBeingPlayed()){
                state.rules.waveTeam.data().destroyToDerelict();
            }

            if(!net.client() && e.sector.planet.generator != null){
                e.sector.planet.generator.onSectorCaptured(e.sector);
            }
        });

        Events.on(SectorLoseEvent.class, e -> {
            if(!net.client() && e.sector.planet.generator != null){
                e.sector.planet.generator.onSectorLost(e.sector);
            }
        });

        Events.on(BlockDestroyEvent.class, e -> {
            if(e.tile.build instanceof CoreBuild core && core.team.isAI() && state.rules.coreDestroyClear){
                Core.app.post(() -> {
                    core.team.data().timeDestroy(core.x, core.y, state.rules.enemyCoreBuildRadius);
                });
            }
        });

        //listen to core changes; if all cores have been destroyed, set to derelict.
        Events.on(CoreChangeEvent.class, e -> Core.app.post(() -> {
            if(state.rules.cleanupDeadTeams && state.rules.pvp && !e.core.isAdded() && e.core.team != Team.derelict && e.core.team.cores().isEmpty()){
                e.core.team.data().destroyToDerelict();
            }
        }));

        Events.on(BlockBuildEndEvent.class, e -> {
            if(e.team == state.rules.defaultTeam){
                if(e.breaking){
                    state.stats.buildingsDeconstructed++;
                }else{
                    state.stats.buildingsBuilt++;
                }
            }
        });

        Events.on(BlockDestroyEvent.class, e -> {
            if(e.tile.team() == state.rules.defaultTeam){
                state.stats.buildingsDestroyed ++;
            }
        });

        Events.on(UnitDestroyEvent.class, e -> {
            if(e.unit.team() != state.rules.defaultTeam){
                state.stats.enemyUnitsDestroyed ++;
            }
        });

        Events.on(UnitCreateEvent.class, e -> {
            if(e.unit.team == state.rules.defaultTeam){
                state.stats.unitsCreated++;
            }
        });
    }

    Long Parameter 
Location:core/src/ai/ControlPathFinder class
raycastRect in ControlPathFinder class located in ControlPathFinder class line 1387 this class is a clear example of a long parameter code smell as indicated by NOPM metric is has 12 parameters 
Spatial data (startX, startY, endX, endY, x1, y1, x2, y2, rectSize)
Contextual / game state (team, type, initialCost) information
this is makes code harder to understand .
Solution:Join some parameters into records like coords record in this case or game state record.
        
          private static boolean raycastRect(int initialCost, float startX, float startY, float endX, float endY, int team, PathCost type, int x1, int y1, int x2, int y2, float rectSize){
                int ww = wwidth, wh = wheight;
                int x = x1, dx = Math.abs(x2 - x), sx = x < x2 ? 1 : -1;
                int y = y1, dy = Math.abs(y2 - y), sy = y < y2 ? 1 : -1;
                    int e2, err = dx - dy;

        while(x >= 0 && y >= 0 && x < ww && y < wh){
            if(
            !nearPassable(initialCost, team, type, x + y * wwidth) ||
            overlap(initialCost,team, type, x + 1, y, startX, startY, endX, endY, rectSize) ||
            overlap(initialCost,team, type, x - 1, y, startX, startY, endX, endY, rectSize) ||
            overlap(initialCost,team, type, x, y + 1, startX, startY, endX, endY, rectSize) ||
            overlap(initialCost,team, type, x, y - 1, startX, startY, endX, endY, rectSize)
            ) return true;

            if(x == x2 && y == y2) return false;

            //diagonal ver
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

        return true;
    }







