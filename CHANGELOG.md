<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Wavefront OBJ IntelliJ Plugin Changelog

## [Unreleased]
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
