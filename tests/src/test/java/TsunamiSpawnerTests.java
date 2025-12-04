import arc.math.geom.Point2;
import mindustry.game.NaturalDisasterSpawner;
import mindustry.game.TsunamiSpawner;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class TsunamiSpawnerTests {

    @Test
    public void testTsunamiDirectionsLengthIsEight() throws Exception {
        TsunamiSpawner spawner = new TsunamiSpawner();

        Field f = TsunamiSpawner.class.getDeclaredField("directions");
        f.setAccessible(true);
        Point2[] directions = (Point2[]) f.get(spawner);

        assertTrue(directions != null && directions.length == 8);
    }

    @Test
    public void testTsunamiDirectionsAreCorrect() throws Exception {
        TsunamiSpawner s = new TsunamiSpawner();

        Field f = TsunamiSpawner.class.getDeclaredField("directions");
        f.setAccessible(true);
        Point2[] d = (Point2[]) f.get(s);

        assertTrue(d[0].x == 0  && d[0].y == 1);   // Up
        assertTrue(d[1].x == -1 && d[1].y == 1);   // Upper Left Diagonal
        assertTrue(d[2].x == -1 && d[2].y == 0);   // Left
        assertTrue(d[3].x == -1 && d[3].y == -1);   // Lower Left Diagonal
        assertTrue(d[4].x == 0  && d[4].y == -1);  // Down
        assertTrue(d[5].x == 1 && d[5].y == -1);   // Lower Right Diagonal
        assertTrue(d[6].x == 1  && d[6].y == 0);   // Right
        assertTrue(d[7].x == 1 && d[7].y == 1);   // Upper Right Diagonal
    }

    @Test
    public void testTsunamiInternalCollectionsAreEmptyAtStart() throws Exception {
        TsunamiSpawner s = new TsunamiSpawner();

        Field movesF = TsunamiSpawner.class.getDeclaredField("moves");
        Field affectedF = TsunamiSpawner.class.getDeclaredField("affectedBuildings");

        movesF.setAccessible(true);
        affectedF.setAccessible(true);

        arc.struct.ObjectMap<?, ?> moves = (arc.struct.ObjectMap<?, ?>) movesF.get(s);
        arc.struct.Seq<?> affected = (arc.struct.Seq<?>) affectedF.get(s);

        assertTrue(moves.size == 0);
        assertTrue(affected.size == 0);
    }

    //scheduleNext changes nextEvent
    @Test
    public void testTsunamiScheduleNextChangesValue() throws Exception {
        TsunamiSpawner s = new TsunamiSpawner();

        Field newf = NaturalDisasterSpawner.class.getDeclaredField("nextEvent");
        newf.setAccessible(true);
        float before = newf.getFloat(s);

        Method method = TsunamiSpawner.class.getDeclaredMethod("scheduleNext");
        method.setAccessible(true);
        method.invoke(s);

        Field newf2 = NaturalDisasterSpawner.class.getDeclaredField("nextEvent");
        newf2.setAccessible(true);
        float after = newf.getFloat(s);

        assertTrue(before != after);
    }

}
