# Образ для order-service: кладемо готовий Spring Boot jar і запускаємо його.
FROM eclipse-temurin:17-jre

WORKDIR /opt/service

# ПОМИЛКА: jar копіюється з неіснуючого шляху target/ (це шлях Maven, а не Gradle),
# а ім'я артефакту не збігається з тим, що збирає bootJar.
COPY target/order-service.jar /app/app.jar

EXPOSE 8080

# ПОМИЛКА: запускаємо jar за шляхом /app/app.jar, який не лежить у WORKDIR /opt/service.
ENTRYPOINT ["java", "-jar", "app.jar"]