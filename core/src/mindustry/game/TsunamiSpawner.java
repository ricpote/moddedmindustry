package mindustry.game;

import arc.Events;
import arc.math.Mathf;
import arc.util.Time;
import arc.struct.Seq;
import arc.math.geom.Point2;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.gen.Building;
import mindustry.world.Tile;
import mindustry.world.Build;
import mindustry.content.Blocks;
import arc.struct.ObjectMap;

public class TsunamiSpawner {

    private float timer = 0f;
    private float nextTsunami = 0f;

    /*
      Variable to valid the starting tile of the tsunami (used in "validStartTile(Tile tile, int radius)")
    */
    private boolean validStart = true;

    /*
      Variable that decides if it's a water or lava tsunami
    */
    private boolean isWater = true;

    /*
      Contains all the possible directions which the tsunami can go
    */
    private final Point2[] directions = {
            new Point2(0, 1),   // 0º (Up)
            new Point2(-1, 1),  // 45º (Upper Left Diagonal)
            new Point2(-1, 0),  // 90º (Left)
            new Point2(-1, -1), // 135º (Lower Left Diagonal)
            new Point2(0, -1),  // 180º (Down)
            new Point2(1, -1),  // 225º (Lower Right Diagonal)
            new Point2(1, 0),   // 270º (Right)
            new Point2(1, 1)    // 315º (Upper Right Diagonal)
    };

    /*
     Saves the buildings affected by the tsunami
    */
    private Seq<Building> affectedBuildings ;

    /*
     Contains the movement associated to each building (for its reallocation)
    */
    private ObjectMap<Building, Point2> moves;

    public TsunamiSpawner() {
        Events.on(EventType.WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });

        moves = new ObjectMap<>();
        affectedBuildings = new Seq<>();
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
        nextTsunami = Mathf.random(900f, 2700f);
    }

    private void spawnTsunami() {
        if (!Vars.state.isPlaying()) {
            return;
        }

        float x, y;
        int startTileX, startTileY;
        Tile startTile;

        int tsunamiFactor = Mathf.random(4, 6);
        int directionIndex = Mathf.random(0, 7);
        Point2 direction = directions[directionIndex];
        float angle = directionIndex*45;
        int dx = direction.x;
        int dy = direction.y;

        int attempts = 0;
        do {
            x = Mathf.random(Vars.world.unitWidth());
            y = Mathf.random(Vars.world.unitHeight());

            startTileX = (int) (x / Vars.tilesize);
            startTileY = (int) (y / Vars.tilesize);

            startTile = Vars.world.tile(startTileX, startTileY);

            attempts++;
            //tries a max of 1000 times to find a valid place for the tsunami to start
            if(attempts == 1000) return;
        } while (!validStartTile(startTile, tsunamiFactor));

        moves.clear();
        affectedBuildings.clear();

        calculateAffectedBuildings(startTileX, startTileY, angle, tsunamiFactor, dx, dy);
        animation(startTileX, startTileY, angle, tsunamiFactor);
        removeAffectedBuildings();
        replaceAffectedBuildings();

        Events.fire(new EventType.TsunamiEvent(startTile.worldx(), startTile.worldy(), angle, tsunamiFactor*10 * Vars.tilesize));
    }

    /**
     * This method verifies if the generated starting tile is valid for the start of the tsunami.
     * It is if all the surrounding tiles in range of "radius" verify these conditions
     * @param tile: starting tile.
     * @param radius: range of tiles to verify.
     * @pre: radius != null
     */
    private boolean validStartTile(Tile tile, int radius) {
        validStart = true;
        if(tile == null || tile.isDarkened() || !tile.floor().isLiquid)
            return false;
        tile.circle(radius, neighborTile ->{
            if (neighborTile == null || neighborTile.isDarkened() || !neighborTile.floor().isLiquid)
                validStart = false;
        });

        //verifies if it's a lava or water tsunami
        isWater = tile.floor().liquidDrop != Liquids.slag;

        return validStart;
    }

    /**
     * This method it's responsible to go in the affected area of the tsunami (in all possible directions) and save the
     * buildings that will be replaced and their new position as well.
     * @param waveX: x coordinate where the tsunami starts.
     * @param waveY: y coordinate where the tsunami starts.
     * @param angle: andle that defines the direction of the tsunami.
     * @param tsunamiFactor: the scale of the tsunami.
     * @param dx: the x coordinate the direction (of Point2[] directions).
     * @param dy: the y coordinate the direction (of Point2[] directions).
     * @pre: waveX != null && waveY != null && angle != null && tsunamiFactor != null &&  dx != null && dy != null
     */
    private void calculateAffectedBuildings(int waveX, int waveY, float angle, int tsunamiFactor, int dx, int dy) {
        int angleFactor = 0;
        if (angle == 225 || angle == 270 || angle == 315) {
            for (int tx = waveX; tx <= waveX + tsunamiFactor * 3; tx++) {
                if (angle == 315) angleFactor += 1;
                else if (angle == 225) angleFactor -= 1;
                for (int ty = waveY - tsunamiFactor + angleFactor; ty <= waveY + tsunamiFactor + angleFactor; ty++) {
                    selectAffectedBuildsAndMoves(Vars.world.tile(tx, ty), tx, ty, dx, dy, tsunamiFactor);
                }
            }
        } else if (angle == 45 || angle == 90 || angle == 135) { // Onda indo para a Esquerda (X diminui)
            for (int tx = waveX; tx >= waveX - tsunamiFactor * 3; tx--) {
                if (angle == 45) angleFactor += 1;
                else if (angle == 135) angleFactor -= 1;
                for (int ty = waveY - tsunamiFactor + angleFactor; ty <= waveY + tsunamiFactor + angleFactor; ty++) {
                    selectAffectedBuildsAndMoves(Vars.world.tile(tx, ty), tx, ty, dx, dy, tsunamiFactor);
                }
            }
        } else if (angle == 0) {
            for (int tx = waveX - tsunamiFactor; tx <= waveX + tsunamiFactor; tx++) {
                for (int ty = waveY; ty <= waveY + tsunamiFactor * 3; ty++) {
                    selectAffectedBuildsAndMoves(Vars.world.tile(tx, ty), tx, ty, dx, dy, tsunamiFactor);
                }
            }
        } else if (angle == 180) {
            for (int tx = waveX - tsunamiFactor; tx <= waveX + tsunamiFactor; tx++) {
                for (int ty = waveY; ty >= waveY - tsunamiFactor * 3; ty--) {
                    selectAffectedBuildsAndMoves(Vars.world.tile(tx, ty), tx, ty, dx, dy, tsunamiFactor);
                }
            }
        }
    }

    /**
     * This method is the on that actually saves int the map "moves" and seq
     * "affectedBuildings" the buildings and their new positions
     * @param tsunamiFactor: the scale of the tsunami.
     * @param tx: x coordinate of the current tile of the search.
     * @param ty: y coordinate of the current tile of the search.
     * @param dx: the x coordinate the direction (of Point2[] directions).
     * @param dy: the y coordinate the direction (of Point2[] directions).
     * @pre: tsunamiFactor != null &&  tx != null && ty != null &&  dx != null && dy != null
     */
    private void selectAffectedBuildsAndMoves(Tile targetTile, int tx, int ty, int dx, int dy, int tsunamiFactor) {
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

    /**
     * This method is responsible for the animation (calls Fx) of the tsunami on every tile affected by it.
     * @param waveX: x coordinate where the tsunami starts.
     * @param waveY: y coordinate where the tsunami starts.
     * @param angle: andle that defines the direction of the tsunami.
     * @param tsunamiFactor: the scale of the tsunami.
     * @pre: waveX != null && waveY != null && angle != null && tsunamiFactor != null
     */
    private void animation(int waveX, int waveY, float angle, int tsunamiFactor){
        int angleFactor = 0;
        if (angle == 225 || angle == 270 || angle == 315) {
            for (int tx = waveX; tx <= waveX + tsunamiFactor * 3; tx++) {
                if (angle == 315) angleFactor += 1;
                else if (angle == 225) angleFactor -= 1;

                int finalTx = tx;
                int finalAngleFactor = angleFactor;
                float delayFrames = (finalTx - waveX) * 0.04f * 60f;
                Time.run(delayFrames, () -> {
                    for (int ty = waveY - tsunamiFactor + finalAngleFactor; ty <= waveY + tsunamiFactor + finalAngleFactor; ty++) {
                        Tile targetTile = Vars.world.tile(finalTx, ty);
                        if (targetTile != null && finalAngleFactor == 0){
                            if(isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                        }else if (targetTile != null && finalAngleFactor > 0) {
                            if (isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 10);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 10);
                        } else if (targetTile != null){//&&finalAngleFactor < 0
                            if(isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 30);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 30);
                        }

                    }
                });
            }
        } else if (angle == 45 || angle == 90 || angle == 135) {
            for (int tx = waveX; tx >= waveX - tsunamiFactor * 3; tx--) {
                if (angle == 45) angleFactor += 1;
                else if (angle == 135) angleFactor -= 1;

                int finalTx = tx;
                int finalAngleFactor = angleFactor;
                float delayFrames = (waveX - finalTx) * 0.04f * 60f;
                Time.run(delayFrames, () -> {
                    for (int ty = waveY - tsunamiFactor + finalAngleFactor; ty <= waveY + tsunamiFactor + finalAngleFactor; ty++) {
                        Tile targetTile = Vars.world.tile(finalTx, ty);
                        if (targetTile != null && finalAngleFactor == 0){
                            if(isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                        }else if (targetTile != null && finalAngleFactor > 0) {
                            if (isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 10);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 10);
                        } else if (targetTile != null){//&&finalAngleFactor < 0
                            if(isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 30);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle * 30);
                        }
                    }
                });
            }
        } else if (angle == 0) {
            for (int ty = waveY; ty <= waveY + tsunamiFactor * 3; ty++) {
                int finalTy = ty;
                float delayFrames = (finalTy - waveY) * 0.04f * 60f;
                Time.run(delayFrames, () -> {
                    for (int tx = waveX - tsunamiFactor; tx <= waveX + tsunamiFactor; tx++) {
                        Tile targetTile = Vars.world.tile(tx, finalTy);
                        if (targetTile != null){
                            if(isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                        }
                    }
                });
            }
        } else if (angle == 180) {
            for (int ty = waveY; ty >= waveY - tsunamiFactor * 3; ty--) {
                int finalTy = ty;
                float delayFrames = (waveY - finalTy) * 0.04f * 60f;
                Time.run(delayFrames, () -> {
                    for (int tx = waveX - tsunamiFactor; tx <= waveX + tsunamiFactor; tx++) {
                        Tile targetTile = Vars.world.tile(tx, finalTy);
                        if (targetTile != null){
                            if(isWater) Fx.waterTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                            else Fx.lavaTsunami.at(targetTile.worldx(), targetTile.worldy(), angle);
                        }
                    }
                });
            }
        }

    }

    /**
     * This method removes the affected buildings from where they are on the map to be replaced on the new position
     */
    private void removeAffectedBuildings(){
        removeCoreFromAffectedBuildings();
        for(Building build : affectedBuildings){
            Tile originalTile = build.tile;
            if(originalTile != null){
                build.remove();
                originalTile.setBlock(Blocks.air, build.team, 0);
                originalTile.build = null;
            }
        }
    }

    /**
     * This method removes all the core blocks from "affectedBuildings" and "moves"
     * ensuring that they are not affected (if not would lead to game over)
     */
    private void removeCoreFromAffectedBuildings(){
        affectedBuildings.removeAll(build -> build.block instanceof mindustry.world.blocks.storage.CoreBlock);
        var iterator = moves.iterator();
        while(iterator.hasNext()){
            var entry = iterator.next();
            if(entry.key.block instanceof mindustry.world.blocks.storage.CoreBlock){
                iterator.remove();
            }
        }
    }

    /**
     * This method reallocates the buildings removed (affectedBuildings) to the new assign position on the map
     */
    private void replaceAffectedBuildings(){
        for (Building build : affectedBuildings) {
            Point2 newPos = moves.get(build);
            Tile newTile = Vars.world.tile(newPos.x, newPos.y);

            boolean valid = newTile != null &&
                    newTile.build == null &&
                    Build.validPlace(build.block, build.team, newPos.x, newPos.y, build.rotation, false, false);
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
            } else {
                build.kill();
            }
        }
    }


}


