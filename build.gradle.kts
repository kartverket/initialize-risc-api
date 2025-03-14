val kotlinVersion = "2.0.21"
val ktorVersion = "3.0.1"
val logbackVersion = "1.3.12"
val jacksonVersion = "2.18.1"
val nettyHandlerVersion = "4.1.115.Final"

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "1.5.0"
    id("io.ktor.plugin") version "3.0.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "kartverket.no"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-request-validation:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.netty:netty-handler:$nettyHandlerVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
