import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.Constants.Constraints
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    // Kotlin Symbol Processing
    id("com.google.devtools.ksp") version "2.1.10-1.0.30"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij.platform") version "2.3.0"
    // Gradle Grammar-Kit Plugin
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "2.2.1"
    // detekt linter - read more: https://detekt.github.io/detekt/kotlindsl.html
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// Configure project's dependencies
repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
    maven {
        name = "JogAmp"
        url = uri(path = "https://www.jogamp.org/deployment/maven/")
    }
}

// Set the JVM language level used to compile sources and generate files - Java 17 is required since 2022.2
kotlin {
    jvmToolchain(17)
}

dependencies {
    api("graphics.glimpse:glimpse-core:2.0.0-alpha.3")
    api("graphics.glimpse:glimpse-geom:2.0.0-alpha.3")
    api("graphics.glimpse:glimpse-obj:2.0.0-alpha.3")
    api("graphics.glimpse:glimpse-offscreen:2.0.0-alpha.3")
    api("graphics.glimpse:glimpse-ui:2.0.0-alpha.3")
    ksp("graphics.glimpse:glimpse-processor-ksp:2.0.0-alpha.3")
    api("org.jogamp.jogl:jogl-all-main:2.5.0")
    api("org.jogamp.gluegen:gluegen-rt-main:2.5.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")

    testImplementation("junit:junit:4.13.2")

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

        // Plugin Dependencies. Uses `platformBundledPlugins` property from the gradle.properties file for bundled IntelliJ Platform plugins.
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

        // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file for plugin from JetBrains Marketplace.
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
}

// Generate parsers and lexers before Kotlin compilation.
// Read more: https://github.com/JetBrains/gradle-grammar-kit-plugin
fun generateParserTask(suffix: String, config: GenerateParserTask.() -> Unit = {}) =
    task<GenerateParserTask>("generateParser${suffix.replaceFirstChar { it.uppercaseChar() }}") {
        sourceFile.set(file("src/main/grammar/${suffix.replaceFirstChar { it.uppercaseChar() }}.bnf"))
        targetRootOutputDir.set(file("${project.layout.buildDirectory.get()}/generated/source/parser/$suffix"))
        pathToParser.set("it/czerwinski/intellij/wavefront/lang/parser/${suffix.replaceFirstChar { it.uppercaseChar() }}Parser.java")
        pathToPsiRoot.set("it/czerwinski/intellij/wavefront/lang/psi")
        purgeOldFiles.set(true)
        config()
    }

fun generateLexerTask(suffix: String, config: GenerateLexerTask.() -> Unit = {}) =
    task<GenerateLexerTask>("generateLexer${suffix.replaceFirstChar { it.uppercaseChar() }}") {
        sourceFile.set(file("src/main/grammar/${suffix.replaceFirstChar { it.uppercaseChar() }}.flex"))
        targetOutputDir.set(file("${project.layout.buildDirectory.get()}/generated/source/lexer/$suffix/it/czerwinski/intellij/wavefront/lang"))
        purgeOldFiles.set(true)
        config()
    }

val generateParserObj = generateParserTask("obj")
val generateParserMtl = generateParserTask("mtl")
val generateLexerObj = generateLexerTask("obj")
val generateLexerMtl = generateLexerTask("mtl")

val compileKotlin = tasks.named("compileKotlin") {
    dependsOn(generateParserObj, generateParserMtl, generateLexerObj, generateLexerMtl)
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        version = providers.gradleProperty("pluginVersion")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                val changelogItem = getOrNull(pluginVersion) ?: getUnreleased()
                renderItem(
                    changelogItem.withHeader(false),
                    Changelog.OutputType.HTML
                )
            }
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

    signing {
        certificateChain = providers.environmentVariable("SIGNING_CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("SIGNING_PRIVATE_KEY")
        password = providers.environmentVariable("SIGNING_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")

        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    pluginVerification {
        ides {
            providers.gradleProperty("pluginVerifierIdeVersions").get()
                .splitToSequence(',')
                .map(String::trim)
                .filter(String::isNotEmpty)
                .forEach { version ->
                    ide(IntelliJPlatformType.IntellijIdeaCommunity, version)
                }
        }
        freeArgs = listOf("-mute", "TemplateWordInPluginId")
    }
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
    config.setFrom(files("./detekt-config.yml"))
    buildUponDefaultConfig = true
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

tasks {
    afterEvaluate {
        named("kspKotlin") {
            dependsOn(generateParserObj, generateParserMtl, generateLexerObj, generateLexerMtl)
        }
    }

    withType<JavaCompile> {
        sourceSets["main"].java.srcDirs(
            "${project.projectDir}/src/main/kotlin",
            "${project.layout.buildDirectory.get()}/generated/source/lexer/obj",
            "${project.layout.buildDirectory.get()}/generated/source/lexer/mtl",
            "${project.layout.buildDirectory.get()}/generated/source/parser/obj",
            "${project.layout.buildDirectory.get()}/generated/source/parser/mtl",
            "${project.layout.buildDirectory.get()}/generated/ksp/main/kotlin"
        )
    }

    withType<Detekt> {
        // Configure detekt reports.
        // Read more: https://detekt.github.io/detekt/kotlindsl.html
        reports {
            html.required.set(false)
            xml {
                required.set(true)
                outputLocation.set(file("build/reports/detekt.xml"))
            }
            txt.required.set(false)
            sarif {
                required.set(true)
                outputLocation.set(file("build/reports/detekt.sarif.json"))
            }
        }
    }

    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }
}

val runIdeForUiTests by intellijPlatformTesting.runIde.registering {
    task {
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf(
                "-Drobot-server.port=8082",
                "-Dide.mac.message.dialogs.as.sheets=false",
                "-Djb.privacy.policy.text=<!--999.999-->",
                "-Djb.consents.confirmation.enabled=false",
            )
        }
    }

    plugins {
        robotServerPlugin(Constraints.LATEST_VERSION)
    }
}
