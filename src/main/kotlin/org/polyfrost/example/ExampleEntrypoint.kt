package org.polyfrost.example

import net.fabricmc.api.ClientModInitializer
import org.polyfrost.example.client.ExampleClient

class ExampleEntrypoint : ClientModInitializer {

    override fun onInitializeClient() {
        ExampleClient.initialize()
    }

}
