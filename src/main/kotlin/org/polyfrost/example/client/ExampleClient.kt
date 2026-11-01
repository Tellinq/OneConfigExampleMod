package org.polyfrost.example.client

import org.polyfrost.example.ExampleConstants
import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.oneconfig.api.hud.v1.HudManager

object ExampleClient {

    fun initialize() {
        ExampleConfig.preload()
        CommandManager.register(ExampleCommand)
        HudManager.register(ExampleHud(), ExampleConstants.ID)
    }

}
