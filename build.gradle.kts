import org.sayandev.plugin.StickyNoteModules

plugins {
    java
    kotlin("jvm") version "2.2.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("de.eldoria.plugin-yml.bukkit") version "0.7.1"
    id("org.sayandev.stickynote.project")
}

val slug = rootProject.name.lowercase()
group = "xyz.lncvrt"
version = "1.0.0"

stickynote {
    modules(StickyNoteModules.BUKKIT)
}

repositories {
    mavenCentral()
    mavenLocal()
    
    maven("https://repo.sayandev.org/snapshots")
    maven("https://repo.sayandev.org/releases")
    maven("https://repo.sayandev.org/private")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://jitpack.io")
    maven("https://ci.frostcast.net/plugin/repository/everything")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.booksaw:BetterTeams:4.13.4")
    compileOnly("me.confuser.banmanager:BanManagerCommon:7.9.0")
}

tasks {
    jar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
        archiveClassifier.set(null as String?)
        destinationDirectory.set(file(rootProject.projectDir.path + "/bin"))
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }
    
    runServer {
        minecraftVersion("1.21.4")
        
        jvmArgs()
    }
    
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
    
    configurations {
        create("compileOnlyApiResolved") {
            isCanBeResolved = true
            extendsFrom(configurations.getByName("compileOnlyApi"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
    
    processResources {
        filesMatching(listOf("**plugin.yml", "**plugin.json")) {
            expand(
                "version" to project.version as String,
                "slug" to slug,
                "name" to rootProject.name,
                "description" to project.description
            )
        }
    }
}
    
bukkit {
    main = "$group.${slug}.${rootProject.name}Plugin"
    version = rootProject.version.toString()
    website = "lncvrt.xyz"
    apiVersion = "1.21"
    depend = listOf("PlaceholderAPI", "BetterTeams", "BanManager")
    authors = listOf("Lncvrt")
    prefix = rootProject.name
}
    
java {
    disableAutoTargetJvm()
    if (gradle.startParameter.getTaskNames().any { it.startsWith("runServer") || it.startsWith("runFolia") || it.startsWith("runVelocity") }) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    } else {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}