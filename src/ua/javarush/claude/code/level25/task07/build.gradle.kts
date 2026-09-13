plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.rush.subscription"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}