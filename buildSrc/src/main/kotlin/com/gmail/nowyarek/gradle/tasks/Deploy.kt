package com.gmail.nowyarek.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import java.nio.file.Path

abstract class Deploy : DefaultTask() {

    @get:Input
    abstract val serverDirectoryPath: Property<Path>

    @get:InputFile
    abstract val archiveFile: RegularFileProperty

}