
plugins {
    `java-library`
}

group = project.property("group")!!
version = project.property("version")!!

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven {
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    maven {
        setUrl("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        setUrl("https://ci.ender.zone/plugin/repository/everything/")
    }
    maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.15.2-R0.1-SNAPSHOT")
    implementation("net.ess3:EssentialsX:2.17.2")
    implementation("org.jetbrains:annotations:16.0.2")
}

tasks {
    jar {
        manifest {
            attributes(
                    mapOf(
                            "Built-By" to project.property("author")!!,
                            "Implementation-Title" to project.name,
                            "Implementation-Version" to project.version
                    )
            )
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
}

tasks.create<Delete>("deleteOldJarOnDevServer") {
    description = "Deletes old jar binaries from test server."
    group = "Deployment"

    delete(
        fileTree("C:\\Users\\nowya\\Desktop\\PBE\\1.15.2\\plugins") {
            include("**/*PVPControl-*.jar")
        }
    )
}

tasks.create<Delete>("deleteOldPluginDirOnDevServer") {
    description = "Deletes old plugin directory from test server."
    group = "Deployment"

    delete("C:\\Users\\nowya\\Desktop\\PBE\\1.15.2\\plugins\\PVPControl")
}

tasks.create<Copy>("copyNewJarToDevServer") {
    description = "Copies binary jar to test server."
    group = "Deployment"

    from("build/libs")
    into("C:\\Users\\nowya\\Desktop\\PBE\\1.15.2\\plugins")

    mustRunAfter("deleteOldJarOnDevServer")
}

tasks.register("deployToDevServer") {
    dependsOn("deleteOldJarOnDevServer")
    dependsOn("copyNewJarToDevServer")
}

tasks.register("deployToDevServer-fresh") {
    dependsOn("deployToDevServer")
    dependsOn("deleteOldPluginDirOnDevServer")
}

tasks.register<Exec>("startDevServer") {
    dependsOn("deployToDevServer")
    group = "Deployment"
    workingDir("C:\\Users\\nowya\\Desktop\\PBE\\1.15.2")
    commandLine("cmd", "/c", "start.bat")
    isIgnoreExitValue = true
}