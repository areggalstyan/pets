import kr.entree.spigradle.kotlin.spigot

plugins {
    java
    id("kr.entree.spigradle") version "2.4.3"
}

group = "com.aregcraft"
version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(spigot("1.19.3"))
    compileOnly("com.mojang:authlib:1.5.25")
}

tasks.register("debugPlugin") {
    file("build/libs").walk().filter { it.extension == "jar" }.forEach { it.delete() }
    file("debug/spigot/plugins").walk().filter { it.extension == "jar" }.forEach { it.delete() }
    file("debug/spigot/plugins/Pets").deleteRecursively()
    tasks["runSpigot"].mustRunAfter(tasks["prepareSpigotPlugins"])
    finalizedBy(tasks["prepareSpigotPlugins"])
    finalizedBy(tasks["runSpigot"])
}

spigot {
    name = "Pets"
    apiVersion = "1.19"
    commands {
        create("pets") {
            description = "Opens the pets menu."
            usage = "Usage: /<command>"
            permission = "pets.command.pets"
        }
        create("showpets") {
            description = "Makes your pets visible."
            usage = "Usage: /<command>"
            permission = "pets.command.showpets"
        }
        create("hidepets") {
            description = "Makes your pets invisible."
            usage = "Usage: /<command>"
            permission = "pets.command.hidepets"
        }
        create("clearpets") {
            description = "Removes all of your pets."
            usage = "Usage: /<command>"
            permission = "pets.command.clearpets"
        }
        create("reloadpets") {
            description = "Reloads the configuration files."
            usage = "Usage: /<command>"
            permission = "pets.command.reloadpets"
        }
    }
}
