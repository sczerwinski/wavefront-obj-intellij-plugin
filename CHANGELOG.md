<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Wavefront OBJ IntelliJ Plugin Changelog

## [Unreleased]

## [1.4.11] - 2025-06-28

### Added

- Add support for platform version `252.*`
- Support for relative paths to material textures

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2024.1.7, 2024.2.6, 2024.3.6, 2025.1.3, 252.23591.19`

## [1.4.10] - 2025-04-17

### Added

- Support for additional material parameters in MTL file:
  - sheen (`Ps` and `map_Ps`)
  - clearcoat thickness (`Pc`) and roughness (`Pcr`)

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2024.1.7, 2024.2.5, 2024.3.5, 2025.1`

## [1.4.9] - 2025-02-19

### Added

- Add support for platform version `251.*`
- Add buttons for hiding and showing preview error log

### Changed

- Replace `org.jetbrains.intellij` with `org.jetbrains.intellij.platform` version `2.2.1`
- `pluginVerifierIdeVersions` – upgrade to `2024.1.7, 2024.2.5, 2024.3.3, 251.21418.62`

### Fixed

- Ignore `ProcessCanceledException` in error log

## [1.4.8] - 2024-11-10

### Fixed

- Remove usages of internal API
- Remove usages of deprecated API

## [1.4.7] - 2024-11-08

### Added

- Add support for platform version `243.*`

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2024.1.7, 2024.2.4, 2024.3`

### Removed

- Remove support for platform version `223.*`
- Remove support for platform version `231.*`
- Remove support for platform version `232.*`
- Remove support for platform version `233.*`

## [1.4.6] - 2024-05-19

### Added

- Add support for platform version `242.*`

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.6, 2023.2.6, 2023.3.6, 2024.1.1, 2024.2`

### Fixed

- Refresh `ErrorLogTreeModel` on AWT event dispatching thread
- Initialize `MtlPreviewScene` with empty `MaterialTexturesProvider`
- Initialize preview editor when component obtained first time

## [1.4.5] - 2024-01-20

### Added

- Add support for platform version `241.*`

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.5, 2023.2.5, 2023.3.2, 2024.1`

## [1.4.4] - 2023-11-25

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.5, 2023.2.5, 2023.3`

### Fixed

- Allow no white character after comment symbol (`#`)

## [1.4.3] - 2023-11-12

### Added

- Add support for platform version `233.*`

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.5, 2023.2.3, 2023.3`

### Fixed

- Prevent concurrent reloading of `ErrorLogTreeModel`
- Handle exceptions when reloading `ErrorLogTreeModel`
- Use `TextEditorProvider.getInstance()` instead of creating `PsiAwareTextEditorProvider` directly
- Get model size in read action
- Move preview model creation to background task

## [1.4.2] - 2023-07-02

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.3, 2023.2`

### Fixed

- Add key to applicationConfigurable in plugin.xml

## [1.4.1] - 2023-06-03

### Added

- Reading and writing state in editor providers
- Add weight to preview editors

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.2, 2023.2`

### Removed

- Creating editors asynchronously

### Fixed

- Fix override-only methods usage violations related to `AsyncFileEditorProvider`

## [1.4.0] - 2023-05-15

### Added

- Add support for platform version `232.*`
- Support for `Tr` (transparency) in MTL file
- Support for free-form geometry syntax in OBJ files
- Support for curves and surfaces in 3D preview of OBJ files
- Support for relative (negative) indices in 3D preview of OBJ files
- Support for multi-line statements in OBJ files
- Icons matching New UI
- Inspection for unused MTL file references in OBJ files
- Inspection for duplicated MTL file references in OBJ files
- Inspection for unused materials in MTL files
- Import optimizer for OBJ files
- Quick fix for optimising MTL file references in OBJ files
- Quick fix for removing unused materials in MTL files
- Code completion for keywords and smoothing group "off" value in OBJ files
- Code completion for keywords, options and enum values in MTL files
- Documentation provider for objects and groups defined in OBJ files
- Documentation provider for materials defined in MTL files
- Folding multi-line comments
- OBJ file templates for square, cube, cylinder, sphere, NURBS circle and NURBS sphere
- MTL file templates for Phong and PBR materials

### Changed

- Use split editor (text and preview) provided by IntelliJ platform
- Remember splitter proportion per file type
- Replace annotators with inspections
- `pluginVerifierIdeVersions` – upgrade to `2022.3.3, 2023.1.1, 2023.2`

### Removed

- Remove support for platform version `222.*`

### Fixed

- Concave polygon rendering in 3D preview
- "Crop Textures" option in 3x3 plane mesh material preview shows middle tile
- Remove overridden deprecated method

## [1.3.1] - 2023-03-15

### Fixed

- Fix solid shading for OBJ files without texture coordinates
- Fix rendering of material shading preview for OBJ files without material
- Fix two-way navigation between MTL text editor and material preview
- Fix displacement glitch after OBJ preview refreshing
- Fix displacement calculations for material shading

## [1.3.0] - 2023-02-19

### Added

- Add support for platform version `231.*`
- Add support for Apple M1 processor (macOS AArch64)
- Automatically preview material at caret
- Add support for scalar map channel option (`-imfchan`) in 3D preview and material editor (only RGB)
- Add support for spherical reflection map (`refl -type sphere`) in 3D preview
- Add support for reflection map type option in material editor
- Add filtering of material properties visibility
- Add material preview mesh: 3x3 plane
- Settings for MTL file editor layout separate from OBJ file editor layout
- Setting for generating mipmaps for textures (on/off)
- Setting for selecting face culling
- Actions for images:
  - Generate Diffuse Irradiance Map
  - Generate Pre-Filtered Environment Maps
  - Generate BRDF Integration Maps
- Add environments for physically based shading method in 3D preview:
  - Beach (day)
  - Alley (night)
- Toggle editor split layout orientation with toolbar action

### Changed

- Improve IBL in PBR shader
- Convert all remaining Java code to Kotlin
- Move material properties to a tool window
- Update platform version to `2022.2.4`
- `pluginVerifierIdeVersions` – upgrade to `2022.2.4, 2022.3.2, 2023.1`

### Removed

- Remove support for platform version `221.*`
- Remove additional light source from PBR shader

### Fixed

- Fix error in 3D preview of a line without texture coordinates
- Wrap file operations in read-actions
- Fix error when adding reflection texture via material editor
- Replace usages of internal method `StartupManager.runAfterOpened()` with a `StartupActivity`
- Override `getActionUpdateThread()` in existing implementations of `AnAction`
- Use DPI-aware `JBEmptyBorder` instead of `EmptyBorder`
- Use Darcula-aware `JBColor` instead of `Color` in color scheme
- Minor grammar fixes in some descriptions
- Fix plugin change notes

## [1.2.2] - 2022-11-17

### Fixed

- Wrap file operations in read-actions

## [1.2.1] - 2022-10-31

### Added

- Add support for platform version `223.*`
- Add detekt linter

### Changed

- Change disposable parent from project to editor when possible
- Remove events from PSI tree change listeners that slow down IDE
- Update platform version to `2022.1.4`
- `pluginVerifierIdeVersions` – upgrade to `2022.1.4, 2022.2.3, 2022.3`
- Update setting GitHub Actions output

### Removed

- Remove support for platform version `213.*`
- Remove Qodana checks

### Fixed

- Replace API usages deprecated in IDE versions `2022.1` and `2022.2`
- Replace usages of `registerPostStartupActivity` with `runAfterOpened`
- Override non-deprecated version of `createPopupActionGroup` in `PBREnvironmentsComboBoxAction`
- Update deprecated property for `EnricoMi/publish-unit-test-result-action/composite` action
- Fix changelog in release draft

## [1.2.0] - 2022-07-26

### Added

- Add support for platform version `222.*`
- MTL material editor with preview
- Material editor settings:
  - Default material preview mesh (cube, cylinder, sphere)
  - Default material type (Phong or PBR)
- Support diffuse texture transparency in 3D preview
- New OBJ file action
- New MTL file action
- Add environments for physically based shading method in 3D preview:
  - Office (interior)
  - Garage (interior)
- Use JVM toolchain for configuring source/target compilation compatibility
- Make sure GitHub Actions release jobs have write permissions
- Add Qodana checks

### Changed

- Improved action and gutter icons to work with high contrast theme
- Use non-null values in editor state
- Migrate to Kotlin UI DSL version 2
- Move settings icon to the left toolbar in 3D preview
- Disable face culling in 3D preview
- Update GitHub Actions build pipeline
- Update platform version to `2021.3.3`
- `pluginVerifierIdeVersions` – upgrade to `2021.3.3, 2022.1.3, 2022.2`

### Removed

- Remove support for platform version `203.*`
- Remove support for platform version `211.*`
- Remove support for platform version `212.*`
- Remove detekt linter
- Remove ktlint linter

### Fixed

- Replace usage of deprecated `UIUtil.getClientProperty` with direct call to `JComponent.getClientProperty`
- Replace usage of deprecated `FilenameIndex.getFilesByName` with `FilenameIndex.getVirtualFilesByName`
  and additional mapping using `PsiManager.findFile`
- Replace usages of deprecated `Iterable.sumBy` with `Iterable.sumOf`
- Replace usages of deprecated `String.toLowerCase` with `String.lowercase`
- Replace usage of deprecated `Char.toInt` with `Char.code`
- Prevent long operations from running on UI thread
- Workaround for unit tests not running
- Add `kspKotlin` Gradle task dependencies
- Fix possible `NullPointerException` in `ObjAnnotator`

## [1.1.2] - 2022-04-30

### Added

- OBJ 3D preview status bar with number of objects, groups, vertices, faces and triangles
- Display Number of triangles for each object and group in structure view

### Fixed

- Change language IDs to `OBJ` and `MTL`.
- Remove unnecessary abstract class `ObjLineElementImpl`.

## [1.1.1] - 2022-03-13

### Added

- Support for platform version `221.*`
- Color preview markers in MTL files
- Add city square (night) environment for physically based shading method in 3D preview

### Changed

- Line markers made configurable
- PBR shader calculations in world space
- Use pre-calculated bitangents in PBR shader
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4, 2021.1.3, 2021.2.4, 2021.3.1, 2022.1`

### Fixed

- Improve PBR shader calculations
- Fix displacement step calculation
- Refresh 3D preview when any of the MTL and texture files referenced by OBJ file changes
- Fix 3D preview issues when loading a project with multiple OBJ files open
- Allow material color components outside of range

### Security

- Sign plugin before publishing

## [1.1.0] - 2022-01-09

### Added

- Extended MTL syntax for physically based rendering parameters:
  - roughness (`Pr` and `map_Pr`)
  - metalness (`Pm` and `map_Pm`)
  - normal map (`norm`)
  - emission (`Ke` and `map_Ke`)
- Physically based shading method in 3D preview

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2020.3.4, 2021.1.3, 2021.2.4, 2021.3.1`

### Fixed

- Prevent texture loading on GL thread

## [1.0.2] - 2021-11-09

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
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4, 2021.1.3, 2021.2.1`, `2021.3`
- Gradle – Changelog plugin configuration update

### Fixed

- Prevent infinite displacement loop
- Use `DynamicBundle` instead of `AbstractBundle` in `WavefrontObjBundle.kt`
- Replace deprecated `ServiceManager.getService` with `Application.getService`

## [1.0.1] - 2021-07-16

### Added

- Support for platform version `212.*`

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2020.3.4`, `2021.1.3`, `2021.2`

## [1.0.0] - 2021-07-04

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
- `pluginVerifierIdeVersions` – upgrade to `2020.3.4`, `2021.1.3`
- Trigger GitHub Actions `Build` workflows only on pushes to `main` branch or pull request to avoid duplicated checks

### Fixed

- Prevent IDE from freezing after a big change to an OBJ file
- Display correct descriptions of OBJ and MTL token types
- Improve UI messages
- Require spaces after keywords, options and values

## [0.3.2] - 2021-04-11

### Added

- Support for relative indices in OBJ files (#95)
- Refresh action in 3D preview
- Setting for default shading method in 3D preview

### Changed

- Use standard IDE error icons
- Display 3D preview errors below the preview (#112)

## [0.3.1] - 2021-04-08

### Added

- Support for platform version `211.*`

### Changed

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

## [0.3.0] - 2021-04-08

### Added

- Wireframe shading in the 3D preview
- Material shading in the 3D preview (material colors)
- Build configuration:
  - `properties` shorthand function for accessing `gradle.properties` in a cleaner way
  - Dependabot check for GitHub Actions used in workflow files

### Changed

- `pluginVerifierIdeVersions` – upgrade to `2020.1.4`, `2020.2.4`, `2020.3.3`
- Update `changelog` Gradle plugin configuration
- Migrate to GLES 2.0 with [Glimpse](https://glimpse.graphics/)

### Fixed

- Provide list of available texture files to texture file reference
- Fix `README.md` file resolution in the `build.gradle.kts`

## [0.2.1] - 2021-02-05

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

- `pluginVerifierIdeVersions` – upgrade to `2020.1.4`, `2020.2.4`, `2020.3.2`

### Fixed

- Separate color settings attribute key for OBJ and MTL constants
- JogAmp bug on macOS causing IDE crash when showing 3D preview (fixed by using JogAmp snapshot dependencies v2.4)
- Limit initial camera distance for very small objects
- Refresh the 3D preview after the OBJ file edited
- Use relative paths in MTL file references

## [0.2.0] - 2021-01-31

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

## [0.1.5] - 2021-01-28

### Added

- Dependabot integration
- Show `idea.log` logs of the run IDE in the Run console

### Changed

- `build.gradle.kts`: simpler syntax for configuring `KotlinCompile`
- `pluginVerifierIdeVersions` – upgrade to `2020.1.4`, `2020.2.3`, `2020.3.1`

### Fixed

- Allow `float` scientific notation with upper case `E`
- Return `Supplier<@Nls String>` instead of `String` in `MyBundle.messagePointer`
- GitHub Actions: Use the correct property in the "Upload artifact" step

## [0.1.4] - 2020-12-02

### Added

- Predefined Run/Debug Configurations
- Support for platform version 203.*

## [0.1.3] - 2020-11-06

### Added

- Setting: Disable 3D preview
- Setting: Default layout

### Changed

- Update `pluginVerifierIdeVersions` in the `gradle.properties` file

### Fixed

- Not creating `GLPanel` before 3D preview set to visible

## [0.1.2] - 2020-10-31

### Added

- Fix default to opt-out of bundling Kotlin standard library in plugin distribution
- Integration with [IntelliJ Plugin Verifier](https://github.com/JetBrains/intellij-plugin-verifier) through the [Gradle IntelliJ Plugin](https://github.com/JetBrains/gradle-intellij-plugin#plugin-verifier-dsl) `runPluginVerifier` task

### Changed

- Set default editor layout to text only
- Update platform version to `2020.1`
- Upgrade Gradle Wrapper to `6.7`

### Removed

- Remove support for platform version `2019.3`
- Remove Third-party IntelliJ Plugin Verifier GitHub Action

### Fixed

- Handling exceptions in 3D preview
- Trying to load different GL profiles (#38)
- Allowing for integer values of coordinates in OBJ files (#39)
- `pluginName` variable name collision with `intellij` closure getter in Gradle configuration
- Using correct encoding of ellipsis character when initializing 3D preview

## [0.1.1] - 2020-08-09

### Fixed

- Correct link in plugin description

## [0.1.0] - 2020-08-09

### Added

- Support for Wavefront OBJ files
  - Syntax highlighting
  - Structure tree view
  - Code formatting
  - Code commenting
- Basic 3D preview of OBJ files
  - Rendering all faces using Gouraud shading model
  - Up vector axis selection

[Unreleased]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.11...main
[1.4.11]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.10...v1.4.11
[1.4.10]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.9...v1.4.10
[1.4.9]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.8...v1.4.9
[1.4.8]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.7...v1.4.8
[1.4.7]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.6...v1.4.7
[1.4.6]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.5...v1.4.6
[1.4.5]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.4...v1.4.5
[1.4.4]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.3...v1.4.4
[1.4.3]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.2...v1.4.3
[1.4.2]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.1...v1.4.2
[1.4.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.4.0...v1.4.1
[1.4.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.3.1...v1.4.0
[1.3.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.3.0...v1.3.1
[1.3.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.2.2...v1.3.0
[1.2.2]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.2.1...v1.2.2
[1.2.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.1.2...v1.2.0
[1.1.2]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.1.1...v1.1.2
[1.1.1]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/compare/v1.1.0...v1.1.1
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
