package mindustry.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.gen.*;

public class HealthBarRenderer {
    public static final float BAR_WIDTH = 30f;
    public static final float BAR_HEIGHT = 4f;
    private static final float OFFSET_Y = 5f;

    private static final Color COLOR_BACKGROUND = Color.darkGray;
    private static final Color COLOR_ALLY = Color.cyan;
    private static final Color COLOR_ENEMY_HIGH = Color.green;
    private static final Color COLOR_ENEMY_MID = Color.yellow;
    private static final Color COLOR_ENEMY_LOW = Color.red;

    private static final float LINE_THICKNESS = 1f;

    public static void drawHealthBars(){
        Lines.beginLine();
        Lines.stroke(LINE_THICKNESS);

        drawUnitHealthBar();

        Lines.endLine();
    }

    private static void drawUnitHealthBar(){
        Groups.unit.each(unit -> {
            if(unit.dead() || Mathf.equal(unit.health(), unit.maxHealth())){return;}
            if(!Core.camera.bounds(Tmp.r1).contains(unit.x(), unit.y())){return;}

            float worldX = unit.x();
            float worldY = unit.y() + unit.hitSize() / 2f + OFFSET_Y;

            //% of the bar filled by current health / max health
            float healthFrac = unit.health() / unit.maxHealth();
            float fillWidth = BAR_WIDTH * healthFrac;

            //sets the color of the bar (cyan - teammate; red, yellow, green - enemies)
            Color HealthColor;
            if(unit.team().id == Vars.player.team().id){
                HealthColor = COLOR_ALLY;
            } else {
                if(healthFrac < 0.33f){ HealthColor = COLOR_ENEMY_LOW;}
                else if(healthFrac < 0.66f){ HealthColor = COLOR_ENEMY_MID;}
                else { HealthColor = COLOR_ENEMY_HIGH;}
            }

            float startX = worldX - BAR_WIDTH / 2f;
            float startY = worldY - BAR_HEIGHT/ 2f;

            //dynamic center of the bar, calculated so the center changes as the hp change to keep the filling inside the bar
            float fillX = startX + fillWidth / 2f;

            //background
            Draw.color(COLOR_BACKGROUND);
            //x and y are the bottom left coordinates
            Lines.rect(startX, startY, BAR_WIDTH, BAR_HEIGHT);

            //filling
            Draw.color(HealthColor);
            Fill.rect(fillX, worldY, fillWidth - 2 * LINE_THICKNESS, BAR_HEIGHT - 2 * LINE_THICKNESS);
        });
    }
}






























