# Etapa de build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de execução
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/caixa-eletronico-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
