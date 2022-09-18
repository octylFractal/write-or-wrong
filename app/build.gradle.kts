import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    application
    kotlin("jvm") version "1.7.10"
    alias(libs.plugins.licenser)
    alias(libs.plugins.osdetector)
    alias(libs.plugins.jlink)
}
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.eclipse.jgit:org.eclipse.jgit:6.3.0.202209071007-r")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(18))
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-parameters"))
}

repositories {
    mavenCentral()
}

configure<LicenseExtension> {
    exclude {
        it.file.startsWith(project.buildDir)
    }
    header(rootProject.file("HEADER.txt"))
    (this as ExtensionAware).extra.apply {
        for (key in listOf("organization", "url")) {
            set(key, rootProject.property(key))
        }
    }
}

dependencies {
    compileOnly(libs.jetbrains.annotations)

    implementation(libs.tinylog.api.kotlin)
    runtimeOnly(libs.tinylog.impl)

    implementation(libs.directories)

    for (lib in listOf(libs.javafx.base, libs.javafx.controls, libs.javafx.graphics, libs.javafx.fxml)) {
        implementation(lib)
        implementation(variantOf(lib) {
            classifier(
                when (osdetector.os) {
                    "osx" -> "mac"
                    "windows" -> "win"
                    else -> "linux"
                }
            )
        })
    }

    implementation(libs.controlsfx)

    implementation(platform(libs.ikonli.bom))
    implementation(libs.ikonli.javafx)
    implementation(libs.ikonli.fontawesome5)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

configurations.runtimeClasspath {
    // Exclude the stdlib metadata from the runtime classpath
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
}

application {
    applicationName = "Write or Wrong!"
    mainClass.set("net.octyl.writeorwrong.WriteOrWrong")
    mainModule.set("net.octyl.writeorwrong")
    applicationDefaultJvmArgs = listOf(
        "-Xms64M",
        "-Xmx512M",
        "-XX:G1PeriodicGCInterval=1000"
    )
}

tasks.compileJava {
    options.javaModuleMainClass.set(application.mainClass)
    val git = org.eclipse.jgit.api.Git.open(project.rootDir)
    val headRef = git.repository.resolve("HEAD")
    val gitInfo = if (headRef != null) {
        var gitInfo = git.repository.newObjectReader().abbreviate(headRef).name()
        if (!git.status().call().isClean) {
            gitInfo += "-dirty"
        }
        gitInfo
    } else {
        "unknown"
    }
    options.javaModuleVersion.set("${project.version}+git.$gitInfo")

    // Patch in Kotlin classes to module
    dependsOn("compileKotlin")
    val moduleName = application.mainModule.get()
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
    )
}

jlink {
    launcher {
        name = "write-or-wrong"
    }
}

tasks.named<JavaExec>("run") {
    workingDir(".")
}

tasks.test {
    useJUnitPlatform()
}
