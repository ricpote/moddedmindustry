package mindustry.game;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.type.Item;
public class TradeOffer {
    public Item give;
    public int giveAmount;
    public Item receive;
    public int receiveAmount;

    public TradeOffer(Item give, int giveAmount, Item receive, int receiveAmount){
        this.give = give;
        this.giveAmount = giveAmount;
        this.receive = receive;
        this.receiveAmount = receiveAmount;
    }

    public static TradeOffer createRandomTrade() {
        Item give = Vars.content.items().random();
        Item receive = Vars.content.items().random();

        ItemRarity giveItemRarity = ItemRarity.getItemRarity(give);
        ItemRarity receiveItemRarity = ItemRarity.getItemRarity(receive);

        while (receive == give || receiveItemRarity == null || giveItemRarity == null || !giveItemRarity.tradePossible(receiveItemRarity)) {
            receive = Vars.content.items().random();
            receiveItemRarity = ItemRarity.getItemRarity(receive);
        }

        float giveAmount = 100f + Mathf.random(0f, 100f);

        float rarityRatio = (float)giveItemRarity.getRarity() / (float)receiveItemRarity.getRarity();

        int receiveAmount = (int)(rarityRatio * giveAmount + Mathf.random(-10f, 10f));

        return new TradeOffer(give, (int)giveAmount, receive, receiveAmount);
    }
}
