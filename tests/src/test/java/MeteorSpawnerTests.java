
import mindustry.game.MeteorSpawner;
import mindustry.game.NaturalDisasterSpawner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeteorSpawnerTests {

    static class TestMeteorSpawner extends MeteorSpawner {
        public void callScheduleNext(){
            scheduleNext();
        }
        public float getNextEvent(){
            return nextEvent;
        }
        public float getTimer(){
            return timer;
        }
    }

    @Test
    public void testMeteorScheduleNextRange(){
        TestMeteorSpawner spawner = new TestMeteorSpawner();
        spawner.callScheduleNext();

        float next = spawner.getNextEvent();
        assertTrue(next >= 3000f && next <= 9000f);
    }

    @Test
    public void testMeteorTimerStartsAtZero(){
        TestMeteorSpawner spawner = new TestMeteorSpawner();
        float timer = spawner.getTimer();
        assertTrue(timer == 0f);
    }

    @Test
    public void testMeteorIsNaturalDisasterSpawner(){
        MeteorSpawner spawner = new MeteorSpawner();
        assertTrue(spawner instanceof NaturalDisasterSpawner);
    }

}


