package mindustry.maps.generators;

import arc.util.noise.Simplex;
import mindustry.ai.Astar;

import mindustry.world.Tiles;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.Astar.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.world.*;

import mindustry.world.blocks.environment.*;
import arc.util.*;



public class WeeklyGenerator extends BasicGenerator {
    private long seed;
    public static class OreData {
        public Block block;
        public float probability;

        public OreData(Block block, float probability) {
            this.block = block;
            this.probability = probability;
        }
    }


    private Seq<OreData> oresToSpawn;
    private Point2 coreSpawnPos;
    private Point2  spawnPos;
    private Seq<Point2> corePositions;
    private Seq<Point2> spawnPositions;
    private Seq<Block> floors;
    private Seq<Block> walls;
    private Seq<Block> allBlocks ;
    private Seq<Block> ores ;
    public WeeklyGenerator(){
        super();
        allBlocks = Vars.content.blocks();
        oresToSpawn = new Seq<>();
        corePositions = new Seq<>();
        spawnPositions = new Seq<>();

        floors = new Seq<>();
        walls = new Seq<>();
        ores=new Seq<>();
    }
    @Override
    public void generate(Tiles tiles, WorldParams params) {
        this.tiles = tiles;
        this.width = tiles.width;
        this.height = tiles.height;
        seed = params.mapSeed;
        this.rand.setSeed(seed);
        oresToSpawn = Seq.with(
                new OreData(Blocks.oreCopper, 0.85f),
                new OreData(Blocks.oreLead, 0.75f),
                new OreData(Blocks.oreScrap, 0.5f),
                new OreData(Blocks.oreCoal, 0.6f),
                new OreData(Blocks.oreTitanium, 0.45f),
                new OreData(Blocks.oreThorium, 0.3f),
                new OreData(Blocks.oreBeryllium, 0.8f),
                new OreData(Blocks.oreTungsten, 0.5f),
                new OreData(Blocks.oreCrystalThorium, 0.3f)
        );
        corePositions.add(new Point2(50, (int)(height * 0.75f)));
        corePositions.add(new Point2(50, (int)(height * 0.50f)));
        corePositions.add(new Point2(50, (int)(height * 0.25f)));
        corePositions.add(new Point2(width - 50, (int)(height * 0.75f)));
        corePositions.add(new Point2(width - 50, (int)(height * 0.50f)));
        corePositions.add(new Point2(width - 50, (int)(height * 0.25f)));

        spawnPositions.add(new Point2(width -10, (int)(height * 0.10f)));
        spawnPositions.add(new Point2(width - 10, (int)(height * 0.50f)));
        spawnPositions.add(new Point2(width - 10, (int)(height * 0.90f)));
        spawnPositions.add(new Point2(10, (int)(height * 0.10f)));
        spawnPositions.add(new Point2(10, (int)(height * 0.50f)));
        spawnPositions.add(new Point2(10, (int)(height * 0.90f)));


        defineCoreAndSpawnPositions();

        ores = initializeOres();
        initializeFloorWalls();

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                tiles.set(x, y, new Tile(x, y, Blocks.stone.id, Blocks.air.id, Blocks.air.id));
            }
        }
        generateFloorTiles(floors);
        generateWallsTiles(walls);

        ores(ores);
        generateWater();
        applyScatter(0.10f,0.008f,0.10f,0.01f);

        trimDark();
        decoration(0.56f);

        median(2);

        prepareCoreArea();
        Schematics.placeLaunchLoadout(coreSpawnPos.x,coreSpawnPos.y);
        spawnEnemiesAreaGen(spawnPos);
        ensureOreConnectivity();
        ensureEnemiesConnectivity();
    }

    @Override
    public void trimDark(){
        float safeZone = (width/2f) * 0.85f;
        for(Tile tile : tiles) {
            float distance = Mathf.dst(tile.x, tile.y, width/2f, height/2f);
            if((distance > safeZone + noise(tile.x , tile.y , 3, 0.5f, 60f) * 30f)  ){
                tile.setBlock(tile.floor().wall);
            }
        }
    }
    private void prepareCoreArea(){
        int cx = coreSpawnPos.x;
        int cy = coreSpawnPos.y;

        Geometry.circle(cx, cy, width, height, 7, (x, y) -> {
            Tile t = tiles.get(x, y);
            if(t == null) return;
            t.setFloor(Blocks.stone.asFloor());
            t.setBlock(Blocks.air);
            t.setOverlay(Blocks.air);
        });
    }



    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag) {
        return Simplex.noise2d((int) seed , octaves, falloff, 1f / scl, x, y) * (float)mag;
    }


    private void initializeFloorWalls(){
        int counterFloor=0;
        int counterWall=0;
        while(counterFloor < 4 || counterWall < 4){
            Block b = allBlocks.random(rand);
            if(b instanceof Floor f && counterFloor < 4){
                if(!f.isLiquid && !f.isDeep() && b != Blocks.space) {
                    floors.add(b);
                    counterFloor++;
                }
            }else if(b instanceof StaticWall && counterWall < 4){
                walls.add(b);
                counterWall++;
            }
        }
    }
    private void generateLayer(Seq<Block> blocksToDraw, Boolean type, int sum, int oct, float fallout) {
        for (int i = blocksToDraw.size - 1; i >= 0; i--) {
            Block b = blocksToDraw.get(i);
            float threshold = 0.55f + i * 0.1f;

            pass((int x, int y) -> {
                if (noise(x + sum, y + sum, oct, fallout, 45f) > threshold) {
                    if (type) {
                        floor = b;
                    } else {
                        block = b;
                    }
                }
            });
        }
    }
    //corrigir o tipo de floor
    private void generateFloorTiles(Seq<Block> blocksToDraw) {
        generateLayer(blocksToDraw,true, 900 , 4 , 0.7f);
    }

    private void generateWallsTiles(Seq<Block> blocksToDraw) {
        generateLayer(blocksToDraw,false, 0, 3, 0.5f);
    }
    private void generateWater(){
        int randomWater = rand.random(2,4);
        int i = 0;

        while(i <= randomWater) {
            int posX = rand.random(0, width);
            int posY = rand.random(0, height);
            int waterDim = rand.random(3, 5);
            if (Mathf.dst(posX, posY, width / 2f, height / 2f) < width / 3f){
                Geometry.circle(posX, posY, width, height, 7, (int x, int y) -> {
                    Tile tile = tiles.get(x, y);
                    float dst = Mathf.dst(posX, posY, x, y);
                    if (dst < waterDim) {
                        tile.setFloor(Blocks.water.asFloor());
                    } else {
                        tile.setFloor(Blocks.darksand.asFloor());
                    }
                    tile.setBlock(Blocks.air);
                });
                i++;
            }
        }
    }

    private Seq<Block> initializeOres() {
        Seq<Block> result = new Seq<>();
        int oresToAdd = rand.random(5, 7);
        Seq<OreData> myOres = oresToSpawn.copy();
        shuffleSeq(myOres,rand);
        for (OreData ore : myOres) {
            if (result.size >= oresToAdd) {
                break;
            }
            if (rand.random(0f, 1f) < ore.probability) {
                result.add(ore.block);
            }
        }
        return result;
    }

    private void ensureOreConnectivity(){
        Tile core = tiles.getn(width / 2, height / 2);
        Seq<Tile> oryTiles=new Seq<>();
        for(Tile tile : tiles){
            if(tile.overlay() != Blocks.air){
                oryTiles.add(tile);
            }
        }
        shuffleSeq(oryTiles,rand);

        for (Tile t : oryTiles) {
            if(!rand.chance(0.05)) continue;
            Seq<Tile> path = getPathToCore(t);
            brush(path, rand.random(1,3));
        }
    }
    private void ensureEnemiesConnectivity(){
        Seq<Tile> path = getPathToCore(tiles.get(spawnPos.x, spawnPos.y));
        brush(path,rand.random(2,5));

    }

    private Seq<Tile> getPathToCore(Tile startTile) {
        Tile closestCoreBarrier=getClosestBoundaryTile(startTile);
        return pathfind(
                startTile.x, startTile.y,
                closestCoreBarrier.x, closestCoreBarrier.y ,
                tile -> {
                    float cost;
                    if(tile.solid()){
                        cost = 100f;
                    }else  cost  = 1f;

                    float dstCore = Mathf.dst(tile.x, tile.y, coreSpawnPos.x, coreSpawnPos.y);
                    if(dstCore < 15f) {
                        cost += 1000000f;
                    }
                    return cost;
                },
                Astar.manhattan
        );
    }


    /**
     * Do not modify tiles here. This is only for specialized configuration.
     *
     * @param tiles
     */
    @Override
    public void postGenerate(Tiles tiles) {
        super.postGenerate(tiles);
    }
    private void spawnEnemiesAreaGen(Point2 position){
        int radious = 10;

        Geometry.circle(position.x,position.y, width, height, radious, (int x, int y) -> {
            Tile t = tiles.get(x,y);
            if(t!=null){
                t.setBlock(Blocks.air);
                t.setOverlay(Blocks.air);
                if(position.x == x && position.y == y ){
                    t.setOverlay(Blocks.spawn);
                }
            }

        });
    }


    private <T> void shuffleSeq(Seq<T> seq, Rand rand){
        for(int i = seq.size - 1; i > 0; i--){
            int j = rand.random(i);
            T tmp = seq.items[i];
            seq.items[i] = seq.items[j];
            seq.items[j] = tmp;
        }
    }

    private void applyScatter(float chanceFloor1, float chanceFloor2, float chanceWall1, float chanceWall2) {

        scatter(floors.get(0), floors.get(1), chanceFloor1);
        scatter(floors.get(1), floors.get(2), chanceFloor2);

        scatter(walls.get(0), walls.get(1), chanceWall1);
        scatter(walls.get(1), walls.get(2), chanceWall2);
    }




    private void terrainGen(){
        float terrainScale     = rand.random(30f, 80f);
        float terrainMagnitude = rand.random(0.5f, 1.5f);
        float centerMagnitude  = rand.random(0.0f, 0.8f);


    }
    private Seq<Tile> getCoreBoundaryTiles() {
        Seq<Tile> tilesOut = new Seq<>();
        int cx = coreSpawnPos.x;
        int cy = coreSpawnPos.y;
        int radius = 7;

        Geometry.circle(cx, cy, width, height, radius, ( x, y) -> {
            Tile t = tiles.get(x, y);
            if(Mathf.dst(cx,cy,t.x,t.y) > radius - 1){
                tilesOut.add(t);
            }

        });

        return tilesOut;
    }
    private Tile getClosestBoundaryTile(Tile tile) {
        Seq<Tile> boundary = getCoreBoundaryTiles();
        Tile closestTile = null;
        float bestDst = Float.MAX_VALUE;

        for (Tile t : boundary) {
            float dst = Mathf.dst(tile.x,tile.y, t.x, t.y);
            if (dst < bestDst) {
                closestTile = t;
                bestDst = dst;
            }
        }

        return closestTile;
    }

    private void defineCoreAndSpawnPositions(){
        int shotCaller = rand.random(0,5);
        coreSpawnPos = corePositions.get(shotCaller);
        spawnPos = spawnPositions.get(shotCaller);
    }


}