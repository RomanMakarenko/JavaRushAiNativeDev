plugins {
    java
    application
}

group = "com.example.commerce"
version = "1.4.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.example.commerce.CommerceApp")
}
