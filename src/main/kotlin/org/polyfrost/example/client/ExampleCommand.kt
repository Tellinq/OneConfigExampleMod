package org.polyfrost.example.client

import org.polyfrost.example.ExampleConstants
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command
import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Handler
import org.polyfrost.oneconfig.utils.v1.dsl.openUI

// Registers `/examplemod`, which opens the config screen.
@Command(ExampleConstants.ID)
object ExampleCommand {

    @Handler
    fun main() {
        ExampleConfig.openUI()
    }

}
