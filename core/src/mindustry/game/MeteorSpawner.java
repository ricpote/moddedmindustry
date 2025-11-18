package mindustry.game;


import arc.Events;
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

        float x = Mathf.random(Vars.world.unitWidth() );
        float y = Mathf.random(Vars.world.unitHeight());

        float rad = Mathf.random(20f, 60f);
        float damage = rad * 10f;   //Dano proporcional ao raio

        Fx.meteorFall.at(x,y);

        Time.run(90f, () -> {
            Fx.explosion.at(x, y);
            Damage.damage(x, y, rad, damage);

            Events.fire(new MeteorEvent(x, y, rad, damage));
        });
    }
}
