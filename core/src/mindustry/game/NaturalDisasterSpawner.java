package mindustry.game;

import arc.Events;
import arc.util.Time;
import mindustry.Vars;

public abstract class NaturalDisasterSpawner {
    protected float timer = 0f;
    protected float nextEvent = 0f;

    public NaturalDisasterSpawner(){
        Events.on(EventType.WorldLoadEvent.class, e -> {
            timer = 0f;
            scheduleNext();
        });
    }

    public void update(){
        if(!Vars.state.isPlaying()) return;

        timer += Time.delta;
        if(timer >= nextEvent){
            triggerEvent();
            timer = 0f;
        }
    }

    protected abstract void scheduleNext();
    protected abstract void triggerEvent();
}
