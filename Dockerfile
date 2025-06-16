FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} laboticademar_webapp.war
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}","-jar","/laboticademar_webapp.war"]
