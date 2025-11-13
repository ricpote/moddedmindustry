package mindustry.game;

import arc.Events;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;

public class EarthquakeSpawner {

    private float timer = 0f;
    private float nextMeteor = 0f;

    public EarthquakeSpawner(){
        Events.on(EventType.WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });
    }


    public void update(){
        if(!Vars.state.isPlaying()) return;

        timer += Time.delta;
        if(timer >= nextMeteor){
            spawnEarthquake();
            timer = 0f;
        }
    }

    private void scheduleNext(){
        nextMeteor = Mathf.random(3000f, 9000f);
    }


    private static void spawnEarthquake(){
        if(!Vars.state.isPlaying()){return;}

        float x = Mathf.random(Vars.world.unitWidth() );
        float y = Mathf.random(Vars.world.unitHeight());

        float rad = Mathf.random(20f, 60f);
        float damage = rad;   //still up to changes

        Vars.renderer.shake(damage * 10f, 3f);
        Fx.explosion.at(x, y); //still up to changes
        Damage.damage(x,y,rad,damage);

        //todo implementation of the earthquake logic (shuffle)
        Events.fire(new EventType.EarthquakeEvent(x, y, rad, damage));
    }
}
