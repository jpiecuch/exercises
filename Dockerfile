FROM openjdk:8
WORKDIR /app
COPY target/exercises-*-fat.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]