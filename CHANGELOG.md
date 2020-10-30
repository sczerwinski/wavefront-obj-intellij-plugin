<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Wavefront OBJ IntelliJ Plugin Changelog

## [Unreleased]
### Added
- Fix default to opt-out of bundling Kotlin standard library in plugin distribution
- Introduced `platformPlugins` property in `gradle.properties` for configuring dependencies to bundled/external plugins
- Integration with [IntelliJ Plugin Verifier](https://github.com/JetBrains/intellij-plugin-verifier) through the [Gradle IntelliJ Plugin](https://github.com/JetBrains/gradle-intellij-plugin#plugin-verifier-dsl) `runPluginVerifier` task
- Cache downloaded IDEs used by Plugin Verifier for the verification

### Changed
- Update platform version to `2020.1`
- GitHub Actions:
  - Simplify and optimize GitHub Actions
  - Allow releasing plugin even for the base project
  - Disable "Release Draft" job for pull requests in the "Build" Workflow
  - `gradleValidation` update to `gradle/wrapper-validation-action@v1.0.3`
  - `releaseDraft` update to `actions/download-artifact@v2`
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
- GitHub Actions – cache Gradle dependencies and wrapper separately
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
