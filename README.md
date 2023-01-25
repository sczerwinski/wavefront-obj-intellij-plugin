# Wavefront OBJ IntelliJ Plugin*

![Build][build_badge]
[![Version][jb_version_badge]][jb_plugin_page]
[![Downloads][jb_download_badge]][jb_plugin_page]
[![Rating][jb_rating_badge]][jb_plugin_page]

<sup>\*Also works with Android Studio, PyCharm, WebStorm,
and other IDE's based on IntelliJ IDEA.</sup>

<!-- Plugin description -->
Adds support for [Wavefront OBJ files](https://en.wikipedia.org/wiki/Wavefront_.obj_file)
and MTL (material library) files.

- OBJ file format syntax
- MTL file format syntax
- 3D preview of OBJ files
- material editor
<!-- Plugin description end -->

## Features

### Supported features

Core features include:
- OBJ file format support
- MTL file format support
- 3D preview of OBJ file
- material editor with preview

## Supported operating systems

3D preview is supported for:
- Windows:
  - x86-64
- Mac OS X:
  - x86-64
  - AArch64 (since v1.3.0)
- Linux:
  - x86-64
  - AArch64

## Installation

### Using IDE Built-In Plugin System

<kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> >
<kbd>Search for "Wavefront OBJ"</kbd> > <kbd>Install Plugin</kbd>

### Manually

Download the [latest release][latest_release] and install it manually using:

<kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> >
<kbd>Install plugin from disk...</kbd>

### EAP Releases

To install EAP version of the plugin, use:

<kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> >
<kbd>Manage Plugin Repositories...</kbd>

and add the following URL to the list:

```
https://plugins.jetbrains.com/plugins/eap/list
```

## Usage

### OBJ File Type And Editor

After installation, `.obj` file extension will be automatically associated with Wavefront OBJ
file format and editor.

#### 3D Preview Controls

- Hold mouse left button on the 3D preview and move to pan the camera.
- Use mouse wheel to zoom in/out.

### MTL File Type And Editor

After installation, `.mtl` file extension will be automatically associated with Wavefront MTL
file format and editor.

### Settings

- To change Wavefront OBJ editor color scheme, use:

  <kbd>Preferences</kbd> > <kbd>Editor</kbd> > <kbd>Color Scheme</kbd> > <kbd>Wavefront OBJ</kbd>

- To change Wavefront MTL editor color scheme, use:

  <kbd>Preferences</kbd> > <kbd>Editor</kbd> > <kbd>Color Scheme</kbd> > <kbd>Wavefront MTL</kbd>

- To change plugin configuration, use:

  <kbd>Preferences</kbd> > <kbd>Languages & Frameworks</kbd> > <kbd>Wavefront OBJ</kbd>

---

Plugin based on the [IntelliJ Platform Plugin Template][template].

[build_badge]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/workflows/Build/badge.svg
[jb_version_badge]: https://img.shields.io/jetbrains/plugin/v/14843-wavefront-obj
[jb_download_badge]: https://img.shields.io/jetbrains/plugin/d/14843-wavefront-obj
[jb_rating_badge]: https://img.shields.io/jetbrains/plugin/r/rating/14843-wavefront-obj
[jb_plugin_page]: https://plugins.jetbrains.com/plugin/14843-wavefront-obj
[latest_release]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/releases/latest
[template]: https://github.com/JetBrains/intellij-platform-plugin-template
