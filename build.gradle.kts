plugins {
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
}

val modId: String by extra.properties
val modVersion: String by extra.properties
val modGroupId: String by extra.properties

version = modVersion
group = modGroupId

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

minecraft {
    mappings(extra.properties["mapping_channel"] as String, extra.properties["mapping_version"] as String)
    
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
    minecraft(
        "net.minecraftforge:forge:${extra.properties["minecraft_version"]}-${extra.properties["forge_version"]}"
    )
    implementation("thedarkcolour:kotlinforforge:4.5.0")
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}