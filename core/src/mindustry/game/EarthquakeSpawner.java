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




public class EarthquakeSpawner {

    private float timer = 0f;
    private float nextEarthquake = 0f;

    public EarthquakeSpawner(){
        Events.on(EventType.WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });
    }


    public void update(){
        if(!Vars.state.isPlaying()) return;

        timer += Time.delta;
        if(timer >= nextEarthquake){
            spawnEarthquake();
            timer = 0f;
        }
    }

    private void scheduleNext(){
        nextEarthquake = Mathf.random(3000f, 9000f);
    }


    private static void spawnEarthquake(){
        if(!Vars.state.isPlaying()){return;}

        float x, y;

        x = Mathf.random(Vars.world.unitWidth());
        y = Mathf.random(Vars.world.unitHeight());

        int centerTileX = (int)(x / Vars.tilesize);
        int centerTileY = (int)(y / Vars.tilesize);

        float rad = Mathf.random(60f, 120f);
        float damage = 0;  //still up to changes

        Vars.renderer.shake(rad*10, 60f);
        Fx.shockwave.at(x, y, 0, rad / 150f); //still up to changes
        Damage.damage(x,y,rad,damage);


        Seq<Building> affectedBuildings = new Seq<>();
        Seq<Point2> allPotentialPositions = new Seq<>();
        Seq<Tile> originalPrimaryTiles = new Seq<>();

        int tileRad = (int)(rad / Vars.tilesize);

        for (int tx = centerTileX - tileRad; tx <= centerTileX + tileRad; tx++) {
            for (int ty = centerTileY - tileRad; ty <= centerTileY + tileRad; ty++) {
                Tile tile = Vars.world.tile(tx, ty);

                if (tile != null && Mathf.dst2(tx, ty, centerTileX, centerTileY) <= tileRad * tileRad) {
                    Building build = tile.build;
                    if (build != null && build.team.active()) {

                        if (tile.x == build.tileX() && tile.y == build.tileY() && !affectedBuildings.contains(build)) {
                            affectedBuildings.add(build);
                            originalPrimaryTiles.add(tile);
                        }
                    }
                    allPotentialPositions.add(new Point2(tx, ty));
                }
            }
        }

        allPotentialPositions.shuffle();

        for(Tile originalTile : originalPrimaryTiles){
            Building build = originalTile.build;
            if(build != null){
                build.remove();
                originalTile.setBlock(Blocks.air, build.team, 0);
                originalTile.build = null;
            }
        }

        for (Building build: affectedBuildings) {
            boolean placed = false;

            for (int j = 0; j < allPotentialPositions.size; j++) {
                Point2 newPos = allPotentialPositions.get(j);
                Tile newTile = Vars.world.tile(newPos.x, newPos.y);

                boolean valid = newTile != null && Build.validPlace(
                        build.block,
                        build.team,
                        newPos.x, newPos.y,
                        build.rotation,
                        false,
                        false
                );

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
                    allPotentialPositions.remove(j);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                build.kill();
            }
        }

        Events.fire(new EventType.EarthquakeEvent(x, y, rad, damage));
    }
}
