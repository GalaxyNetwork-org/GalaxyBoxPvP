
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.sayandev.org/snapshots")
    }
}

plugins {
    id("org.sayandev.stickynote.settings") version "1.10.4.41"}

rootProject.name = "GalaxyBoxPvP"
