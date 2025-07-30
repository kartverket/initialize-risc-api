val kotlinVersion = "2.2.0"
val ktorVersion = "3.2.2"
val logbackVersion = "1.5.18"
val nettyHandlerVersion = "4.2.3.Final"
val junitVersion = "5.13.2"
val mockkVersion = "1.14.5"

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
    id("io.ktor.plugin") version "3.2.3"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

// Specifies the usage of the currently newest version of Ktlint. `org.jlleitschuh.gradle.ktlint` version 12.2.0 is
// not guaranteed to use the newest version available.
ktlint {
    version.set("1.6.0")
}

group = "kartverket.no"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(23)
}

java {
    sourceCompatibility = JavaVersion.VERSION_23
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
    implementation("io.ktor:ktor-server-config-yaml")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.netty:netty-handler:$nettyHandlerVersion")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion")) {
        because("The BOM (bill of materials) provides correct versions for all JUnit libraries used.")
    }
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks.test {
    useJUnitPlatform()
}
