// Build-конфіг сервісу payments-service.
// Поточний стан: dependency graph не зафіксований, версії тягнуться транзитивно.
plugins {
    kotlin("jvm") version "1.9.24"
    application
}

group = "com.acme.payments"
version = "0.7.0"

repositories {
    mavenCentral()
}

dependencyLocking {
    lockAllConfigurations()
}

dependencies {
    // framework layer: міграція Spring Boot 2.7 -> 3.x ще не виконана
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.18")

    // dependency layer: проблемна транзитивна залежність, версія зафіксована явно
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.4")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("com.acme.payments.AppKt")
}

// runtime layer: ціль — Java 21, але toolchain поки не задано жорстко
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}