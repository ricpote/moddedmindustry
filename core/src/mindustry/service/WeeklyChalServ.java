package mindustry.service;

import arc.Core;
import arc.math.Rand;
import arc.files.Fi;
import arc.struct.StringMap;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.maps.*;
import mindustry.maps.generators.WeeklyGenerator;
import mindustry.game.WeeklyGameInfo;
import mindustry.io.SaveIO;
import mindustry.world.*;

import java.util.Random;

public class WeeklyChalServ {
    private long mapSeed;
    private int width;
    private int height;
    private Rand randSize;
//REVERT
    private static final String NAME_FILE_INFO = "weekly-challenge-info";
    private static final String SAVE_FILE_NAME = "weeklyFile";

    public WeeklyGameInfo info = new WeeklyGameInfo();

    public WeeklyChalServ(){
        loadInfo();
    }
    public void saveInfo() {
        Core.settings.putJson(NAME_FILE_INFO, info);
        Core.settings.forceSave();
    }

    public void loadInfo() {
        info = Core.settings.getJson(NAME_FILE_INFO, WeeklyGameInfo.class, WeeklyGameInfo::new);
    }

    public void clearInfo() {
        info = new WeeklyGameInfo();
        Core.settings.remove(NAME_FILE_INFO);
        Core.settings.forceSave();
    }
    public void startWeeklyChalGame() {
        mapSeed = Vars.getWeeklySeed();
        Fi file = Vars.saveDirectory.child(SAVE_FILE_NAME + "." + Vars.saveExtension);
        if (info.seed == mapSeed && info.isGameActive && file.exists()) {
            try {
                Vars.ui.loadfrag.show("@loading");
                SaveIO.load(file);
                Vars.state.map.tags.put("name", "Weekly Challenge");
                Vars.state.set(mindustry.core.GameState.State.playing);
                Vars.ui.loadfrag.hide();
            } catch (Throwable e) {
                info.isGameActive = false;
                saveInfo();
                file.delete();
                Vars.ui.loadfrag.hide();
            }
        } else {
            startNewGame();
        }
    }

    private void startNewGame() {
        Vars.ui.loadfrag.show("@generating");
        info.seed = mapSeed;
        info.isGameActive = true;
        info.wave = 1;
        info.hasWon = false;
        saveInfo();
        Core.app.post(() -> {
            try {
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
                Vars.state.map = new Map(StringMap.of("weekly", "Weekly Challenge"));

                Vars.logic.play();

                Vars.ui.loadfrag.hide();

            } catch (Throwable e) {
                Vars.ui.loadfrag.hide();
            }
        });
    }
}