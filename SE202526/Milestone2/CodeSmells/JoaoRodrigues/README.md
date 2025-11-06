## Code Smells

# Change Log
   - 6/11/2025 Joao Rodrigues

# Large Class

   Location: core/src/mindustry/Vars.java
   
   The Vars class holds a large number of global variables that represent nearly every subsystem of the game(world, 
   logic, UI, player, input, ...), and holds methods like init(), loadSettings(), loadLogger() that even though they 
   are logically valid, they are grouped inappropriately in a single class that should be responsible for variables.
   This makes Vars a large class that holds a lot of responsibility, being responsible for initialization, logging,
   and storing global variables all at once. Such design breaks the single responsibility principle and creates tight
   coupling across the entire code.

   Solution: Separate Vars into different domains, like LoggerManager - handles console and file logging. ConfigManager
    - manage settings and directories. Constants - stores only immutable values. This solution should improve cohesion,
   testability and maintenance.

   //Code snippet

    public class Vars implements Loadable{
    /** Whether the game failed to launch last time. */
    public static boolean failedToLaunch = false;
    /** Whether to load locales.*/
    public static boolean loadLocales = true;
    /** Whether the logger is loaded. */
    public static boolean loadedLogger = false, loadedFileLogger = false;
    /** Name of current Steam player. */
    public static String steamPlayerName = "";
    /** Min game version for all mods. */
    public static final int minModGameVersion = 136;
    ...
    public static Net net;
    public static ContentLoader content;
    public static GameState state;
    public static EntityCollisions collisions;
    public static Waves waves;
    public static Platform platform = new Platform(){};
    public static Mods mods;
    public static Schematics schematics;
    public static BeControl becontrol;
    public static AsyncCore asyncCore;
    public static BaseRegistry bases;
    public static GlobalVars logicVars;
    public static MapEditor editor;
    public static AvoidanceProcess avoidance;
    public static GameService service = new GameService();

    public static Universe universe;
    public static World world;
    public static Maps maps;
    public static WaveSpawner spawner;
    public static BlockIndexer indexer;
    public static Pathfinder pathfinder;
    public static ControlPathfinder controlPath;
    public static FogControl fogControl;

    public static Control control;
    public static Logic logic;
    public static Renderer renderer;
    public static UI ui;
    public static NetServer netServer;
    public static NetClient netClient;
    ...
    public static void init(){
        Groups.init();
       ...
       ...
       ...
        mods.load();
        maps.load();
    }
    public static void loadLogger(){
        ...
        ...
        loadedLogger = true;
    }
    public static void loadSettings(){
        settings.setJson(JsonIO.json);
        settings.setAppName(appName);
        ...
        ...
    }
    ...
    }
   

# Duplicate Code

   Location: core/src/mindustry/world/blocks/distribution/Duct.java, 
             core/src/mindustry/world/blocks/distribution/DuctJunction.java
   
   This is a duplicated code smell because both methods contain almost identical logic, differing in only one line of 
   code. This is problematic because it violates the principle dont repeat yourself and increases maintenance cost.
   
   Solution: The duplicated behaviour should be abstracted into a shared method or superclass, allowing the subclasses 
   to modify only the part that differs between them.
   
   //Code snippet

    public class DuctJunction{
    ...
    public boolean acceptItem(Building source, Item item){
            int relative = source.relativeTo(tile);

            if(relative == -1 || itemdata[relative] != null) return false;
            Building to = nearby(relative);
            return to != null && to.team == team;
        }
    ...
    }
    
    public class Junction{
    ...
    public boolean acceptItem(Building source, Item item){
            int relative = source.relativeTo(tile);

            if(relative == -1 || !buffer.accepts(relative)) return false;
            Building to = nearby(relative);
            return to != null && to.team == team;
        }
    ...
    }

# Long Method

   Location: core/src/mindustry/world/blocks/production/BeamDrill.java
   
   The method performs too many distinct actions at once. Initializes lasers if needed, updates facing direction,
   calculates drill progress and boost, handles item production and resource addition, manage dumping. Each of these
   is an independent process but all are mixed together in one block of code. That makes it hard to read, test and 
   modify safely.
   
   Solution: clean solution is to split the logic into smaller private methods, each handling one concept.
   
   //Code snippet

    public void updateTile(){
            super.updateTile();

            if(lasers[0] == null) updateLasers();

            warmup = Mathf.approachDelta(warmup, Mathf.num(efficiency > 0), 1f / 60f);

            updateFacing();

            float multiplier = Mathf.lerp(1f, optionalBoostIntensity, optionalEfficiency);
            float drillTime = getDrillTime(lastItem);
            boostWarmup = Mathf.lerpDelta(boostWarmup, optionalEfficiency, 0.1f);
            lastDrillSpeed = (facingAmount * multiplier * timeScale) / drillTime;

            time += edelta() * multiplier;

            if(time >= drillTime){
                for(Tile tile : facing){
                    Item drop = tile == null ? null : tile.wallDrop();
                    if(items.total() < itemCapacity && drop != null){
                        items.add(drop, 1);
                        produced(drop);
                    }
                }
                time %= drillTime;
            }

            if(timer(timerDump, dumpTime / timeScale)){
                dump();
            }
        }