<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Wavefront OBJ IntelliJ Plugin Changelog

## [Unreleased]
### Added
- Support for platform version `221.*`
- Color preview markers in MTL files
- Add city square (night) environment for physically based shading method in 3D preview

### Changed
- Line markers made configurable
- PBR shader calculations in world space
- Use pre-calculated bitangents in PBR shader
- Upgrade Gradle Wrapper to `7.4.1`
- Dependencies:
  - Upgrade `org.jetbrains.intellij` to `1.4.0`
  - Upgrade `com.google.devtools.ksp` to `1.6.10-1.0.4`
  - Upgrade `glimpse-core` to `1.1.0-ALPHA3`
  - Upgrade `glimpse-ui` to `1.1.0-ALPHA3`
  - Upgrade `glimpse-processor-ksp` to `1.1.0-ALPHA3`
- GitHub Actions:
  - Upgrade `actions/checkout` to `v3`
  - Upgrade `actions/upload-artifact` to `v3`
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4, 2021.1.3, 2021.2.4, 2021.3.1, 2022.1`

### Deprecated

### Removed

### Fixed
- Improve PBR shader calculations
- Fix displacement step calculation
- Refresh 3D preview when any of the MTL and texture files referenced by OBJ file changes
- Fix 3D preview issues when loading a project with multiple OBJ files open
- Allow material color components outside of range

### Security
- Sign plugin before publishing

## [1.1.1-alpha.1]
### Added
- Color preview markers in MTL files

### Changed
- Line markers made configurable
- PBR shader calculations in world space
- Dependencies:
  - Upgrade `org.jetbrains.intellij` to `1.3.1`

### Fixed
- Improve PBR shader calculations
- Fix displacement step calculation
- Refresh 3D preview when any of the MTL and texture files referenced by OBJ file changes
- Fix 3D preview issues when loading a project with multiple OBJ files open

### Security
- Sign plugin before publishing

## [1.1.0]
### Added
- Extended MTL syntax for physically based rendering parameters:
  - roughness (`Pr` and `map_Pr`)
  - metalness (`Pm` and `map_Pm`)
  - normal map (`norm`)
  - emission (`Ke` and `map_Ke`)
- Physically based shading method in 3D preview

### Changed
- Upgrade Gradle Wrapper to `7.3.3`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.6.10`
  - Replace `kapt` with `com.google.devtools.ksp` version `1.6.10-1.0.2`
  - Upgrade `org.jetbrains.intellij` to `1.3.0`
  - Upgrade `org.jetbrains.grammarkit` to `2021.2.1`
  - Upgrade `detekt-formatting` to `1.19.0`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.19.0`
  - Upgrade `org.jlleitschuh.gradle.ktlint` to `10.2.1`
  - Upgrade `glimpse-core` to `1.1.0-ALPHA1`
  - Upgrade `glimpse-ui` to `1.1.0-ALPHA1`
  - Replace `glimpse-processor-java` with `glimpse-processor-ksp` version `1.1.0-ALPHA1`
- GitHub Actions:
  - Upgrade `actions/cache` to `v2.1.7`
  - Upgrade `actions/upload-artifact` to `v2.3.1`
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4, 2021.1.3, 2021.2.4, 2021.3.1`

### Fixed
- Prevent texture loading on GL thread

## [1.0.2]
### Added
- Support for platform version `213.*`
- Shader quality (float precision) setting
- Pinch to zoom on 3D preview (if supported on current platform)
- Publish test report in GitHub Actions build
- Annotate sources with detekt issues in GitHub Actions build
- Use Gradle `wrapper` task to handle Gradle updates
- JVM compatibility version extracted to `gradle.properties` file
- Suppress `UnusedProperty` inspection for the `kotlin.stdlib.default.dependency` in `gradle.properties`

### Changed
- Upload detekt report in GitHub Actions build after failure
- Upgrade Gradle Wrapper to `7.2`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.5.31`
  - Upgrade `kapt` to `1.5.31`
  - Upgrade `org.jetbrains.intellij` to `1.2.1`
  - Upgrade `org.jetbrains.changelog` to `1.3.1`
  - Upgrade `detekt-formatting` to `1.18.1`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.18.1`
  - Upgrade `org.jlleitschuh.gradle.ktlint` to `10.2.0`
- GitHub Actions:
  - Upgrade `actions/checkout` to `v2.4.0`
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4, 2021.1.3, 2021.2.1`, `2021.3`
- Gradle – Changelog plugin configuration update

### Fixed
- Prevent infinite displacement loop
- Use `DynamicBundle` instead of `AbstractBundle` in `WavefrontObjBundle.kt`
- Replace deprecated `ServiceManager.getService` with `Application.getService`

## [1.0.1]
### Added
- Support for platform version `212.*`

### Changed
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.5.21`
  - Upgrade `kapt` to `1.5.21`
  - Upgrade `org.jetbrains.intellij` to `1.1.3`
  - Upgrade `org.jetbrains.changelog` to `1.2.0`
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4`, `2021.1.3`, `2021.2`

## [1.0.0]
### Added
- Support for textured lines
- Support for multiple MTL filenames in `mtllib` statement
- Support for displacement textures in 3D preview
- Cropping textures toggle action in 3D preview
- Showing axes labels in 3D preview
- Setting for cropping textures
- Setting for displacement quality
- Setting for showing axes labels
- Setting for axes labels font size

### Changed
- Group 3D preview editor color settings
- Reorganise plugin settings
- Smoothing group number can be any positive integer
- Use `gradle-grammarkit-plugin` to generate parsers and lexers
- Remove reference to the `jcenter()` from Gradle configuration file
- Upgrade Gradle Wrapper to `7.1`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.5.20`
  - Upgrade `kapt` to `1.5.20`
  - Upgrade `org.jetbrains.intellij` to `1.1.2`
  - Upgrade `detekt-formatting` to `1.17.1`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.17.1`
- GitHub Actions:
  - Upgrade `gradle/wrapper-validation-action` to `v1.0.4`
  - Upgrade `actions/cache` to `v2.1.6`
  - Upgrade `actions/checkout` to `v2.3.4`
  - Upgrade `actions/upload-artifact` to `v2.2.4`
  - Upgrade `actions/upload-release-asset` to `v1.0.2`
  - Upgrade `actions/create-release` to `v1.1.4`
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4`, `2021.1.3`
- Trigger GitHub Actions `Build` workflows only on pushes to `main` branch or pull request to avoid duplicated checks

### Fixed
- Prevent IDE from freezing after a big change to an OBJ file
- Display correct descriptions of OBJ and MTL token types
- Improve UI messages
- Require spaces after keywords, options and values

## [0.3.2]
### Added
- Support for relative indices in OBJ files (#95)
- Refresh action in 3D preview
- Setting for default shading method in 3D preview

### Changed
- Use standard IDE error icons
- Display 3D preview errors below the preview (#112)

## [0.3.1]
### Added
- Support for platform version `211.*`

### Changed
- Dependencies:
  - Upgrade `kapt` to `1.4.32`
- GitHub Actions:
  - Upgrade `actions/upload-artifact` to `v2.2.3`
  - Upgrade `actions/setup-java` to `v2`
- Update platform version to `2020.3`
- `pluginVerifierIdeVersions` – upgrade to `2020.3.3`, `2021.1`
- Update Java version to `11`

### Removed
- Remove support for platform version `201.*`
- Remove support for platform version `202.*`

### Fixed
- Update signature of overridden method `RelatedItemLineMarkerProvider.collectNavigationMarkers(...)`
- Replace deprecated method `FormattingModelBuilder.createModel(...)`
- Replace deprecated extension function `max()` with `maxOrNull()`
- Replace deprecated method `PsiManager.addPsiTreeChangeListener(...)`
- Run `FilenameIndex.getFilesByName(...)` inside `runReadAction { ... }`

## [0.3.0]
### Added
- Wireframe shading in the 3D preview
- Material shading in the 3D preview (material colors)
- Build configuration:
  - `properties` shorthand function for accessing `gradle.properties` in a cleaner way
  - Dependabot check for GitHub Actions used in workflow files

### Changed
- Upgrade Gradle Wrapper to `6.8.3`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.4.32`
  - Upgrade `org.jetbrains.intellij` to `0.7.2`
  - Upgrade `org.jetbrains.changelog` to `1.1.2`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.16.0`
  - Upgrade `detekt-formatting` to `1.16.0`
  - Upgrade `org.jlleitschuh.gradle.ktlint` to `10.0.0`
- GitHub Actions:
  - Upgrade `actions/upload-artifact` to `v2.2.2`
  - Upgrade `actions/cache` to `v2.1.4`
- `pluginVerifierIdeVersions` – upgrade to `2020.1.4`, `2020.2.4`, `2020.3.3`
- Update `changelog` Gradle plugin configuration
- Migrate to GLES 2.0 with [Glimpse](https://glimpse.graphics/)

### Fixed
- Provide list of available texture files to texture file reference
- Fix `README.md` file resolution in the `build.gradle.kts`

## [0.2.1]
### Added
- Color settings for OBJ 3D preview colors for faces, lines and points.
- Settings for the 3D preview:
  - Default up vector
  - Line width
  - Point size
- Showing axes and grid on the 3D preview (both toggleable)
- Zoom actions on 3D preview toolbar
- Settings action on 3D preview toolbar

### Changed
- Upgrade Gradle Wrapper to `6.8.1`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.4.30`
- `pluginVerifierIdeVersions` – upgrade to `2020.1.4`, `2020.2.4`, `2020.3.2`

### Fixed
- Separate color settings attribute key for OBJ and MTL constants
- JogAmp bug on macOS causing IDE crash when showing 3D preview (fixed by using JogAmp snapshot dependencies v2.4)
- Limit initial camera distance for very small objects
- Refresh the 3D preview after the OBJ file edited
- Use relative paths in MTL file references

## [0.2.0]
### Added
- Support for drawing lines in 3D preview
- Support for drawing points in 3D preview
- Support for MTL files:
  - Syntax highlighting
  - Structure tree view
  - Code formatting
  - Code commenting
- Navigation between OBJ and MTL files:
  - Code completion
  - References
  - Usages
  - Renaming
  - Quick fixes

### Fixed
- Replace usages deprecated in platform version 2020.3 (if replacement available)
- Fix errors after typing long keywords

## [0.1.5]
### Added
- Dependabot integration
- Show `idea.log` logs of the run IDE in the Run console

### Changed
- `build.gradle.kts`: simpler syntax for configuring `KotlinCompile`
- Upgrade Gradle Wrapper to `6.8`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.4.21-2`
  - Upgrade `detekt-formatting` to `1.15.0`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.15.0`
  - Upgrade `org.jetbrains.changelog` to `1.0.1`
- `pluginVerifierIdeVersions` – upgrade to `2020.1.4`, `2020.2.3`, `2020.3.1`

### Fixed
- Allow `float` scientific notation with upper case `E`
- Return `Supplier<@Nls String>` instead of `String` in `MyBundle.messagePointer`
- GitHub Actions: Use the correct property in the "Upload artifact" step

## [0.1.4]
### Added
- Predefined Run/Debug Configurations
- Support for platform version 203.*

### Changed
- Dependencies:
  - Upgrade `org.jetbrains.intellij` to `0.6.5`
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.4.20`

## [0.1.3]
### Added
- Setting: Disable 3D preview
- Setting: Default layout

### Changed
- Update `pluginVerifierIdeVersions` in the `gradle.properties` file
- Dependencies:
  - Upgrade `org.jetbrains.intellij` to `0.6.2`

### Fixed
- Not creating `GLPanel` before 3D preview set to visible

## [0.1.2]
### Added
- Fix default to opt-out of bundling Kotlin standard library in plugin distribution
- Integration with [IntelliJ Plugin Verifier](https://github.com/JetBrains/intellij-plugin-verifier) through the [Gradle IntelliJ Plugin](https://github.com/JetBrains/gradle-intellij-plugin#plugin-verifier-dsl) `runPluginVerifier` task

### Changed
- Set default editor layout to text only
- Update platform version to `2020.1`
- Upgrade Gradle Wrapper to `6.7`
- Dependencies:
  - Upgrade `org.jetbrains.kotlin.jvm` to `1.4.10`
  - Upgrade `org.jetbrains.intellij` to `0.6.1`
  - Upgrade `org.jetbrains.changelog` to `0.6.2`
  - Upgrade `io.gitlab.arturbosch.detekt` to `1.14.2`
  - Upgrade `org.jlleitschuh.gradle.ktlint` to `9.4.1`

### Removed
- Remove support for platform version `2019.3`
- Remove Third-party IntelliJ Plugin Verifier GitHub Action

### Fixed
- Handling exceptions in 3D preview
- Trying to load different GL profiles (#38)
- Allowing for integer values of coordinates in OBJ files (#39)
- `pluginName` variable name collision with `intellij` closure getter in Gradle configuration
- Using correct encoding of ellipsis character when initializing 3D preview

## [0.1.1]
### Fixed
- Correct link in plugin description

## [0.1.0]
### Added
- Support for Wavefront OBJ files
  - Syntax highlighting
  - Structure tree view
  - Code formatting
  - Code commenting
- Basic 3D preview of OBJ files
  - Rendering all faces using Gouraud shading model
  - Up vector axis selection

[Unreleased]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.1.0...main
[1.1.1-alpha.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.1.0...v1.1.1-alpha.1
[1.1.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.0.2...v1.1.0
[1.0.2]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.3.2...v1.0.0
[0.3.2]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.3.1...v0.3.2
[0.3.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.1.5...v0.2.0
[0.1.5]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.1.4...v0.1.5
[0.1.4]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/releases/tag/v0.1.0