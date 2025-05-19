import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.4.0"
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
}

val intellijIdeaBuild = 243

group = "dev.MikhailShad"
version = "0.0.2+IJ.$intellijIdeaBuild"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.5")
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
        version = project.version.toString()
        ideaVersion {
            sinceBuild = "$intellijIdeaBuild"
            untilBuild = "$intellijIdeaBuild.*"
        }
    }
}

val runIdeWithPsiViewer by intellijPlatformTesting.runIde.registering {
    plugins {
        plugin("PsiViewer", "243.7768")
    }
}
