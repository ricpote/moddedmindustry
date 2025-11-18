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
import arc.struct.ObjectMap;

public class TsunamiSpawner {

    private float timer = 0f;
    private float nextTsunami = 0f;

    private static final Point2[] directions = {
            new Point2(1, 0),    // 0º (Right)
            new Point2(1, 1),    // 45º (Upper Right Diagonal)
            new Point2(0, 1),    // 90º (Up)
            new Point2(-1, 1),   // 135º (Upper Left Diagonal)
            new Point2(-1, 0),   // 180º (Left)
            new Point2(-1, -1),  // 225º (Lower Left Diagonal)
            new Point2(0, -1),   // 270º (Down)
            new Point2(1, -1)    // 315º (Lower Right Diagonal)
    };

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
        int startTileX, startTileY;
        Tile startTile;

        int tsunamiFactor = Mathf.random(3, 7);
        int directionIndex = Mathf.random(0, 7);
        Point2 direction = directions[directionIndex];
        float angle = directionIndex * 45f;
        int dx = direction.x;
        int dy = direction.y;

        int attempts = 0;
        do {
            x = Mathf.random(Vars.world.unitWidth());
            y = Mathf.random(Vars.world.unitHeight());

            startTileX = (int) (x / Vars.tilesize);
            startTileY = (int) (y / Vars.tilesize);

            startTile = Vars.world.tile(startTileX, startTileY);

            if (startTile != null && startTile.floor().isLiquid) {
                break;
            }

            attempts++;
        } while (attempts < 100);

        if (startTile == null || !startTile.floor().isLiquid) {
            return;
        }

        ObjectMap<Building, Point2> moves = new ObjectMap<>();
        Seq<Building> affectedBuildings = new Seq<>();

        int waveX = startTileX;
        int waveY = startTileY;

        for (int i = 0; i < tsunamiFactor*10; i++) {
            waveX += dx;
            waveY += dy;

            Tile currentTile = Vars.world.tile(waveX, waveY);

            if (currentTile == null) {
                break;
            }

            //Calculates where the buildings will be placed
            for (int tx = waveX - tsunamiFactor; tx <= waveX + tsunamiFactor; tx++) {
                for (int ty = waveY - tsunamiFactor; ty <= waveY + tsunamiFactor; ty++) {
                    Tile targetTile = Vars.world.tile(tx, ty);

                    if (targetTile != null && targetTile.build != null) {
                        Building build = targetTile.build;

                        if (targetTile.x == build.tileX() && targetTile.y == build.tileY() && !moves.containsKey(build)) {

                            int newTileX = tx + dx * tsunamiFactor;
                            int newTileY = ty + dy * tsunamiFactor;

                            Tile newTile = Vars.world.tile(newTileX, newTileY);

                            if (newTile != null) {
                                moves.put(build, new Point2(newTileX, newTileY));
                                affectedBuildings.add(build);
                            }
                        }
                    }
                }
            }
        }

        //removes all the affectedBuildings
        for(Building build : affectedBuildings){
            Tile originalTile = build.tile;
            if(originalTile != null){
                build.remove();
                originalTile.setBlock(Blocks.air, build.team, 0);
                originalTile.build = null;
            }
        }

        //makes the reposition of the buildings if "valid"
        for (Building build : affectedBuildings) {
            Point2 newPos = moves.get(build);
            Tile newTile = Vars.world.tile(newPos.x, newPos.y);

            boolean valid = newTile != null &&
                    newTile.build == null &&
                    Build.validPlace(build.block, build.team, newPos.x, newPos.y, build.rotation, false, false);

            if (valid) {
                newTile.setBlock(build.block, build.team, build.rotation);

                if (newTile.build != null && newTile.build != build) {
                    newTile.build.remove();
                }
                newTile.build = build;
                build.tile = newTile;
                build.set(newTile.worldx() + build.block.offset, newTile.worldy() + build.block.offset);
                build.placed();
                Vars.renderer.blocks.updateShadow(build);
            } else {
                build.kill();
            }
        }

        Events.fire(new EventType.TsunamiEvent(startTile.worldx(), startTile.worldy(), angle, tsunamiFactor*10 * Vars.tilesize));
    }
}


