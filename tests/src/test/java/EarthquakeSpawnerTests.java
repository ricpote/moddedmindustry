import arc.struct.Seq;
import mindustry.game.NaturalDisasterSpawner;
import mindustry.world.Tile;
import mindustry.game.EarthquakeSpawner;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class EarthquakeSpawnerTests {

    @Test
    public void testEarthquakeListsAreInitializedEmpty() throws Exception {
        EarthquakeSpawner s = new EarthquakeSpawner();

        Field affectedF = EarthquakeSpawner.class.getDeclaredField("affectedBuildings");
        Field posF = EarthquakeSpawner.class.getDeclaredField("allPotentialPositions");
        Field tilesF = EarthquakeSpawner.class.getDeclaredField("originalPrimaryTiles");

        affectedF.setAccessible(true);
        posF.setAccessible(true);
        tilesF.setAccessible(true);

        Seq<?> affected = (Seq<?>) affectedF.get(null);
        Seq<?> pos = (Seq<?>) posF.get(null);
        Seq<?> tiles = (Seq<?>) tilesF.get(null);

        assertTrue(affected.size == 0);
        assertTrue(pos.size == 0);
        assertTrue(tiles.size == 0);
    }

    @Test
    public void testEarthquakeScheduleNextChangesNextEvent() throws Exception {
        EarthquakeSpawner s = new EarthquakeSpawner();

        Field newf = NaturalDisasterSpawner.class.getDeclaredField("nextEvent");
        newf.setAccessible(true);
        float before = newf.getFloat(s);

        Method method = EarthquakeSpawner.class.getDeclaredMethod("scheduleNext");
        method.setAccessible(true);
        method.invoke(s);

        Field newf2 = NaturalDisasterSpawner.class.getDeclaredField("nextEvent");
        newf2.setAccessible(true);
        float after = newf.getFloat(s);

        assertTrue(before != after);
    }

    @Test
    public void testEarthquakeOriginalPrimaryTilesNotNull() throws Exception {
        new EarthquakeSpawner();

        Field f = EarthquakeSpawner.class.getDeclaredField("originalPrimaryTiles");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Seq<Tile> originals = (Seq<Tile>) f.get(null);

        assertTrue(originals != null);
    }


}
