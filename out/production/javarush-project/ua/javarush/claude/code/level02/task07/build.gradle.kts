plugins {
    java
    application
}

group = "com.rush.billing"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass.set("com.rush.billing.BillingApp")
}

tasks.test {
    useJUnitPlatform()
}