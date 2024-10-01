plugins {
    id("java-library")
    id("kotlin")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}