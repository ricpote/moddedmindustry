package mindustry.service;

import arc.Core;
import arc.math.Rand;

import arc.struct.StringMap;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.maps.*;
import mindustry.maps.generators.WeeklyGenerator;
import mindustry.world.*;



import java.util.Random;


public class WeeklyChalServ {
    private long mapSeed;
    private int width;
    private int height;
    private Rand randSize ;

    public WeeklyChalServ(){


    }
    public void startWeeklyChalGame(){

        Vars.ui.loadfrag.show("@generating");

        Core.app.post(() -> {
            try {
                mapSeed = Vars.getWeeklySeed();

                randSize = new Rand(mapSeed);
                this.width = randSize.random(150, 250);
                this.height = randSize.random(150, 250);

                Rules rulesChallenge = new Rules();
                Gamemode.infinite.apply(rulesChallenge);

                Vars.state.rules = rulesChallenge;

                Vars.world.loadGenerator(this.width, this.height, (Tiles tiles) -> {
                    WorldParams params = new WorldParams();
                    params.mapSeed = rulesChallenge.seed;
                    params.width = this.width;
                    params.height = this.height;

                    rulesChallenge.mapGenerator.generate(tiles, params);
                });

                Vars.logic.play();

                Vars.ui.loadfrag.hide();
                Team.sharded.core();
            } catch (Exception e) {
                Vars.ui.loadfrag.hide();
                Vars.ui.showException("Erro ao Gerar Mapa", e);
            }
        });
    }
}
