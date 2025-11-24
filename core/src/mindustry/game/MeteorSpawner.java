package mindustry.game;


import arc.Events;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import arc.math.*;
import mindustry.content.Fx;
import mindustry.game.EventType.*;
import mindustry.Vars;
import mindustry.entities.*;

public class MeteorSpawner {

    private float timer = 0f;
    private float nextMeteor = 0f;

    public MeteorSpawner(){
        Events.on(WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });
    }

    public void update(){
        if(!Vars.state.isPlaying()) return;

        timer += Time.delta;
        if(timer >= nextMeteor){
            spawnMeteor();
            timer = 0f;
        }
    }

    private void scheduleNext(){
        nextMeteor = Mathf.random(3000f, 9000f);
    }


    private static void spawnMeteor(){
        if(!Vars.state.isPlaying()){return;}

        int count = Mathf.random(4, 7);
        Seq<Vec2> valid = generateValidPositions();

        for(int i = 0; i < count; i++){
            Vec2 pos = valid.pop();
            float x = pos.x;
            float y = pos.y;

            float rad = Mathf.random(20f, 60f);
            float damage = rad * 10f;

            Fx.meteorFall.at(x,y);

            Time.run(90f, () -> {
                Fx.explosion.at(x, y);
                Damage.damage(x, y, rad, damage);

                Events.fire(new MeteorEvent(x, y, rad, damage));
            });
        }
    }

    private static Seq<Vec2> generateValidPositions(){
        Seq<Vec2> list = new Seq<>();

        float safeRadius = 80f;

        for(int i = 0; i < 100; i++){
            float x = Mathf.random(Vars.world.unitWidth());
            float y = Mathf.random(Vars.world.unitHeight());

            if(!isNearCore(x, y, safeRadius)){
                list.add(new Vec2(x, y));
            }
        }

        return list;
    }

    private static boolean isNearCore(float x, float y, float safe){
        for(var core : Vars.state.teams.playerCores()){
            if(core == null) continue;

            if(Mathf.within(x, y, core.x, core.y, safe)){
                return true;
            }
        }
        return false;
    }
}
