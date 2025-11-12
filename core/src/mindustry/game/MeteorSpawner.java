package mindustry.game;


import arc.Events;
import arc.util.Time;
import mindustry.*;
import arc.math.*;
import mindustry.game.EventType.*;

public class MeteorSpawner {
    private float timer = 0f;

    public void update(){
        timer += Time.delta;
        if(timer > Mathf.random(3000f, 9000f)){
            spawnMeteor();
            timer = 0;
        }
    }


    private static void spawnMeteor(){
        if(!Vars.state.isPlaying()){return;}

        float x = Mathf.random(Vars.world.unitWidth() );
        float y = Mathf.random(Vars.world.unitHeight());

        float rad = Mathf.random(20f, 60f);
        float damage = rad * 10f;   //Dano proporcional ao raio

        Events.fire(new MeteorEvent(x, y, rad, damage));
    }
}
