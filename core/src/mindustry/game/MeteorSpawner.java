package mindustry.game;


import arc.Events;
import mindustry.*;
import arc.math.*;
import mindustry.game.EventType.*;
import mindustry.content.*;

public class MeteorSpawner {


    private static void spawnMeteor(){
        if(!Vars.state.isPlaying()){return;}

        float x = Mathf.random(Vars.world.unitWidth() );
        float y = Mathf.random(Vars.world.unitHeight());

        float rad = Mathf.random(20f, 60f);
        float damage = rad * 10f;   //Dano proporcional ao raio

        Events.fire(new MeteorEvent(x, y, rad, damage));
    }
}
