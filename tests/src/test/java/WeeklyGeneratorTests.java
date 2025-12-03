import mindustry.game.InfiniteWaveClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeeklyGeneratorTests {
    private InfiniteWaveClass inf;
    @Test
    @BeforeEach
    public void testWeeklyGenerator() {

        inf=new InfiniteWaveClass();

    }
    @Test
    public void testDifficultyIncrease() {
        double diff1= inf.computeDifficulty(1);
        double diff2= inf.computeDifficulty(10);
        assertTrue(diff1<diff2);
    }
    @Test
    public void testAmountIncrease(){
        int amount5=inf.computeAmountForTests(5,1,1);
        int amount10=inf.computeAmountForTests(10,1,1);
        int amount50=inf.computeAmountForTests(50,1,1);
        assertTrue(amount5<amount10);
        assertTrue(amount10<amount50);

    }
    @Test
    public void testSpeciesCount(){
        int count=inf.computeSpeciesCountForTests(1);
        int count30=inf.computeSpeciesCountForTests(30);
        int count40=inf.computeSpeciesCountForTests(40);
        assertTrue((count>=1)&&(count<=3));
        assertTrue((count30>=1)&&(count30<=3));
        assertTrue((count40 >= 1) && (count40 <= 3));
    }
}
