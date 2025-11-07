## Design Patterns

# Change log
- 7/11/2025 André Narquel

# Prototype
Path: core/src/mindustry/world/blocks/payloads/BuildPayload.java
Path: core/src/mindustry/world/Block.java
Path: core/src/mindustry/world/blocks/power/Battery.java

The program uses Prototype Pattern to create new instances of blocks based on existing models. The BuildPayload class is
an example of a client using this pattern.

When BuildPayload needs to create a new battery on the map, it does not instantiate the Battery from scratch.
Instead, it calls battery.newBuilding().create(battery, team), which returns a new instance of the battery based on 
the original model (Battery). The original block functions as a Prototype, providing a template for generating new 
instances that can be modified or placed on the map without affecting the base battery.

Benefits:

Avoids code duplication: there is no need to recreate all properties and logic for each new battery.
Flexible for variants: new batteries can be created with minor modifications from the original model.
Easier maintenance: changes to the base battery can be reflected in all clones created from it, ensuring consistency 
throughout the game.

The battery example is one of dozens of possible blocks created by this system, it is just 1 of many types of blocks.

![img_1.png](img_1.png)


//Code snippet


    ---------// BuildPayload Class //-------------
    (...)
    public BuildPayload(Block block, Team team){
    this.build = block.newBuilding().create(block, team);
    this.build.tile = emptyTile;
    }

    ---------// Battery Class //------------------
    public class Battery extends PowerDistributor{
    public @Nullable DrawBlock drawer;

    public Color emptyLightColor = Color.valueOf("f8c266");
    public Color fullLightColor = Color.valueOf("fb9567");

    public Battery(String name){
        super(name);
        outputsPower = true;
        consumesPower = true;
        canOverdrive = false;
        flags = EnumSet.of(BlockFlag.battery);
        //TODO could be supported everywhere...
        envEnabled |= Env.space;
        destructible = true;
        //batteries don't need to update
        update = false;
    }
    (...)

    ---------// Block Class //-------------
    (...)    
    public final Building newBuilding(){
        return buildType.get();
    }
    (...)

    
# Memento
Path: core/src/mindustry/Saves.java
Path: core/src/mindustry/io/SaveIO.java
Path: core/src/mindustry/Saves.java (inner class SaveSlot)

The program uses the Memento Pattern to save and restore the state of the game. The Saves class is an example of a
client using this pattern, while SaveMeta acts as the Memento storing the internal state of the game.
When the game needs to save the current state, it does not expose the internal details of the world, units, or blocks.
Instead, it calls SaveSlot.save(), which internally serializes the current state into a SaveMeta object (the Memento).
Later, this Memento can be used to restore the game to the saved state by calling SaveSlot.load().
The Saves class acts as the Caretaker, managing all save slots, deciding when to save or load a Memento. This ensures
that the game state can be restored without exposing internal structures of the world, units, or other objects.

Benefits:

Encapsulation: game state is saved without exposing internal representation of objects.
Restore capability: allows restoring the game to a previous state reliably.
Consistency: saves and restores include all necessary data (time played, map state, units, rules).

![img_2.png](img_2.png)

//Code snippet
    
    public class SaveIO{
    /** Save format header. */
    public static final byte[] header = {'M', 'S', 'A', 'V'};
    public static final IntMap<SaveVersion> versions = new IntMap<>();
    public static final Seq<SaveVersion> versionArray = Seq.with(new Save1(), new Save2(), new Save3(), new Save4(), new Save5(), new Save6(), new Save7(), new Save8(), new Save9(), new Save10());

    static{
        for(SaveVersion version : versionArray){
            versions.put(version.version, version);
        }
    }

    public static SaveVersion getSaveWriter(){
        return versionArray.peek();
    }

    public static @Nullable SaveVersion getSaveWriter(int version){
        return versions.get(version);
    }

    public static void save(Fi file){
        boolean exists = file.exists();
        if(exists) file.moveTo(backupFileFor(file));
        try{
            write(file);
        }catch(Throwable e){
            if(exists) backupFileFor(file).moveTo(file);
            throw new RuntimeException(e);
        }
    }
    (...)


# 

//Code snippet
