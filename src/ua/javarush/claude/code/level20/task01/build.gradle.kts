// Конфігурація збірки backend-сервісу orders-service.
// Використовується Gradle Kotlin DSL. Запуск завдань — через ./gradlew <task>.

plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    // Spotless забезпечує форматування Java-коду
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.example"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Форматування: ./gradlew spotlessApply застосовує формат,
// ./gradlew spotlessCheck лише перевіряє (використовується як швидкий сенсор).
spotless {
    java {
        googleJavaFormat()
        target("src/**/*.java")
    }
}

// Backend-тести запускаються через ./gradlew test.
tasks.test {
    useJUnitPlatform()
}