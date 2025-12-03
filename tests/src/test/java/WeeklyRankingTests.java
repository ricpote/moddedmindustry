import mindustry.game.WeeklyRankingInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeeklyRankingTests {

    @Test
    public void testRankingSortsCorrectly() {
        WeeklyRankingInfo info = new WeeklyRankingInfo();
        info.rankings.remove(0);
        info.rankings.remove(0);
        info.rankings.remove(0);

        info.addScore("PlayerLow", 5);
        info.addScore("PlayerHigh", 50);
        info.addScore("PlayerMid", 25);

        assertEquals(50, info.rankings.get(0).wave);
        assertEquals("PlayerHigh", info.rankings.get(0).playerName);
    }

    @Test
    public void testRankingLimit() {
        WeeklyRankingInfo info = new WeeklyRankingInfo();
        info.rankings.remove(0);
        info.rankings.remove(0);
        info.rankings.remove(0);


        for(int i=0; i<15; i++) {
            info.addScore("P" + i, i);
        }
        assertEquals(10, info.rankings.size);
    }

    @Test
    public void testAddScoreLower() {
        WeeklyRankingInfo info = new WeeklyRankingInfo();
        info.rankings.remove(0);
        info.rankings.remove(0);
        info.rankings.remove(0);
        info.addScore("Player", 5);
        info.addScore("Player", 4);
        assertEquals(5, info.rankings.get(0).wave);
    }
    @Test
    public void testAddScoreHigher() {
        WeeklyRankingInfo info = new WeeklyRankingInfo();
        info.rankings.remove(0);
        info.rankings.remove(0);
        info.rankings.remove(0);
        info.addScore("Player", 6);
        info.addScore("Player", 7);
        assertEquals(7, info.rankings.get(0).wave);
    }
}
