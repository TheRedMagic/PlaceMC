import com.undefinedcreation.runServer.ServerType

plugins {
    java
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.undefinedcreation.runServer") version "0.0.1"
}

group = "com.redmagic"
version = "1.0"

val minecraftVersion = "1.21"

repositories {
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "undefinedRepo"
        url = uri("https://repo.undefinedcreation.com/repo")
    }
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}


dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.undefined:api:0.5.56:mapped")
}

tasks {
    shadowJar {
        archiveFileName.set("${this.project.name}-shadow.jar")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
    runServer {
        mcVersion(minecraftVersion)

        serverType(ServerType.PAPERMC)

        acceptMojangEula(true)
    }
}

kotlin {
    jvmToolchain(21)
}