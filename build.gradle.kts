import com.github.jengelman.gradle.plugins.shadow.relocation.Relocator
import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val versionProperty: Any? = project.property("version")
version = if (versionProperty is String && versionProperty !== "unspecified") versionProperty else "dev"

// Specify Gradle plugins
plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("co.uzzu.dotenv.gradle").version("1.1.0")
}

// Configure Java
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
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
        setUrl("https://ci.ender.zone/plugin/repository/everything/")
    }
}

// List dependencies
dependencies {
    // Spigot API
    implementation("org.spigotmc:spigot-api:1.18.1-R0.1-SNAPSHOT")
    // EssentialsX
    implementation("net.ess3:EssentialsX:2.18.2")
    // Aikar/TaskChain
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    // google/guice
    implementation("com.google.inject:guice:5.0.1")
    // API against which we are writting tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    // An implementation of the junit-platform-engine API that runs JUnit 5 tests.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

val devServerPath: String = env.fetch("DEV_SERVER_PATH")

/********** TASKS **********/

/* Build */

tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.named<ProcessResources>("processResources") {
    filter(org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf("PLUGIN_VERSION" to project.version))
}

tasks.named<ShadowJar>("shadowJar") {
    dependencies {
        include(dependency("co.aikar:taskchain-bukkit"))
        include(dependency("co.aikar:taskchain-core"))
        include(dependency("com.google.inject:guice"))
        include(dependency("aopalliance:aopalliance"))
        exclude("META-INF/**/*")
    }

    relocators = listOf<Relocator>(
        SimpleRelocator("co.aikar.taskchain", "com.gmail.nowyarek.pvpcontrol.relocation.co.aikar.taskchain", ArrayList<String>(), ArrayList<String>()),
        SimpleRelocator("com.google.inject", "com.gmail.nowyarek.pvpcontrol.relocation.com.google.inject", ArrayList<String>(), ArrayList<String>()),
        SimpleRelocator("org.aopalliance", "com.gmail.nowyarek.pvpcontrol.relocation.org.aopalliance", ArrayList<String>(), ArrayList<String>()),
    )

    mergeServiceFiles()

    manifest {
        attributes(
            mapOf(
                "Built-By" to project.property("author"),
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
}

tasks.named<DefaultTask>("build") {
    dependsOn("shadowJar")
}

/* Deployment */

tasks.create<Delete>("deleteOldArtifact") {
    description = "Deletes old jar binary from dev server location."
    group = "Deployment"

    delete(fileTree("$devServerPath${File.separator}plugins") {
        include("**/*" + project.name + "*.jar")
    })
}

tasks.create<Delete>("deletePluginDir") {
    description = "Deletes old plugin directory from dev server location."
    group = "Deployment"

    delete("$devServerPath${File.separator}plugins${File.separator}" + project.name)
}

tasks.create<Copy>("copyNewArtifact") {
    description = "Copies new jar binary to dev server location."
    group = "Deployment"

    from(tasks.shadowJar.get().archiveFile.get().asFile.absolutePath)
    into("$devServerPath${File.separator}plugins")
    rename { it.dropLast(8) + ".jar" }

    dependsOn("build", "deleteOldArtifact")
}

tasks.register("deploy") {
    description = "Uploads a new artifact to the dev server."
    group = "Deployment"

    dependsOn("copyNewArtifact")
}