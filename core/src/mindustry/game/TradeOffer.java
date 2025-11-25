package mindustry.game;

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

        while (receive == give || !giveItemRarity.tradePossible(receiveItemRarity)) {
            receive = Vars.content.items().random();
            receiveItemRarity = ItemRarity.getItemRarity(receive);
        }

        int giveAmount = 10;
        int receiveAmount = 10;

        return new TradeOffer(give, giveAmount, receive, receiveAmount);
    }
}
