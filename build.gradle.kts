plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.aregcraft.delta.plugin") version "1.0.0"
}

group = "com.aregcraft"
version = "3.0.0"

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
    implementation("com.aregcraft.delta:api:1.0.0")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.0.7")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

pluginDescription {
    main.set("$group.pets.Pets")
    apiVersion.set("1.19")
    commands {
        create("pets") {
            description.set("Opens the pet menu.")
            usage.set("Usage: /<command>")
            permission.set("pets.command.pets")
        }
        create("togglepets") {
            description.set("Toggles the visibility of your pets.")
            usage.set("Usage: /<command>")
            permission.set("pets.command.togglepets")
        }
        create("reloadpets") {
            description.set("Reloads the configuration files.")
            usage.set("Usage: /<command>")
            permission.set("pets.command.reloadpets")
        }
    }
}

tasks.shadowJar {
    relocate("org.bstats", "${project.group}.reforging")
    relocate("org.aregcraft.delta", "${project.group}.reforging")
}
