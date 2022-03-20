package com.gmail.nowyarek.gradle.plugins

import com.gmail.nowyarek.gradle.tasks.Deploy.Deploy
import java.nio.file.Paths

// Specify Gradle plugins
plugins {
    id("com.gmail.nowyarek.gradle.plugins.java-library")
    id("co.uzzu.dotenv.gradle")
}

/* Deployment */

val deployTask = tasks.register<Deploy>("deploy") {
    description = "Takes care of deployment process."
    group = "deployment"
    archiveFile.set(tasks.named<Jar>("jar").get().archiveFile)
    archiveBaseName.set(tasks.named<Jar>("jar").get().archiveBaseName)

    dependsOn("copyNewArtifact")
}

tasks.create<Delete>("deleteOldArtifact") {
    description = "Deletes old jar binary from dev server location."
    group = "deployment"

    delete(
        providers.provider {
            fileTree(Paths.get(deployTask.get().serverDirectoryPath.get().toAbsolutePath().toString(), "plugins")) {
                include(deployTask.get().archiveBaseName.get() + "*.jar")
            }
        }
    )
}

tasks.create<Copy>("copyNewArtifact") {
    description = "Copies new jar binary to dev server location."
    group = "deployment"

    from(providers.provider { deployTask.get().archiveFile.get().asFile.absolutePath })
    into(providers.provider { Paths.get(deployTask.get().serverDirectoryPath.get().toAbsolutePath().toString(), "plugins") })
    rename { deployTask.get().archiveBaseName.get() + ".jar" }

    dependsOn("build", "deleteOldArtifact")
}