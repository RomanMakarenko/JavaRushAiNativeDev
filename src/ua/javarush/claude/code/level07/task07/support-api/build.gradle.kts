// Збірка модуля support-api: клієнт тикет-системи підтримки.
plugins {
    java
}

group = "com.acme.support.api"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}