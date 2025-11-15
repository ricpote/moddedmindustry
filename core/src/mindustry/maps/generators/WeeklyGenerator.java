package mindustry.maps.generators;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import mindustry.world.Tiles;
import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.*;
import mindustry.ai.Astar.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
public class WeeklyGenerator extends BasicGenerator {

    public WeeklyGenerator(){
        super();
    }
    public void generate(Tiles tiles, WorldParams params) {
        long weeklySeed = getWeeklySeed();
        rand.setSeed(new Random(weeklySeed).nextLong());



    }

    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag) {
        return 0;
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

    private long getWeeklySeed() {
        LocalDate start = LocalDate.of(2001, 1, 1);
        LocalDate now = LocalDate.now();
        return ChronoUnit.WEEKS.between(start, now);
    }

}
