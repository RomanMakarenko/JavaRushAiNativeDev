// Збірка мінімального Spring Boot сервісу order-service.
// Створює виконуваний "fat jar" у build/libs/order-service-0.0.1.jar.
plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.shop"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
}

// Фіксуємо стабільне ім'я підсумкового jar для Dockerfile і скриптів.
tasks.bootJar {
    archiveFileName.set("order-service-0.0.1.jar")
}