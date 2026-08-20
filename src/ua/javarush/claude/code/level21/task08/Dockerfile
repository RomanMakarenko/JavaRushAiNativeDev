# Образ для order-service: кладемо готовий Spring Boot jar і запускаємо його.
FROM eclipse-temurin:17-jre

WORKDIR /opt/service

# Копіюємо реальний артефакт Gradle bootJar з build/libs у поточний WORKDIR.
COPY build/libs/order-service-0.0.1.jar app.jar

EXPOSE 8080

# Запускаємо Spring Boot jar із тієї самої WORKDIR.
ENTRYPOINT ["java", "-jar", "app.jar"]