FROM openjdk:17-jdk-slim
VOLUME /tmp

ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} laboticademar.war

EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}","-jar","/laboticademar.war"]