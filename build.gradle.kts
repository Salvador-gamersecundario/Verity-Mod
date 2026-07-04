plugins {
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
}

// Leer propiedades de gradle.properties
val modId = project.findProperty("mod_id")?.toString() ?: "verity_mod"
val modVersion = project.findProperty("mod_version")?.toString() ?: "1.0.0"
val modGroupId = project.findProperty("mod_group_id")?.toString() ?: "com.luis"
val mcVersion = project.findProperty("minecraft_version")?.toString() ?: "1.20.1"
val forgeVersion = project.findProperty("forge_version")?.toString() ?: "47.4.10"
val mapChannel = project.findProperty("mapping_channel")?.toString() ?: "official"
val mapVersion = project.findProperty("mapping_version")?.toString() ?: "1.20.1"

version = modVersion
group = modGroupId

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

minecraft {
    mappings(mapChannel, mapVersion)
    
    copyIdeResources.set(true)
    
    runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modId)
            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }
        
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modId)
            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

repositories {
    mavenCentral()
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    minecraft("net.minecraftforge:forge:$mcVersion-$forgeVersion")
    implementation("thedarkcolour:kotlinforforge:4.5.0")
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}