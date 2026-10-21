import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun property(key: String) = providers.gradleProperty(key).get()

plugins {
    id("java")
    kotlin("jvm") version "2.4.21"
    kotlin("plugin.serialization") version "2.4.21"
    id("org.jetbrains.intellij.platform") version "2.19.0"
    id("org.jetbrains.intellij.platform.grammarkit") version "2.19.0"
    id("org.jetbrains.changelog") version "2.5.0"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

group = property("pluginGroup")
version = property("pluginVersion")

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        java.srcDirs("src/main/gen")
    }
    test {
        kotlin.srcDirs("src/test/kotlin")
    }
}

idea {
    module {
        generatedSourceDirs.add(file("src/main/gen"))
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.2.3")
        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.modules.jcef")
        grammarKit("2023.3.4")
        jflex("1.10.18")

        testFramework(TestFrameworkType.Platform)
    }

    implementation("io.github.oshai:kotlin-logging-jvm:8.0.4")
    implementation("org.json:json:20260814")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    buildSearchableOptions = true
    instrumentCode = true
    projectName = project.name

    pluginConfiguration {
        id = property("pluginId")
        name = property("pluginName")
        version = property("pluginVersion")

        ideaVersion {
            sinceBuild = property("pluginSinceBuild")
        }
    }

    changelog {
        version = property("pluginVersion")
        path = "${project.projectDir}/CHANGELOG.md"
        unreleasedTerm = "[Unreleased]"
        keepUnreleasedSection = false
    }

    signing {
        certificateChainFile = file(env.CERTIFICATE_CHAIN_FILE.value)
        privateKeyFile = file(env.PRIVATE_KEY_FILE.value)
        password = env.PRIVATE_KEY_PASSWORD.value
    }

    publishing {
        token = env.PUBLISH_TOKEN.value
    }
}

tasks {
    generateParser {
        sourceFile = file("src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/nuXmv.bnf")
        targetRootOutputDir = file("src/main/gen")
        pathToParser = "dev/mikhailshad/nuxmvplugin/language/parser/NuXmvParser.java"
        pathToPsiRoot = "dev/mikhailshad/nuxmvplugin/language/psi"
        purgeOldFiles = true
    }
    generateLexer {
        sourceFile = file("src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/nuXmv.flex")
        targetRootOutputDir = file("src/main/gen")
        pathToClass = "dev/mikhailshad/nuxmvplugin/language/lexer/_NuXmvLexer.java"
        purgeOldFiles = true
        dependsOn(generateParser)
    }
    prepareTestSandbox {
        disabledPlugins.add("com.intellij.modules.ultimate")
    }
}

val runIdeWithPsiViewer = intellijPlatformTesting.runIde.register("runIdeWithPsiViewer") {
    plugins {
        plugin("PsiViewer", property("psiViewerPluginVersion"))
    }
}

tasks.named<KotlinCompile>("compileKotlin") {
    dependsOn(tasks.generateLexer)
}

kotlin {
    jvmToolchain(25)
}
