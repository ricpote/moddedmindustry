package mindustry.ui;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.scene.*;
import mindustry.Vars;
import mindustry.world.blocks.storage.CoreBlock;

public class CoreHealthBar extends Element {

    private static final Color COLOR_BACKGROUND = Color.darkGray;
    private static final Color COLOR_DEFAULT = Color.cyan;
    private static final Color COLOR_LOW_HEALTH = Color.red;
    private static final Color COLOR_TEXT = Color.white;
    private static final float THICKNESS = 4f;

    public float barWidth = 250f;
    public float barHeight = 25f;

    {
        setSize(barWidth, barHeight);
    }

    public void draw(){
        if(!Vars.state.rules.waves || Vars.state.wave <= 1) return;

        CoreBlock.CoreBuild core = Vars.player.team().core();
        if(core == null || core.dead()) return;

        float healthFrac = core.health() / core.maxHealth();
        float fillWidth = barWidth * healthFrac;

        float fillX = x + fillWidth / 2f;
        float fillY = y + barHeight / 2f;

        Color healthColor = healthFrac > 0.33 ? COLOR_DEFAULT : COLOR_LOW_HEALTH;

        Draw.color(COLOR_BACKGROUND);
        Lines.stroke(THICKNESS);
        Lines.rect(x, y, barWidth, barHeight);

        Draw.color(healthColor);
        Fill.rect(fillX, fillY, Math.max(0f, fillWidth - 2 * THICKNESS), barHeight - 2 * THICKNESS);

        Draw.color(COLOR_TEXT);
        Fonts.outline.draw("Core Health", x, y + height + 25f);


        Draw.reset();
    }
}
