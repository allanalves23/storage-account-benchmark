# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY src/main/resources/static  /app/src/main/resources/static
COPY target/*.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]