package com.gmail.nowyarek.gradle.plugins

// Specify Gradle plugins
plugins {
    id("java-library")
}

// Define project properties
group = project.properties["group"] as String
version = project.properties["version"] as String

// Configure Java
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

// Configure built-in tasks from `java-library` plugin
tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

// Run tests using JUnit Platform
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}