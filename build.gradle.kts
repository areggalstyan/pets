plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.aregcraft.delta.plugin") version "1.0.0"
}

group = "com.aregcraft"
version = "4.8.0"

repositories {
    mavenLocal()
}

dependencies {
    compileOnly("com.aregcraft.delta:meta:1.0.0")
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    implementation("com.aregcraft.delta:api:1.0.0")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("org.bstats:bstats-bukkit:3.0.1")
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
        create("petsinfo") {
            aliases.add("pi")
            description.set("Shows information about available pets, stats, and crafting recipes.")
            permission.set("pets.command.petsinfo")
        }
        create("petsgive") {
            description.set("Gives you or the player a pet, experience booster, or candy.")
            usage.set("Usage: /<command> <id>")
            permission.set("pets.command.petsgive")
        }
        create("clearpets") {
            description.set("Removes all of your pets.")
            usage.set("Usage: /<command>")
            permission.set("pets.command.clearpets")
        }
        create("updatepets") {
            description.set("Updates the plugin.")
            usage.set("Usage: /<command>")
            permission.set("pets.command.updatepets")
        }
        create("petslang") {
            description.set("Changes the language of the plugin.")
            usage.set("Usage: /<command> <language>")
            permission.set("pets.command.petslang")
        }
    }
    permissions {
        create("pets.command.pets") {
            description.set("Allows you to use the command /pets")
            default.set("true")
        }
        create("pets.command.togglepets") {
            description.set("Allows you to use the command /togglepets")
            default.set("true")
        }
        create("pets.command.reloadpets") {
            description.set("Allows you to use the command /reloadpets")
        }
        create("pets.command.petsinfo") {
            description.set("Allows you to use the command /petsinfo")
            default.set("true")
        }
        create("pets.command.petsgive") {
            description.set("Allows you to use the command /petsgive")
        }
        create("pets.command.clearpets") {
            description.set("Allows you to use the command /clearpets")
            default.set("true")
        }
        create("pets.command.updatepets") {
            description.set("Allows you to use the command /updatepets")
        }
        create("pets.command.petslang") {
            description.set("Allows you to use the command /petslang")
        }
    }
}

tasks.shadowJar {
    relocate("org.bstats", "${project.group}.reforging")
    relocate("org.aregcraft.delta", "${project.group}.reforging")
}

tasks.register<Javadoc>("generateMeta") {
    dependsOn(project(":meta").tasks["shadowJar"])
    source = sourceSets.main.get().allJava
    classpath = sourceSets.main.get().compileClasspath
    options.destinationDirectory(projectDir)
    options.docletpath(project(":meta").tasks.getByName<Jar>("shadowJar").archiveFile.get().asFile)
    options.doclet("${project.group}.pets.meta.PetsMeta")
    (options as StandardJavadocDocletOptions).addStringOption("version", version.toString())
    outputs.upToDateWhen { false }
}
