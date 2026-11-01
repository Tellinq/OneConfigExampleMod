plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3" /* [SC] DO NOT EDIT */

stonecutter {
    tasks {
        order("publishModrinth")
        order("publishCurseforge")
    }

    parameters {
        // Swaps Mojang class names for their 1.8.9 (Feather) equivalents.
        // Add a `replace` here whenever a class you use lives somewhere else on 1.8.9.
        replacements {
            string(eval(current.version, "= 1.8.9")) {
                replace(
                    "com.mojang.blaze3d.platform.InputConstants",
                    "org.polyfrost.oneconfig.internal.legacy.InputConstants"
                )
            }
        }
    }
}
