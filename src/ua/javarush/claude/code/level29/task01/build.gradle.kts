plugins {
    java
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.cashflow"
version = "1.0.0"

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

tasks.withType<Test> {
    useJUnitPlatform()
}