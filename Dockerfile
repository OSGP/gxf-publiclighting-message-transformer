FROM alpine/java:21-jre

WORKDIR /app

COPY build/libs/gxf-publiclighting-message-transformer.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
