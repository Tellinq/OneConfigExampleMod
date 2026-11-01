package org.polyfrost.example

import net.fabricmc.api.ClientModInitializer
import org.polyfrost.example.client.ExampleClient

// Fabric Loader is used on every version (Ornithe on 1.8.9), so a single client entrypoint covers them all.
class ExampleEntrypoint : ClientModInitializer {

    override fun onInitializeClient() {
        ExampleClient.initialize()
    }

}
