# Multi-stage build for Event Service
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Install maven
RUN apt-get update && apt-get install -y maven

# Copy parent POM first
COPY pom.xml ./pom.xml

# Copy service POM and source
COPY event-service/pom.xml ./event-service/pom.xml
COPY event-service/src ./event-service/src

# Build the application
WORKDIR /app/event-service
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built jar
COPY --from=builder /app/event-service/target/event-service-*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

USER spring

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
