package org.polyfrost.example.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * An example Mixin using SpongePowered's Mixin library
 *
 * @see Mixin
 * @see Inject
 */
@Mixin(Minecraft.class)
// Class naming convention: Mixin_[OriginalClassName]_[SimplifiedDescription]
public class Mixin_Minecraft_Example {

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void examplemod$onStartGame(CallbackInfo ci) {
        System.out.println("This is a message from the example mod!");
    }

}
