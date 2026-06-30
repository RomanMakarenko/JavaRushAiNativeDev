// Збірка модуля analytics.
plugins {
    java
}

group = "com.acme.analytics"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // BUG: AnalyticsController використовує тип com.acme.support.api.TicketClient
    // із модуля support-api, але залежність на модуль тут не оголошена.
    // Через це збірка модуля падає під час компіляції.
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}