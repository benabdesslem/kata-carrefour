FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/deliver-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "deliver-0.0.1-SNAPSHOT.jar"]