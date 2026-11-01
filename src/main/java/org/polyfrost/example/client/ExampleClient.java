package org.polyfrost.example.client;

import org.polyfrost.example.ExampleConstants;
import org.polyfrost.oneconfig.api.commands.v1.CommandManager;
import org.polyfrost.oneconfig.api.hud.v1.HudManager;

public class ExampleClient {

    public static void initialize() {
        ExampleConfig.INSTANCE.preload();
        CommandManager.register(new ExampleCommand());
        HudManager.register(new ExampleHud(), ExampleConstants.ID);
    }

}
