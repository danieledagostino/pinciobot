FROM openjdk:8u171-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bot-client.jar
ENTRYPOINT ["java","-jar","/bot-client.jar"]