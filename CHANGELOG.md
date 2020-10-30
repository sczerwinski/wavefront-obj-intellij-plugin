<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Wavefront OBJ IntelliJ Plugin Changelog

## [Unreleased]
### Added
- Fix default to opt-out of bundling Kotlin standard library in plugin distribution
- Introduced `platformPlugins` property in `gradle.properties` for configuring dependencies to bundled/external plugins

### Changed
- Update platform version to `2020.1`
- Update IntelliJ Platform Plugin Template to `0.5.1`
  - Simplify and optimize GitHub Actions
  - GitHub Actions: allow releasing plugin even for the base project
  - Disable "Release Draft" job for pull requests in the "Build" GitHub Actions Workflow
  - Upgrade Gradle Wrapper to `6.7`
  - Dependencies – upgrade `org.jetbrains.kotlin.jvm` to `1.4.10`
  - Dependencies – upgrade `org.jetbrains.intellij` to `0.5.0`
  - Dependencies – upgrade `org.jetbrains.changelog` to `0.6.2`
  - Dependencies – upgrade `io.gitlab.arturbosch.detekt` to `1.14.1`
  - Dependencies – upgrade `org.jlleitschuh.gradle.ktlint` to `9.4.1`

### Removed
- Support for platform version `2019.3`

### Fixed
- GitHub Actions – cache Gradle dependencies and wrapper separately
- `pluginName` variable name collision with `intellij` closure getter in Gradle configuration

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
