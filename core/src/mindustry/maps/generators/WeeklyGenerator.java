package mindustry.maps.generators;


import arc.util.noise.Simplex;
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

public class WeeklyGenerator extends BasicGenerator {
private long seed;

    public WeeklyGenerator(){
        super();
    }
    @Override
    public void generate(Tiles tiles, WorldParams params) {

        this.tiles = tiles;
        this.width = tiles.width;
        this.height = tiles.height;
        seed = params.mapSeed;
        this.rand.setSeed(params.mapSeed);

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                tiles.set(x, y, new Tile(x, y, Blocks.stone.id, Blocks.air.id, Blocks.air.id));
            }
        }

        pass((int x, int y) -> {
            if(noise(x, y, 3, 0.5f, 60f) > 0.65f){
                floor = Blocks.sand;
                block=Blocks.iceWall;

            }else {
               floor = Blocks.sand;
               ore= Blocks.oreTitanium;
            }
        });

        trimDark();


        Schematics.placeLaunchLoadout(width/2, height/2);
    }
    //@Override
    public void trimDark(){
        float safeZone = (width/2f) * 0.9f;
        for(Tile tile : tiles) {
            float distance = Mathf.dst(tile.x, tile.y, width/2f, height/2f);
            if(distance > safeZone + noise(tile.x, tile.y, 3, 0.5f, 60f) * 40f){
                tile.setBlock(tile.floor().wall);
            }
        }
    }



    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag) {
        return Simplex.noise2d((int) seed , octaves, falloff, 1f / scl, x, y) * (float)mag;
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
