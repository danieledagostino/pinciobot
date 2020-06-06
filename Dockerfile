FROM openjdk:8u171-jre-alpine
ARG JAR_FILE=target/*.jar
ARG apikey=802d8a6e-cec0-45cd-a2f6-457bcc5bb47a
ENV CLOUDAMQP_APIKEY=$apikey
ARG url=amqp://ggefxdli:bW142-F9BheYeb3DAU5B1SUGZw2n7qnN@squid.rmq.cloudamqp.com/ggefxdli
ENV CLOUDAMQP_URL=$url
ARG token=1158157085:AAH2vDu2HYu1qzQ9ScCgABNZ4h9unRF3wMY
ENV PINCIO_BOT_TOKEN=$token
ARG elastic=http://localhost:8854/
ENV ELASTIC_SERVICE=$elastic
ARG persistent=http://localhost:8853/
ENV ELASTIC_SERVICE=$persistent
COPY ${JAR_FILE} bot.jar
ENTRYPOINT ["java","-jar","/bot.jar"]