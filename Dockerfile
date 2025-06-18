# Stage 1: Build con Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copia sólo el POM para cachear dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código y empaqueta el WAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime minimal con la JRE
FROM eclipse-temurin:17-jdk-slim
WORKDIR /app

# Copia el WAR generado (ajusta el patrón si tu artefacto cambia de nombre)
COPY --from=build /workspace/target/*.war laboticademar.war

EXPOSE 8080
ENTRYPOINT ["java","-jar","laboticademar.war"]
