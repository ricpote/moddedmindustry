package mindustry.game;

import arc.struct.Seq;
import arc.util.Strings;

public class WeeklyRankingInfo {
    public static class ScoreEntry {
        public String playerName;
        public int wave;
        public ScoreEntry() {}
        public ScoreEntry(String playerName, int wave) {
            this.playerName = playerName;
            this.wave = wave;
        }
    }
    public Seq<ScoreEntry> rankings = new Seq<>();
    private int seedOfCurrentMap;
    public WeeklyRankingInfo() {
        rankings.add(new ScoreEntry("Player 1", 15));
        rankings.add(new ScoreEntry("Player 2", 10));
        rankings.add(new ScoreEntry("Player 3", 7));
        seedOfCurrentMap = 0;
    }
    public int getSeedOfCurrentMap() {
        return seedOfCurrentMap;
    }
    public void setSeedOfCurrentMap(int seed) {
        seedOfCurrentMap = seed;
    }
    public void addScore(String playerName, int waveReached) {
        rankings.add(new ScoreEntry(playerName, waveReached));

        rankings.sort(s -> -s.wave);

        if (rankings.size > 10) {
            rankings.truncate(10);
        }
    }
    public void submitScore(String playerName, int newWave) {
        ScoreEntry existingEntry = rankings.find(e ->
                Strings.stripColors(e.playerName).equals(Strings.stripColors(playerName))
        );
        if (existingEntry != null) {
            if (newWave > existingEntry.wave) {
                rankings.remove(existingEntry);
                addScore(playerName, newWave);
            }
        } else {
            addScore(playerName, newWave);
        }
    }
}
