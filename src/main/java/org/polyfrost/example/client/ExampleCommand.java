package org.polyfrost.example.client;

import org.polyfrost.example.ExampleConstants;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command;
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler;
import org.polyfrost.oneconfig.utils.v1.dsl.ScreensKt;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleClient.java with `CommandManager.register(new ExampleCommand());`
 * <p>
 * Registers `/examplemod`, which opens the config screen.
 *
 * @see Command
 * @see ExampleClient
 */
@Command(ExampleConstants.ID)
public class ExampleCommand {

    @Handler
    public void main() {
        ScreensKt.openUI(ExampleConfig.INSTANCE);
    }

}
