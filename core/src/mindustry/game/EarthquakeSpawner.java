package mindustry.game;

import arc.Events;
import arc.math.Mathf;
import arc.util.Time;
import arc.struct.Seq;
import arc.math.geom.Point2;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.world.Tile;
import mindustry.world.Build;
import mindustry.content.Blocks;
import mindustry.world.blocks.storage.CoreBlock;


public class EarthquakeSpawner {

    private float timer = 0f;
    private float nextEarthquake = 0f;

    /*
     Saves the buildings affected by the earthquake
    */
    static Seq<Building> affectedBuildings;

    /*
     Saves the positions affected by the earthquake (where builds might go)
    */
    static Seq<Point2> allPotentialPositions;

    /*
     Saves the tiles in the affected by the earthquake before the earthquake
    */
    static Seq<Tile> originalPrimaryTiles;

    public EarthquakeSpawner(){
        Events.on(EventType.WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });

        affectedBuildings = new Seq<>();
        allPotentialPositions = new Seq<>();
        originalPrimaryTiles = new Seq<>();
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

    private void spawnEarthquake(){
        if(!Vars.state.isPlaying()){return;}

        float x, y;
        int centerTileX,centerTileY;
        int attempts = 0;
        Tile centerTile;
        do{
            x = Mathf.random(Vars.world.unitWidth());
            y = Mathf.random(Vars.world.unitHeight());

            centerTileX = (int)(x / Vars.tilesize);
            centerTileY = (int)(y / Vars.tilesize);

            centerTile = Vars.world.tile(centerTileX, centerTileY);

            attempts++;
            //tries a max of 1000 times to find a not Darkened place on the map for the earthquake
            if(attempts == 1000) return;
        }while(centerTile.isDarkened());

        float rad = Mathf.random(60f, 120f);
        Vars.renderer.shake(rad*5, 60f);

        affectedBuildings.clear();
        allPotentialPositions.clear();
        originalPrimaryTiles.clear();

        replaceCalculationAndAnimation(rad, centerTileX, centerTileY);
        allPotentialPositions.shuffle();
        removeAffectedBuildings();
        replaceAffectedBuildings();

        Events.fire(new EventType.EarthquakeEvent(x, y, rad));
    }

    /**
     * This method is responsible to collect all the buildings and tiles, and determines all the new potential
     * positions for the buildings, in the range affected by the earthquake. Also calls the FX class to draw the
     * animation on the affected tiles.
     * @param rad: the radius of the earthquake.
     * @param centerTileX: x coordinate of the center of the earthquake.
     * @param centerTileY: y coordinate of the center of the earthquake.
     * @pre: rad != null && rad >= 0 && centerTileX != null && centerTileY != null
     */
    private void replaceCalculationAndAnimation(float rad, int centerTileX, int centerTileY){
        int tileRad = (int)(rad / Vars.tilesize);
        int animation = 0;
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
                    if(animation%2 == 0){
                        for(int i = 0; i < 4; i++) //more intensity on the image
                            Fx.earthquake.at(tile.worldx(), tile.worldy());
                    }else
                        Fx.earthquake2.at(tile.worldx(),tile.worldy());

                    animation++;
                }
            }
        }
    }

    /**
     * This method removes the affected buildings from where they are on the map to be replaced somewhere else
     */
    private void removeAffectedBuildings(){
        //removes all the core blocks ensuring that they are not affected (if not would lead to game over)
        affectedBuildings.removeAll(build -> build.block instanceof CoreBlock);
        originalPrimaryTiles.removeAll(build -> build.block() instanceof CoreBlock);
        for(Tile originalTile : originalPrimaryTiles){
            Building build = originalTile.build;
            if(build != null){
                build.remove();
                originalTile.setBlock(Blocks.air, build.team, 0);
                originalTile.build = null;
            }
        }
    }


    /**
     * This method reallocates the buildings removed (affectedBuildings) to the new assign position on the map
     * (specifically in the range of the earthquake)
     */
    private void replaceAffectedBuildings(){
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
                //makes the reposition of the buildings if "valid", if not destroys the building.
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
    }


}
