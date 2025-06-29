# Multi-stage build for API Gateway Service
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Install maven
RUN apt-get update && apt-get install -y maven

# Copy parent POM first
COPY pom.xml ./pom.xml

# Copy service POM and source
COPY api-gateway-service/pom.xml ./api-gateway-service/pom.xml
COPY api-gateway-service/src ./api-gateway-service/src

# Build the application
WORKDIR /app/api-gateway-service
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built jar
COPY --from=builder /app/api-gateway-service/target/api-gateway-service-*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
