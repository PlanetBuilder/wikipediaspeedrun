plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "de.planetbuilder"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.http4k:http4k-client-apache:4.41.4.0")
    implementation("org.http4k:http4k-contract:4.41.4.0")
    implementation("org.http4k:http4k-core:4.41.4.0")
    implementation("org.http4k:http4k-format-gson:4.41.4.0")
    implementation("org.http4k:http4k-server-jetty:4.41.4.0")
    implementation("org.http4k:http4k-template-handlebars:4.41.4.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}