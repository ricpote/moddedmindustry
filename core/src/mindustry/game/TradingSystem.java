package mindustry.game;

import arc.util.Time;
import mindustry.Vars;

public class TradingSystem {

    public static final int MAX_OFFERS = 3;
    public static final int MAX_TRADES_DONE = 5;
    public static final float REFRESH_INTERVAL = 900f;

    private TradeOffer[] offers = new TradeOffer[MAX_OFFERS];
    private int used_times = 0;
    private float timer = REFRESH_INTERVAL;

    public TradingSystem(){
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

    public TradeOffer getOffer(int i) {
        return offers[i];
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
        if(core.items.get(trade.give) < trade.giveAmount)
            return false;

        core.items.remove(trade.give, trade.giveAmount);
        core.items.add(trade.receive, trade.receiveAmount);
        tradeMade();
        return true;
    }
}
