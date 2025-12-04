package mindustry.game;

import mindustry.type.Item;
import mindustry.content.Items;
public enum ItemRarity {
    COPPER(Items.copper, 1),
    LEAD(Items.lead, 1),
    SCRAP(Items.scrap, 1),
    SAND(Items.sand, 1),

    COAL(Items.coal, 2),
    GRAPHITE(Items.graphite, 2),
    SILICON(Items.silicon, 2),
    METAGLASS(Items.metaglass, 2),
    PYRATITE(Items.pyratite, 2),

    TITANIUM(Items.titanium, 3),
    PLASTANIUM(Items.plastanium, 3),
    PHASE_FABRIC(Items.phaseFabric, 3),
    SPORE_POD(Items.sporePod, 3),
    OXIDE(Items.oxide, 3),

    THORIUM(Items.thorium, 4),
    SURGE_ALLOY(Items.surgeAlloy, 4),
    BLAST_COMPOUND(Items.blastCompound, 4),
    BERYLLIUM(Items.beryllium, 4),
    DORMANT_CYST(Items.dormantCyst,4),

    TUNGSTEN(Items.tungsten, 5),
    CARBIDE(Items.carbide, 5),
    FISSILE_MATTER(Items.fissileMatter, 5);

    private int rarity;
    private Item item;

    ItemRarity(Item item, int rarity) {
        this.item = item;
        this.rarity = rarity;
    }
    public int getRarity() {
        return rarity;
    }

    public Item getItem() {
        return item;
    }

    public boolean tradePossible(ItemRarity itemReceived) {
        return this.rarity >= itemReceived.getRarity() - 2;
    }

    public static ItemRarity getItemRarity(Item item){
        for(ItemRarity i : ItemRarity.values()){
            if(i.item.equals(item))
                return i;
        }
        return null;
    }
}
