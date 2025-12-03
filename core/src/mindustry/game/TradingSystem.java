package mindustry.game;

import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.ui.dialogs.TradingDialog;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.trader.TraderBlock;

public class TradingSystem {

    public static final TradingSystem instance =  new TradingSystem();

    public static final int MAX_OFFERS = 5;
    public static final int MAX_TRADES_DONE = 5;
    public static final float REFRESH_INTERVAL = 36000f;

    public TradingDialog dialog;

    private TradeOffer[] offers = new TradeOffer[MAX_OFFERS];
    private int used_times = 0;
    private float timer = REFRESH_INTERVAL;

    private TradingSystem(){
        dialog = new TradingDialog();
        generateNewOffers();
    }

    private void generateNewOffers(){
        for(int i = 0; i < MAX_OFFERS; i++){
            offers[i] = TradeOffer.createRandomTrade();
        }
        used_times = 0;
        timer = REFRESH_INTERVAL;
    }

    public void update(){
        timer -= Time.delta;
        if (timer <= 0f) {
            generateNewOffers();
        }
    }

    public float getTimeRemaining(){
        return timer;
    }

    public int getUsedTimes() {
        return used_times;
    }

    public TradeOffer[] getOffers(){
        return offers;
    }

    public void tradeMade() {
        used_times++;
        if (used_times == MAX_TRADES_DONE) {
            generateNewOffers();
        }
    }

    public boolean canTrade(int i) {
        TradeOffer trade = offers[i];
        var core = Vars.player.team().core();
        return core.items.get(trade.give) >= trade.giveAmount;
    }

    //pre condiçao canTrade()
    public void executeTrade(int offerIdx, TraderBlock.TraderBuild trader){
        if(offerIdx < 0 || offerIdx >= MAX_OFFERS){
            return;
        }
        TradeOffer offer = offers[offerIdx];
        CoreBlock.CoreBuild core = trader.core();
        if(core == null){return;}

        //give items
        core.items.remove(offer.give, offer.giveAmount);

        //receive items
        core.items.add(offer.receive, offer.receiveAmount);

        tradeMade();

        Fx.spawn.at(trader);
    }
}
