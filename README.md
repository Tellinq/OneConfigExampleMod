# OneConfigExampleMod

![Powered by OneConfig](https://polyfrost.org/media/branding/badges/badge_1.svg)
![Compact Powered by OneConfig](https://polyfrost.org/media/branding/badges/badge_2.svg)
![Minimal Powered by OneConfig](https://polyfrost.org/media/branding/badges/badge_3.svg)
![Minimal Compact Powered by OneConfig](https://polyfrost.org/media/branding/badges/badge_4.svg)

Example mod implementing OneConfig, written in Kotlin.

This branch builds for multiple Minecraft versions from one codebase using [Stonecutter](https://stonecutter.kikugie.dev/):

| Version                                                 | Loader                   |
|---------------------------------------------------------|--------------------------|
| 1.8.9                                                   | Ornithe (Fabric Loader)  |
| 1.21.1, 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11        | Fabric                   |
| 26.1, 26.2, 26.3                                        | Fabric                   |

## How to use

- Copy the template either by using GitHub's "Use this template" feature or downloading the repo manually.
- **Remove the license named "LICENSE-TEMPLATE" and choose a new one.**
- Refactor the template (specifically, the `mod.*` values in `stonecutter.properties.toml` and most of the class names)
  to a different name.
- Have fun modding! :D

## Working with versions

- `./gradlew build` builds every version, `./gradlew :1.21.11:build` builds just one. Jars end up in `versions/<version>/build/libs`.
- Use the "Set active project" Stonecutter tasks to switch which version your sources are written for. Switch back to
  the `vcsVersion` (26.3) before committing.
- Version-specific code goes in Stonecutter comments, such as `//? if > 1.8.9 {`. See `ExampleHud.kt` for an example.
- To add or remove a version, update `settings.gradle.kts`, `stonecutter.properties.toml` and the CI matrix
  in `.github/workflows/build.yml`.
