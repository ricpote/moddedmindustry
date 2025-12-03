package mindustry.service;
import arc.struct.Seq;
import mindustry.content.*;
import arc.Core;
import arc.math.Rand;
import arc.files.Fi;
import arc.struct.StringMap;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.*;
import mindustry.maps.*;
import mindustry.maps.generators.WeeklyGenerator;
import mindustry.io.SaveIO;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter;

import java.util.Random;

public class WeeklyChalServ {
    private long mapSeed;
    private int width;
    private int height;
    private Rand randSize;

    private InfiniteWaveClass waveGen = new InfiniteWaveClass();

    private static final String NAME_FILE_INFO = "weekly-challenge-info";
    private static final String SAVE_FILE_NAME = "weeklyFile";
    private static final String NAME_FILE_RANKING = "weeklyRanking";

    public WeeklyGameInfo info = new WeeklyGameInfo();
    public WeeklyRankingInfo rankingInfo = new WeeklyRankingInfo();

    public WeeklyChalServ(){
        loadInfo();
        loadRanking();
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
        Vars.state.rules.spawns.clear();
        Core.settings.remove(NAME_FILE_INFO);
        Core.settings.forceSave();
    }
    public void updateWaves(){
        if(Vars.state.rules.spawns == null) return;
        Vars.state.rules.spawns.clear();
        int currentWave = Vars.state.wave;
        for(int i = 0; i < 5; i++){
            Vars.state.rules.spawns.addAll(waveGen.generate(currentWave + i));
        }
    }
    public void saveRanking() {
        Core.settings.putJson(NAME_FILE_RANKING, rankingInfo);
        Core.settings.forceSave();
    }

    public void loadRanking() {
        rankingInfo = Core.settings.getJson(NAME_FILE_RANKING, WeeklyRankingInfo.class, WeeklyRankingInfo::new);
    }

    public void clearRanking() {
        rankingInfo = new WeeklyRankingInfo();
        rankingInfo.setSeedOfCurrentMap((int) Vars.getWeeklySeed());
        Core.settings.remove(NAME_FILE_RANKING);
        Core.settings.forceSave();
    }
    public void startWeeklyChalGame() {
        mapSeed = Vars.getWeeklySeed();
        Fi file = Vars.saveDirectory.child(SAVE_FILE_NAME + "." + Vars.saveExtension);
        if (info.seed == mapSeed && info.isGameActive && file.exists()) {
            try {
                Vars.ui.loadfrag.show("@loading");
                SaveIO.load(file);
                Vars.state.wave = info.wave;
                //Vars.InfSpawner.setSpeciesCount(info.speciesCount);
                Vars.state.map.tags.put("weekly", "Weekly Challenge");
                updateWaves();
                Vars.state.set(mindustry.core.GameState.State.playing);
                Vars.ui.loadfrag.hide();
            } catch (Throwable e) {
                info.isGameActive = false;
                saveInfo();
                file.delete();
                Vars.ui.loadfrag.hide();
            }
        } else {

            if(rankingInfo.getSeedOfCurrentMap() != mapSeed){

                clearRanking();
            }
            clearInfo();


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
                updateWaves();
                Vars.logic.play();

                Vars.ui.loadfrag.hide();

            } catch (Throwable e) {
                Vars.ui.loadfrag.hide();
            }
        });
    }


}