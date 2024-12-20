plugins {
    base
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.10"
    `maven-publish`

    // Linter
    id("org.jmailen.kotlinter") version "4.5.0"
}

group = "sh.nemo"
version = "0.5.0"

repositories {
    mavenCentral()
}

dependencies {
    api(platform("io.ktor:ktor-bom:3.0.1"))

    api("io.ktor:ktor-client-core")
    api("io.ktor:ktor-client-cio")
    api("io.ktor:ktor-client-content-negotiation")
    api("io.ktor:ktor-serialization-kotlinx-json")
    api("io.ktor:ktor-client-logging")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    api("ch.qos.logback:logback-classic:1.5.14")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.1")
    testImplementation("org.testcontainers:testcontainers:1.20.4")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask::class).all {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")

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
