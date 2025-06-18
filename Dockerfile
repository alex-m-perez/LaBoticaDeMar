FROM eclipse-temurin:17-jdk-slim
WORKDIR /app

# Copiamos el .war generado por el workflow
COPY target/laboticademar.war laboticademar.war

EXPOSE 8080
ENTRYPOINT ["java","-jar","laboticademar.war"]
