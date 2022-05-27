import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    // Kotlin Symbol Processing
    id("com.google.devtools.ksp") version "1.6.21-1.0.5"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.5.3"
    // Gradle Grammar-Kit Plugin
    id("org.jetbrains.grammarkit") version "2021.2.2"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

// Set the JVM language level used to compile sources and generate files - Java 11 is required since 2020.3
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    api(fileTree(mapOf("dir" to "jogamp-2.4-SNAPSHOT", "include" to listOf("*.jar"))))
    api("graphics.glimpse:glimpse-core:1.1.0")
    api("graphics.glimpse:glimpse-obj:1.1.0")
    api("graphics.glimpse:glimpse-ui:1.1.0")
    ksp("graphics.glimpse:glimpse-processor-ksp:1.1.0")
}

// Generate parsers and lexers before Kotlin compilation.
// Read more: https://github.com/JetBrains/gradle-grammar-kit-plugin
fun generateParserTask(suffix: String, config: GenerateParserTask.() -> Unit = {}) =
    task<GenerateParserTask>("generateParser${suffix.capitalize()}") {
        source.set("src/main/grammar/${suffix.capitalize()}.bnf")
        targetRoot.set("${project.buildDir}/generated/source/parser/$suffix")
        pathToParser.set("it/czerwinski/intellij/wavefront/lang/parser/${suffix.capitalize()}Parser.java")
        pathToPsiRoot.set("it/czerwinski/intellij/wavefront/lang/psi")
        purgeOldFiles.set(true)
        config()
    }

fun generateLexerTask(suffix: String, config: GenerateLexerTask.() -> Unit = {}) =
    task<GenerateLexerTask>("generateLexer${suffix.capitalize()}") {
        source.set("src/main/grammar/${suffix.capitalize()}.flex")
        targetDir.set("${project.buildDir}/generated/source/lexer/$suffix/it/czerwinski/intellij/wavefront/lang")
        targetClass.set("${suffix.capitalize()}Lexer")
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
    version.set(properties("pluginVersion"))
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(projectDir.resolve(".qodana").canonicalPath)
    reportPath.set(projectDir.resolve("build/reports/inspections").canonicalPath)
    saveReport.set(true)
    showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
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
            "${project.buildDir}/generated/source/lexer/obj",
            "${project.buildDir}/generated/source/lexer/mtl",
            "${project.buildDir}/generated/source/parser/obj",
            "${project.buildDir}/generated/source/parser/mtl",
            "${project.buildDir}/generated/ksp/main/kotlin"
        )
    }

    val test by getting(Test::class) {
        isScanForTestClasses = false
        include("**/*Test.class")
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            projectDir.resolve("README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(
            provider {
                changelog.run {
                    getOrNull(properties("pluginVersion")) ?: getLatest()
                }.toHTML()
            }
        )
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(',').map(String::trim).filter(String::isNotEmpty))
    }

    signPlugin {
        certificateChain.set(System.getenv("SIGNING_CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("SIGNING_PRIVATE_KEY"))
        password.set(System.getenv("SIGNING_PASSWORD"))
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
