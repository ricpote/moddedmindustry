package mindustry.ui.dialogs;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.util.*;
import mindustry.Vars;
import mindustry.ui.*;
import mindustry.game.TradingSystem;
import mindustry.game.TradeOffer;
import mindustry.world.blocks.trader.TraderBlock.TraderBuild;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;


public class TradingDialog extends BaseDialog{

    private TradingSystem tradingSystem;
    private Table offersTable;
    private Timer.Task updateTask;
    private TraderBuild trader;

    public TradingDialog() {
        super("Tim Cheese");

        closeOnBack();
        addCloseButton();

        TextureRegion tex = Core.atlas.find("tim-cheese");

        cont.table(t -> {
            t.center();
            t.image(tex).size(128f).padBottom(20f);
        }).row();

        cont.table(t -> {
            t.center();
            t.pane(offersTable = new Table()).width(600f).center();
        }).center();

        cont.margin(20f);

        shown(() -> {
            updateTask = Timer.schedule(this::updateOffers, 0.5f, 1.0f);
        });

        hidden(() -> {
            if(updateTask != null) {updateTask.cancel();}
        });
    }

    public void show(TraderBuild build){
        this.trader = build;
        this.tradingSystem = build.tradingSystem;
        updateOffers(); //draws for the first time
        super.show();
    }

    public void updateOffers(){
        offersTable.clear();
        offersTable.defaults().growX().pad(5);

        TradeOffer[] offers = tradingSystem.getOffers();

        for(int i = 0; i < offers.length; i++){
            int offerIdx = i;
            TradeOffer offer = offers[i];

            offersTable.table(Styles.grayPanel, t -> {
                t.left();

                //item to give
                t.image(offer.give.uiIcon).size(40f).padRight(5);
                t.add("-" + offer.giveAmount).color(Pal.remove).padRight(20);

                //arrow
                t.image(Icon.rightSmall).color(Pal.accent).size(32f).padRight(26);

                //item to get
                t.image(offer.receive.uiIcon).size(40).padRight(5);
                t.add("+" + offer.receiveAmount).color(Pal.items).padRight(40);

                t.add().growX();

                //trade button
                t.button("Trocar", Icon.ok, Styles.defaultt, () -> {
                    if(trader.core() != null && tradingSystem.canTrade(offerIdx)){

                        tradingSystem.executeTrade(offerIdx, trader);

                        Vars.ui.showInfo("Negócio fechado!!!");
                        updateOffers();
                    } else {
                        Vars.ui.showErrorMessage("Não tem " + offer.give.localizedName + " suficiente!!");
                    }
                }).size(150f, 60f).right();

            }).growX().row();
        }

        long timeRemainingMillisec = (long) tradingSystem.getTimeRemaining() * 1000;

        offersTable.add("Próximo Refresh:[] " + Strings.formatMillis(timeRemainingMillisec)).colspan(5).left().row();
        offersTable.add("Trocas Restantes:[] " + (TradingSystem.MAX_TRADES_DONE - tradingSystem.getUsedTimes()));
    }
}
