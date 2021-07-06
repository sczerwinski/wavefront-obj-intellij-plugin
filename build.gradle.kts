import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
    // Kapt annotation processing
    kotlin("kapt") version "1.5.20"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.1.2"
    // gradle-grammarkit-plugin - read more: https://github.com/JetBrains/gradle-grammar-kit-plugin
    id("org.jetbrains.grammarkit") version "2021.1.3"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.2.0"
    // detekt linter - read more: https://detekt.github.io/detekt/kotlindsl.html
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

dependencies {
    api(fileTree(mapOf("dir" to "jogamp-2.4-SNAPSHOT", "include" to listOf("*.jar"))))
    api("graphics.glimpse:glimpse-core:1.0.0")
    api("graphics.glimpse:glimpse-ui:1.0.0")
    kapt("graphics.glimpse:glimpse-processor-java:1.0.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")
}

kapt {
    correctErrorTypes = true
}

// Generate parsers and lexers before Kotlin compilation.
// Read more: https://github.com/JetBrains/gradle-grammar-kit-plugin
fun generateParserTask(suffix: String, config: GenerateParser.() -> Unit = {}) =
    task<GenerateParser>("generateParser${suffix.capitalize()}") {
        source = "src/main/grammar/${suffix.capitalize()}.bnf"
        targetRoot = "${project.buildDir}/generated/source/parser/$suffix"
        pathToParser = "it/czerwinski/intellij/wavefront/lang/parser/${suffix.capitalize()}Parser.java"
        pathToPsiRoot = "it/czerwinski/intellij/wavefront/lang/psi"
        purgeOldFiles = true
        config()
    }

fun generateLexerTask(suffix: String, config: GenerateLexer.() -> Unit = {}) =
    task<GenerateLexer>("generateLexer${suffix.capitalize()}") {
        source = "src/main/grammar/${suffix.capitalize()}.flex"
        targetDir = "${project.buildDir}/generated/source/lexer/$suffix/it/czerwinski/intellij/wavefront/lang"
        targetClass = "${suffix.capitalize()}Lexer"
        purgeOldFiles = true
        config()
    }

val generateParserObj = generateParserTask("obj")
val generateParserMtl = generateParserTask("mtl")
val generateLexerObj = generateLexerTask("obj")
val generateLexerMtl = generateLexerTask("mtl")

val compileKotlin = tasks.named("compileKotlin") {
    dependsOn(generateParserObj, generateParserMtl, generateLexerObj, generateLexerMtl)
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

// Configure gradle-changelog-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version = properties("pluginVersion")
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
    config = files("./detekt-config.yml")
    buildUponDefaultConfig = true

    reports {
        html.enabled = false
        xml.enabled = false
        txt.enabled = false
        sarif {
            enabled = true
            destination = file("build/reports/detekt.sarif.json")
        }
    }
}

tasks {
    // Build for Java 11
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
        sourceSets["main"].java.srcDirs(
            "${project.buildDir}/generated/source/lexer/obj",
            "${project.buildDir}/generated/source/lexer/mtl",
            "${project.buildDir}/generated/source/parser/obj",
            "${project.buildDir}/generated/source/parser/mtl"
        )
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    withType<Detekt> {
        jvmTarget = "11"
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            File(projectDir, "README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider { changelog.getLatest().toHTML() })
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}
