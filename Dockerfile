FROM openjdk:8u171-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bot.jar
ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","/bot.jar"]