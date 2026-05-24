import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun property(key: String) = providers.gradleProperty(key).get()

plugins {
    id("java")
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
    id("org.jetbrains.intellij.platform") version "2.16.0"
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
        intellijIdeaCommunity("2025.1.2")
        bundledPlugin("com.intellij.java")

        testFramework(TestFrameworkType.Platform)
    }

    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("org.json:json:20231013")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

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

    signing {
        certificateChainFile = file(env.CERTIFICATE_CHAIN_FILE.value)
        privateKeyFile = file(env.PRIVATE_KEY_FILE.value)
        password = env.PRIVATE_KEY_PASSWORD.value
    }

    publishing {
        token = env.PUBLISH_TOKEN.value
    }
}

val cleanNuXmvGrammarOutputs = tasks.register<Delete>("cleanNuXmvGrammarOutputs") {
    delete(
        file("src/main/gen/dev/mikhailshad/nuxmvplugin/language/parser/NuXmvParser.java"),
        file("src/main/gen/dev/mikhailshad/nuxmvplugin/language/psi"),
        file("src/main/gen/dev/mikhailshad/nuxmvplugin/language/lexer/_NuXmvLexer.java"),
        file("src/main/gen/dev/mikhailshad/nuxmvplugin/language/lexer/_NuXmvLexer.java~")
    )
}

val generateNuXmvParser = tasks.register<GenerateParserTask>("generateNuXmvParser") {
    sourceFile.set(file("src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/nuXmv.bnf"))
    pathToParser.set("/parser/NuXmvParser.java")
    pathToPsiRoot.set("/gen/psi")
    targetRootOutputDir.set(file("src/main/gen"))
    purgeOldFiles.set(true)
    dependsOn(cleanNuXmvGrammarOutputs)
}

val generateNuXmvLexer = tasks.register<GenerateLexerTask>("generateNuXmvLexer") {
    sourceFile.set(file("src/main/kotlin/dev/mikhailshad/nuxmvplugin/language/nuXmv.flex"))
    targetOutputDir.set(file("src/main/gen/dev/mikhailshad/nuxmvplugin/language/psi"))
    purgeOldFiles.set(false)

    dependsOn(generateNuXmvParser)
}

tasks.clean {
    dependsOn(cleanNuXmvGrammarOutputs)
}

val runIdeWithPsiViewer by intellijPlatformTesting.runIde.registering {
    plugins {
        plugin("PsiViewer", property("psiViewerPluginVersion"))
    }
}

tasks.named<KotlinCompile>("compileKotlin") {
    dependsOn(generateNuXmvLexer)
}
