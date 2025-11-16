package mindustry.game;

import arc.Events;
import arc.math.Mathf;
import arc.util.Time;
import arc.struct.Seq;
import arc.math.geom.Point2;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.gen.Building;
import mindustry.world.Tile;
import mindustry.world.Build;
import mindustry.content.Blocks;


public class TsunamiSpawner {

    private float timer = 0f;
    private float nextTsunami = 0f;

    public TsunamiSpawner() {
        Events.on(EventType.WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });
    }

    public void update() {
        if (!Vars.state.isPlaying()) return;

        timer += Time.delta;
        if (timer >= nextTsunami) {
            spawnTsunami();
            timer = 0f;
        }
    }

    private void scheduleNext() {
        nextTsunami = Mathf.random(3000f, 9000f);
    }

    private static void spawnTsunami() {
        if (!Vars.state.isPlaying()) {
            return;
        }

        float x, y;
        int centerTileX, centerTileY;
        int attempt = 0;
        do {
            x = Mathf.random(Vars.world.unitWidth());
            y = Mathf.random(Vars.world.unitHeight());

            centerTileX = (int) (x / Vars.tilesize);
            centerTileY = (int) (y / Vars.tilesize);

            attempt++;
            //tries a max of 100 times to find a not Darkened place on the map for the earthquake
        } while (Vars.world.tile(centerTileX, centerTileY).isDarkened() && attempt < 100);

        Events.fire(new EventType.EarthquakeEvent(x, y, 0, 0));

    }
}

