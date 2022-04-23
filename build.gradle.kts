import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    `maven-publish`

    // Linter
    id("org.jmailen.kotlinter") version "3.10.0"
}

group = "sh.nemo"
version = "0.2.1"

repositories {
    mavenCentral()
}

dependencies {
    api(platform("io.ktor:ktor-bom:2.0.0"))

    api("io.ktor:ktor-client-core")
    api("io.ktor:ktor-client-cio")
    api("io.ktor:ktor-client-content-negotiation")
    api("io.ktor:ktor-serialization-kotlinx-json")
    api("io.ktor:ktor-client-logging")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
    api("ch.qos.logback:logback-classic:1.2.11")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.testcontainers:testcontainers:1.17.1")
    testImplementation("org.testcontainers:junit-jupiter:1.17.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}
