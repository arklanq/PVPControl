import com.gmail.nowyarek.gradle.tasks.Deploy
import java.nio.file.Paths

// Specify Gradle plugins
plugins {
    id("com.gmail.nowyarek.gradle.plugins.java-plugin")
}

group = "com.example.plugin_consumer.pvpcontrol"

// Include repositires
repositories {
    // General repos
    mavenCentral()
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
}

// List dependencies
dependencies {
    /* Available at runtime classpath (shaded by Bukkit/CraftBukkit) */
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    // API we are actually testing
    compileOnly(project(":projects:api"))

    /* Testing libararies */
    // API against which we are writing tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    // An implementation of the junit-platform-engine API that runs JUnit 5 tests.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

/********** TASKS **********/

// Replace variables in resources
tasks.named<ProcessResources>("processResources") {
    filter(
        org.apache.tools.ant.filters.ReplaceTokens::class,
        "tokens" to mapOf(
            "PLUGIN_VERSION" to project.version,
            "PLUGIN_AUTHOR" to project.property("author")
        )
    )
}

// Configure base Jar file
tasks.named<Jar>("jar") {
    archiveBaseName.set("${rootProject.name}--${project.name}")
    archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}

// Deployment
tasks.named<Deploy>("deploy") {
    serverDirectoryPath.set(Paths.get(env.fetch("DEV_SERVER_PATH")))
}