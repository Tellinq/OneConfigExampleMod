package org.polyfrost.example.client;

import kotlin.Pair;
import net.minecraft.client.Minecraft;
import org.polyfrost.oneconfig.api.config.v1.annotations.Text;
import org.polyfrost.oneconfig.api.hud.v1.TextHud;

/**
 * An example HUD that shows the player's name.
 * Registered in ExampleClient.java with `HudManager.register(new ExampleHud(), ExampleConstants.ID);`
 *
 * @see TextHud
 * @see ExampleClient
 */
public class ExampleHud extends TextHud {

    @Text(title = "Fallback Text", description = "Shown when you aren't in a world.")
    public String fallbackText = "None";

    public ExampleHud() {
        super("example_hud.json", "Example HUD", Category.getPLAYER(), "Player: ", "");
    }

    @Override
    protected String getText() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return fallbackText;
        }

        return mc.player.getName().getString();
    }

    // Hides the HUD while the mod is switched off from its card
    @Override
    public boolean shouldShow() {
        return ExampleConfig.enabled;
    }

    @Override
    public Pair<Float, Float> defaultPosition() {
        return new Pair<>(10f, 10f);
    }

}
