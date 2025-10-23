val kotlinVersion = "2.2.20"
val ktorVersion = "3.3.0"
val logbackVersion = "1.5.20"
val nettyHandlerVersion = "4.2.7.Final"
val junitVersion = "5.13.4"
val mockkVersion = "1.14.5"

plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.serialization") version "2.2.10"
    id("io.ktor.plugin") version "3.3.0"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
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
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "io.netty" && requested.name == "netty-codec-http2") {
            useVersion("4.2.4.Final") // Sårbarhet i io.netty:netty-codec-http2. 2025-08-20
        }
    }
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

    constraints {
        implementation("org.apache.commons:commons-lang3:3.19.0") {
            because("Force secure version to fix CVE in transitive dependency from spring-boot-gradle-plugin")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
