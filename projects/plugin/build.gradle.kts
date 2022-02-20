
import com.github.jengelman.gradle.plugins.shadow.relocation.Relocator
import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.gmail.nowyarek.gradle.tasks.Deploy.Deploy
import java.nio.file.Path
import java.nio.file.Paths

// Specify Gradle plugins
plugins {
    id("com.gmail.nowyarek.gradle.plugins.java-plugin")
    id("com.github.johnrengelman.shadow").version("7.1.2")
}

// Include repositires
repositories {
    // General repos
    mavenCentral()
    // Gradle Central Plugin Repository
    gradlePluginPortal()
    // Bukkit & Spigot
    maven {
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    /*
     * As Spigot-API depends on the Bungeecord ChatComponent-API,
     * we need to add the Sonatype OSS repository
     */
    maven {
        setUrl("https://oss.sonatype.org/content/repositories/snapshots")
    }
    // Aikar/TaskChain
    maven {
        setUrl("https://repo.aikar.co/content/groups/aikar")
    }
    // EssentialsX
    maven {
        setUrl("https://repo.essentialsx.net/releases")
    }
}

// List dependencies
dependencies {
    /* Available at runtime classpath (shaded by Bukkit/CraftBukkit) */
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.18.1-R0.1-SNAPSHOT")
    // google/guava
    compileOnly("com.google.guava:guava:31.0.1-jre")

    /* Own dependencies, shaded into fat-jar */
    // Internal API library
    implementation(project(":projects:api"))
    // Aikar/TaskChain
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    // google/guice
    implementation("com.google.inject:guice:5.0.1")

    /* Optional dependencies on 3rd party plugins */
    // EssentialsX
    compileOnly("net.essentialsx:EssentialsX:2.19.2")

    /* Testing libararies */
    // API against which we are writing tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    // An implementation of the junit-platform-engine API that runs JUnit 5 tests.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

/********** TASKS **********/

// Replace variables in resources
tasks.named<ProcessResources>("processResources") {
    filter(org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf("PLUGIN_VERSION" to project.version))
}

// Configure Fat-Jar
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(rootProject.name)

    dependencies {
        include(project(":projects:api"))
        include(dependency("co.aikar:taskchain-bukkit"))
        include(dependency("co.aikar:taskchain-core"))
        include(dependency("com.google.inject:guice"))
        include(dependency("aopalliance:aopalliance"))
        exclude("META-INF/**/*")
    }

    relocators = listOf<Relocator>(
        SimpleRelocator(
            "co.aikar.taskchain",
            "com.gmail.nowyarek.pvpcontrol.relocation.co.aikar.taskchain",
            ArrayList<String>(),
            ArrayList<String>()
        ),
        SimpleRelocator(
            "com.google.inject",
            "com.gmail.nowyarek.pvpcontrol.relocation.com.google.inject",
            ArrayList<String>(),
            ArrayList<String>()
        ),
        SimpleRelocator(
            "org.aopalliance",
            "com.gmail.nowyarek.pvpcontrol.relocation.org.aopalliance",
            ArrayList<String>(),
            ArrayList<String>()
        ),
    )

    mergeServiceFiles()

    manifest {
        attributes(
            mapOf(
                "Built-By" to project.property("author"),
                "Implementation-Title" to rootProject.name,
                "Implementation-Version" to project.version
            )
        )
    }
}

// Explicitly define `shadowJar` task dependency
tasks.named<DefaultTask>("assemble") {
    dependsOn("shadowJar")
}

/* Deployment */

tasks.named<Deploy>("deploy") {
    val developmentServerPath: Path = Paths.get(env.fetch("DEV_SERVER_PATH"))

    serverDirectoryPath.set(developmentServerPath)
    archiveFile.set(tasks.named<ShadowJar>("shadowJar").get().archiveFile)
    archiveBaseName.set("${rootProject.name}-${project.name}")
}