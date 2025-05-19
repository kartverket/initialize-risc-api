val kotlinVersion = "2.1.21"
val ktorVersion = "3.1.3"
val logbackVersion = "1.5.18"
val nettyHandlerVersion = "4.1.119.Final"
val junitVersion = "5.12.2"
val mockkVersion = "1.14.2"

plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.20"
    id("io.ktor.plugin") version "3.1.3"
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
}

// Oppgradeer til siste versjon av ktlint fordi org.jlleitschuh.gradle.ktlint version 12.1.2 bruker for gammel versjon
// Slett når ktlint-plugin'en oppdateres til nyere versjon
ktlint {
    version.set("1.5.0")
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
