package mindustry.world.blocks.trader;

import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.type.Category;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.gen.Building;
import mindustry.game.TradingSystem;


public class TraderBlock extends Block {
    public TraderBlock(String name) {
        super(name);

        localizedName = "Tim Cheese";
        update = true;
        size = 2; //2x2
        configurable = true;
        sync = true;
        solid = true; //cannot go through
        hasShadow = false;
        category = Category.effect;

        //invisible in the build menu
        buildVisibility = BuildVisibility.hidden;
        destructible = false; //perma there

        stats.add(Stat.cooldownTime, TradingSystem.REFRESH_INTERVAL / 60f, StatUnit.seconds);

        buildType = TraderBuild::new;
    }

    @Override
    public boolean canBreak(Tile tile){
        return false;
    }

    public static class TraderBuild extends Building {
        public TradingSystem tradingSystem = TradingSystem.instance;

        @Override
        public void updateTile(){
            super.updateTile();
            tradingSystem.update();
        }

        @Override
        public boolean onConfigureBuildTapped(Building other){
            if(this != other) {return true;}
            if(team.core() == null){return false;}

            tradingSystem.dialog.show(this);
            return true;
        }
    }
}
