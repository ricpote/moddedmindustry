import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.game.TradeOffer;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.trader.TraderBlock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TraderTests {

    private void contentStarter(){
        Vars.content = new ContentLoader();
        Vars.content.createBaseContent();
    }

    @Test
    public void testTraderBlockHasCorrectDefaults(){
        contentStarter();

        TraderBlock block = new TraderBlock("tim-cheese-test");

        assertTrue(block.update);
        assertTrue(block.solid);
        assertTrue(block.configurable);
        assertFalse(block.destructible);
        assertEquals(2, block.size);
        assertNotNull(block.buildType);
    }

    @Test
    public void testBlockCanBreak(){
        contentStarter();
        TraderBlock block = new TraderBlock("tim-cheese-test");
        Tile tile = new Tile(1, 2);
        boolean test = block.canBreak(tile);
        assertFalse(test);
    }

    @Test
    public void testTradeOfferConstructor() {
        contentStarter();
        Item give = Vars.content.items().first();
        Item receive = Vars.content.items().get(1);

        TradeOffer offer = new TradeOffer(give, 50, receive, 30);

        assertEquals(give, offer.give);
        assertEquals(50, offer.giveAmount);
        assertEquals(receive, offer.receive);
        assertEquals(30, offer.receiveAmount);
    }

    @Test
    public void testDifferentItemsInATradeOffer(){
        contentStarter();
        for(int i = 0; i < 20; i++){
            TradeOffer offer = TradeOffer.createRandomTrade();
            assertNotEquals(offer.give, offer.receive);
        }
    }
}
