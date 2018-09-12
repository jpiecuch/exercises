FROM openjdk:8
WORKDIR /app
COPY target/exercises-*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]