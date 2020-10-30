<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Wavefront OBJ IntelliJ Plugin Changelog

## [Unreleased]
### Added
- Fix default to opt-out of bundling Kotlin standard library in plugin distribution

### Changed
- Using IntelliJ Platform Plugin Template 0.4.0
  - Simplify and optimize GitHub Actions
  - GitHub Actions: allow releasing plugin even for the base project
  - Gradle Wrapper upgrade to 6.6.1
  - Dependencies – upgrade `org.jetbrains.kotlin.jvm` to 1.4.10
  - Dependencies – upgrade `org.jetbrains.intellij` to 0.4.26
  - Dependencies – upgrade `org.jetbrains.changelog` to 0.6.0
  - Dependencies – upgrade `io.gitlab.arturbosch.detekt` to 1.14.1
  - Dependencies – upgrade `org.jlleitschuh.gradle.ktlint` to 9.4.0

### Removed
- Support for IDE v.2019.3

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
