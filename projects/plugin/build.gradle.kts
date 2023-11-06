import com.github.jengelman.gradle.plugins.shadow.relocation.Relocator
import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.gmail.nowyarek.gradle.tasks.Deploy
import java.nio.file.Paths

// Specify Gradle plugins
plugins {
    id("com.gmail.nowyarek.gradle.plugins.java-plugin")
    id("com.github.johnrengelman.shadow").version("8.1.1")
}

group = "com.gmail.nowyarek.pvpcontrol"

// Include respositories
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
        setUrl("https://repo.essentialsx.net/snapshots")
    }
}

// List dependencies
dependencies {
    /* Available at runtime classpath (shaded by Bukkit/CraftBukkit) */
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    // google/guava (provided by spigot)
    compileOnly("com.google.guava:guava:32.1.2-jre")
    // google/jsr305 (provided by spigot -> google/guava)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    /* Own dependencies, shaded into fat-jar */
    // Internal API library
    implementation(project(":projects:api"))
    // Aikar/TaskChain
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    // google/guice
    implementation("com.google.inject:guice:7.0.0")
    // jakarta/inject-api
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

    /* Optional dependencies on 3rd party plugins */
    // EssentialsX
    compileOnly("net.essentialsx:EssentialsX:2.21.0-SNAPSHOT")

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
    archiveBaseName.set(rootProject.name)
    archiveFileName.set("${archiveBaseName.get()}-${project.version}.${archiveExtension.get()}")

    manifest.attributes.putAll(
        mapOf(
            "Built-By" to project.property("author"),
            "Implementation-Title" to rootProject.name,
            "Implementation-Version" to project.version
        )
    )
}

// Configure fat Jar file
tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set(tasks.getByName<Jar>("jar").archiveBaseName.get())
    archiveFileName.set(tasks.getByName<Jar>("jar").archiveFileName.get())

    dependencies {
        include(project(":projects:api"))
        include(dependency("co.aikar:taskchain-bukkit"))
        include(dependency("co.aikar:taskchain-core"))
        include(dependency("com.google.inject:guice"))
        include(dependency("jakarta.inject:jakarta.inject-api"))
        include(dependency("aopalliance:aopalliance"))
        exclude("META-INF/**/*")
    }

    relocators = listOf<Relocator>(
        SimpleRelocator(
            "co.aikar.taskchain",
            "com.gmail.nowyarek.pvpcontrol.relocation.co.aikar.taskchain",
            null,
            null
        ),
        SimpleRelocator(
            "com.google.inject",
            "com.gmail.nowyarek.pvpcontrol.relocation.com.google.inject",
            null,
            null
        ),
        SimpleRelocator(
            "jakarta.inject",
            "com.gmail.nowyarek.pvpcontrol.relocation.jakarta.inject",
            null,
            null
        ),
        SimpleRelocator(
            "org.aopalliance",
            "com.gmail.nowyarek.pvpcontrol.relocation.org.aopalliance",
            null,
            null
        ),
    )

    mergeServiceFiles()
}

// Explicitly define `shadowJar` task dependency
tasks.named<DefaultTask>("assemble") {
    dependsOn("shadowJar")
}

// Deployment
tasks.named<Deploy>("deploy") {
    serverDirectoryPath.set(Paths.get(env.fetch("DEV_SERVER_PATH")))
}