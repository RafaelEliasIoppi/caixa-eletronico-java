# Etapa de build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de execução
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Copia os arquivos do banco de dados
COPY --from=build /app/data /app/data

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
