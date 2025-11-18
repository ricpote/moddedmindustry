package mindustry.maps.generators;


import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.ai.Astar;
import mindustry.ai.BaseRegistry;
import mindustry.core.World;
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

import java.util.Arrays;


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


    private Seq<Block> floors = new Seq<>();
    private Seq<Block> walls = new Seq<>();
    private Seq<Block> allBlocks = new Seq<>();
    private Seq<Block> ores = new Seq<>();
    public WeeklyGenerator(){
        super();
        allBlocks = Vars.content.blocks();
        oresToSpawn = new Seq<>();

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
        trimDark();
        decoration(0.56f);
        median(2);
        Schematics.placeLaunchLoadout(width/2, height/2);
    }

    @Override
    public void trimDark(){
        float safeZone = (width/2f) * 0.85f;
        for(Tile tile : tiles) {
            float distance = Mathf.dst(tile.x, tile.y, width/2f, height/2f);
            if(distance > safeZone + noise(tile.x , tile.y , 3, 0.5f, 60f) * 30f){
                tile.setBlock(tile.floor().wall);
            }
        }
    }



    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag) {
        return Simplex.noise2d((int) seed , octaves, falloff, 1f / scl, x, y) * (float)mag;
    }



    private void initializeFloorWalls(){
        int counterFloor=0;
        int counterWall=0;
        while(counterFloor < 3 || counterWall < 3){
           Block b = allBlocks.random(rand);
            if(b instanceof Floor && counterFloor<3){
                floors.add(b);
                counterFloor++;
            }else if(b instanceof StaticWall && counterWall<3){
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

    private Seq<Block> initializeOres() {
        Seq<Block> result = new Seq<>();
        int oresToAdd = rand.random(5, 7);
        Seq<OreData> myOres = oresToSpawn.copy().shuffle();
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

    /**
     * Do not modify tiles here. This is only for specialized configuration.
     *
     * @param tiles
     */
    @Override
    public void postGenerate(Tiles tiles) {
        super.postGenerate(tiles);
    }

}
