package org.polyfrost.example;

import net.fabricmc.api.ClientModInitializer;
import org.polyfrost.example.client.ExampleClient;

public class ExampleEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ExampleClient.initialize();
    }

}
