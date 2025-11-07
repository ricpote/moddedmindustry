## Code Smells

# Change Log
   - 6/11/2025 Tomás Silva

# Data Class

   Location: core/src/mindustry/world/WorldParams.java
   
   This class only has the public variables "seedOffset", "saveInfo" and "corePositionOverride", with no logic methods
   breaking the encapsulation principle, because other classes can change those variables directly.Resumming this is a 
   Data Class because has no logic methods and the 3 variables are public so that other classes can change them.

   Solution: 
   The solution for this Code Smell is simply make the variables private and implement the methods "get", 
   "set" (and "is" in the case of "saveInfo") to encapsulate the variables and control the access of other classes, 
   and also some logic methods:

    public class WorldParams {
        private int seedOffset;
        private boolean saveInfo = true;
        private int corePositionOverride;

        public int getSeedOffset() { return seedOffset; }
        public void setSeedOffset(int seedOffset) { this.seedOffset = seedOffset; }

        public boolean isSaveInfo() { return saveInfo; }
        public void setSaveInfo(boolean saveInfo) { this.saveInfo = saveInfo; }

        public int getCorePositionOverride() { return corePositionOverride; }
        public void setCorePositionOverride(int corePositionOverride) { this.corePositionOverride = corePositionOverride; }
         
        //after review (exemple)
        /**
        * Determina se o 'core' do mundo deve ser gerado
        * com uma posição específica (override) ou com base em saveInfo.
        * @return true se corePositionOverride tiver um valor válido (diferente de 0),
        * ou se o mundo estiver configurado para salvar informações (saveInfo).
        */
        public boolean shouldGenerateCore() {
            // Exemplo de lógica que combina múltiplos campos
            // Assumindo que 0 é o valor padrão que significa "não sobrescrever".
            return this.corePositionOverride != 0 || this.saveInfo;
        }
    }


   //Code snippet

    /** Parameters for loading or generating a world. */
    public class WorldParams{

    /** For sectors: World generator seed offset. */
    public int seedOffset;

    /** For sectors: Whether to save the info once the map is generated. A value of 'false' is used for editor generation. */
    public boolean saveInfo = true;

    /** Position in packed x/y format - not array format. Overrides the core position when generating with a FileMapGenerator. 0 to disable. */
    public int corePositionOverride;
    }
   

# Speculative Generality

   Location: core/src/mindustry/world/DirectionalItemBuffer.java
   
   In this class the method "poll" and the structure's "BufferItemStruct" and "BufferItemLegacyStruct", are never used, 
   which implies Speculative Generality (when creating code that's not necessary for now, but maybe will, "over-engineer")
   
   Solution: The Speculative Generality can be solved by simply delete the code that's never used.
   
   //Code snippet

    public class DirectionalItemBuffer{
    public final long[][] buffers;
    public final int[] indexes;

    public DirectionalItemBuffer(int capacity){
        this.buffers = new long[4][capacity];
        this.indexes = new int[5];
    }
    ...

    public Item poll(int buffer, float speed){
        if(indexes[buffer] > 0){
            long l = buffers[buffer][0];
            float time = BufferItem.time(l);

            if(Time.time >= time + speed || Time.time < time){
                return content.item(BufferItem.item(l));
            }
        }
        return null;
    }
    ...

    @Struct
    class BufferItemStruct{
        short item;
        float time;
    }

    @Struct
    class BufferItemLegacyStruct{
        byte item;
        float time;
    }

# Data Clumps

   Location: core\src\mindustry\world\Block.java
   
   The class has to many methods that receive the parameters "Team team, float x, float y, float range", representing a 
   crucial, yet unformalized, concept repeatedly scattered throughout the codebase, leading to poor cohesion and hiding 
   the true data structure that should be an object.
   
   Solution: To solve this Code smell, we could create an objet that holds these 4 variables, as private, with logic methods
   to reach these variables through other classes and methods to modify the variables (to avoid data class).
   
   //Code snippet (lines 245-394)

    /** Returns the closest target enemy. First, units are checked, then tile entities. */
    public static Teamc closestTarget(Team team, float x, float y, float range){
        
    }

    /** Returns the closest target enemy. First, units are checked, then tile entities. */
    public static Teamc closestTarget(Team team, float x, float y, float range, Boolf<Unit> unitPred){
        
    }

    /** Returns the closest target enemy. First, units are checked, then tile entities. */
    public static Teamc closestTarget(Team team, float x, float y, float range, Boolf<Unit> unitPred, Boolf<Building> tilePred){
        
    }

    /** Returns the closest target enemy. First, units are checked, then buildings. */
    public static Teamc bestTarget(Team team, float x, float y, float range, Boolf<Unit> unitPred, Boolf<Building> tilePred, Sortf sort){
        
    }

    /** Returns the closest enemy of this team. Filter by predicate. */
    public static Unit closestEnemy(Team team, float x, float y, float range, Boolf<Unit> predicate){
        
    }

    /** Returns the closest enemy of this team using a custom comparison function. Filter by predicate. */
    public static Unit bestEnemy(Team team, float x, float y, float range, Boolf<Unit> predicate, Sortf sort){
        
    }

    /** Returns the closest ally of this team. Filter by predicate. No range. */
    public static Unit closest(Team team, float x, float y, Boolf<Unit> predicate){
        
    }

    /** Returns the closest ally of this team in a range. Filter by predicate. */
    public static Unit closest(Team team, float x, float y, float range, Boolf<Unit> predicate){
        
    }

    /** Returns the closest ally of this team in a range. Filter by predicate. */
    public static Unit closest(Team team, float x, float y, float range, Boolf<Unit> predicate, Sortf sort){
        
    }

    /** Returns the closest ally of this team. Filter by predicate.
     * Unlike the closest() function, this only guarantees that unit hitboxes overlap the range. */
    public static Unit closestOverlap(Team team, float x, float y, float range, Boolf<Unit> predicate){
        
    }