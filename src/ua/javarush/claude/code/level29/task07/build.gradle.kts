// Конфіг збірки сервісу payments-service.
// Поточний стан: граф залежностей не зафіксований, версії тягнуться транзитивно.
plugins {
    kotlin("jvm") version "1.9.24"
    application
}

group = "com.acme.payments"
version = "0.7.0"

repositories {
    mavenCentral()
}

dependencies {
    // шар фреймворку: міграцію Spring Boot 2.7 -> 3.x ще не виконано
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.18")

    // шар залежностей: проблемна транзитивна залежність, версія не закріплена
    implementation("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("com.acme.payments.AppKt")
}

// шар виконання: цілімося в Java 21, але toolchain поки не задано жорстко
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}