package com.gmail.nowyarek.gradle.plugins

// Specify Gradle plugins
plugins {
    id("java-library")
}

// Define project properties
group = project.property("group").toString()
version = project.property("version").toString()

// Configure Java
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("${rootProject.name}-${project.name}")
}

// Configure built-in tasks from `java-library` plugin
tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}