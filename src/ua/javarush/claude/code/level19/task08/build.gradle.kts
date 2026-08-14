plugins {
    java
}

group = "com.acme.commerce"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Лише JUnit 5 — жодних додаткових тестових бібліотек для цієї задачі.
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}