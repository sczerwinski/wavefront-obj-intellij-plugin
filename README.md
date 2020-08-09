# Wavefront OBJ IntelliJ Plugin

![Build][build_badge]
[![Version][jb_version_badge]][jb_plugin_page]
[![Downloads][jb_download_badge]][jb_plugin_page]

<!-- Plugin description -->
Adds support for [Wavefront OBJ files](https://en.wikipedia.org/wiki/Wavefront_.obj_file).

- OBJ file format syntax
- 3D preview of OBJ files
<!-- Plugin description end -->

## Features

### Supported features

- OBJ file format:
  - `v` – vertices,
  - `vt` – texture coordinates,
  - `vn` – normals,
  - `f` – faces,
  - `l` – lines,
  - `p` – points,
  - `s` – smooth shading flagg (`1`/`off`),
  - `o` – objects,
  - `g` – groups,
  - `mtllib` – references to MTL file (without navigation or validation),
  - `usemtl` – references to materials (without navigation or validation).
- 3D preview of OBJ file:
  - text only, preview only, or split,
  - selection of up axis,
  - Gouraud shading model.

### Planned features

The following features are already under consideration, so please refrain from requesting them in
issue tracker.

- MTL file format,
- navigation between OBJ and MTL files,
- 3D preview improvements:
  - additional shading models: flat and Phong,
  - textures,
  - highlighting selected element,
  - applying materials from MTL files.

## Installation

- Using IDE built-in plugin system:

  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> >
  <kbd>Search for "Wavefront OBJ"</kbd> > <kbd>Install Plugin</kbd>

- Manually:

  Download the
  [latest release][latest_release]
  and install it manually using:
   
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> >
  <kbd>Install plugin from disk...</kbd>

## Usage

### OBJ File Type And Editor

After installation, `.obj` file extension will be automatically associated with Wavefront OBJ
file format and editor.

#### 3D Preview Controls

- Hold mouse left button on the 3D preview and move to pan the camera.
- Use mouse wheel to zoom in/out.

### Settings

- To change Wavefront OBJ editor color scheme, use:

  <kbd>Preferences</kbd> > <kbd>Editor</kbd> > <kbd>Color Scheme</kbd> > <kbd>Wavefront OBJ</kbd>

- To change plugin configuration, use

  <kbd>Preferences</kbd> > <kbd>Languages & Frameworks</kbd> > <kbd>Wavefront OBJ</kbd>

---

Plugin based on the [IntelliJ Platform Plugin Template][template].

[build_badge]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/workflows/Build/badge.svg
[jb_version_badge]: https://img.shields.io/jetbrains/plugin/v/14843-wavefront-obj
[jb_download_badge]: https://img.shields.io/jetbrains/plugin/d/14843-wavefront-obj
[jb_plugin_page]: https://plugins.jetbrains.com/plugin/14843-wavefront-obj
[latest_release]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/releases/latest
[template]: https://github.com/JetBrains/intellij-platform-plugin-template
