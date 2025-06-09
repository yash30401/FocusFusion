// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by extra("2.0.0") // Ensures Kotlin 2.0.0 is used

    repositories { // Repositories for the classpath dependencies below
        google()
        mavenCentral()
    }
    dependencies {
        // Provides the Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.4.2") // Using a stable AGP

        // ✅ CRITICAL: Provides the main Kotlin plugin and makes sub-plugins
        // like kotlin("android"), kotlin("kapt"), kotlin("plugin.compose") available.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // Hilt and Google Services plugins
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1") // Use latest stable Hilt plugin
        classpath("com.google.gms:google-services:4.4.2") // Use latest stable Google Services
    }
}
plugins {
    id("com.android.application") version "8.4.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.kapt") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false // For Compose
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}