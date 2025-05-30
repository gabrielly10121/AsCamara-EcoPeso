FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
RUN ls -l AsCamara-Gestao/target/
RUN find /app -name "*.jar"
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/AsCamara-Gestao/target/AsCamara-Gestao.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]