package org.polyfrost.example.client

import net.minecraft.client.Minecraft
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.hud.v1.TextHud

class ExampleHud : TextHud(
    id = "example_hud.json",
    title = "Example HUD",
    category = Category.PLAYER,
    prefix = "Player: "
) {

    @Text(title = "Fallback Text", description = "Shown when you aren't in a world.")
    var fallbackText = "None"

    override fun getText(): String {
        val name = Minecraft.getInstance().player?.name
        return name ?: fallbackText
    }

    // Hides the HUD while the mod is switched off from its card
    override fun shouldShow(): Boolean = ExampleConfig.enabled

    override fun defaultPosition(): Pair<Float, Float> = 10f to 10f

}
