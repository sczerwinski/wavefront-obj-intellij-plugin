# Contributing to Wavefront OBJ IntelliJ Plugin

Thank you for all contributions to this project.

## Bug Reports

* All bugs should be reported in the [GitHub issue tracker][issue_tracker].
* Search for any existing ticket related to your issue before you create a new one.
* Where possible, include your IDE details and stack trace.

### Including IDE details

1. Open "About" screen of your IDE, e.g.:

   <kbd>Android Studio</kbd> > <kbd>About Android Studio</kbd>

2. Click on "Copy" icon to copy IDE details and paste the content in the bug description.

   Example:
   ```
   Android Studio 4.0.1
   Build #AI-193.6911.18.40.6626763, built on June 25, 2020
   Runtime version: 1.8.0_242-release-1644-b3-6222593 x86_64
   VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
   macOS 10.15.6
   GC: ParNew, ConcurrentMarkSweep
   Memory: 1981M
   Cores: 8
   Registry: ide.new.welcome.screen.force=true
   Non-Bundled Plugins: Docker, org.jetbrains.kotlin, com.android.tool.sizereduction.plugin, com.crashlytics.tools.androidstudio, com.google.services.firebase, com.oliverlockwood.plugins.jenkinsfile, gherkin, cucumber-java, mobi.hsz.idea.gitignore, net.lagerwey.cucumber-kotlin, org.intellij.plugins.markdown
   ```

### Including plugin stack trace

If a plugin causes an exception, a red blinking error icon will appear in the bottom right corner
of the screen:

![IDE error icon](https://raw.githubusercontent.com/sczerwinski/wavefront-obj-intellij-plugin/develop/.github/images/ide_error_icon.png)

When you click on the icon, "IDE Fatal Errors" dialog will appear:

![IDE Fatal Errors dialog](https://raw.githubusercontent.com/sczerwinski/wavefront-obj-intellij-plugin/develop/.github/images/ide_error_dialog.png)

Copy the stack trace, and paste it in the bug description.

## New Feature Requests

* All new features should be requested in the [GitHub issue tracker][issue_tracker].
* Search for any existing ticket related to your feature request before you create a new one.
* Try to provide as many details as possible when you describe the new feature.

## Source Code Contributions

If you've made changes to the source code (either bug fixes or functionality enhancements)
that you think other user might benefit from, you are welcome to create a GitHub pull request.

* Please describe your pull request, as if you were reporting a bug or requesting a new feature.
* Make your pull requests as small as possible. If you make two related changes,
  create two separate pull requests.
* Make sure to format your code according to [Kotlin Coding Conventions][kotlin_coding_conventions].

### Plugin Translations

If you'd like to provide a new bundled translation for the plugin, please adhere to the following guidelines:

* Open one PR per language (or region).
* Use localization suffix in file name (not language directory).
* Provide translations for message bundle `/src/main/resources/messages/WavefrontObjBundle.properties`.
* Provide translations for inspection descriptions `/src/main/resources/inspectionDescriptions`.
* Do **NOT** provide translations for file templates, because using anything except English
  in the codebase is considered a **bad practice**.


[issue_tracker]: https://github.com/sczerwinski/wavefront-obj-intellij-plugin/issues
[kotlin_coding_conventions]: https://kotlinlang.org/docs/reference/coding-conventions.html
