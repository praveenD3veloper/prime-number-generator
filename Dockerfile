
# Command to run the Spring Boot application
# Use Google Cloud's preferred Java 21 image - distroless for maximum security with busybox shell
FROM gcr.io/distroless/java21-debian12:debug-nonroot

## Set the working directory inside the container
#WORKDIR /app

# Copy the compiled Spring Boot JAR file into the container
COPY target/prime-number-generator-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.address=0.0.0.0", "-Dserver.port=8080", "-jar", "app.jar"]

