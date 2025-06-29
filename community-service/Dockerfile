# Multi-stage build for Community Service
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app
# Install maven
RUN apt-get update && apt-get install -y maven

# Copy parent POM first
COPY pom.xml ./pom.xml

# Copy service POM and source
COPY community-service/pom.xml ./community-service/pom.xml
COPY community-service/src ./community-service/src

# Build the application
WORKDIR /app/community-service
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app


# Create non-root community
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built jar
COPY --from=builder /app/community-service/target/community-service-*.jar app.jar

# Change ownership
RUN chown spring:spring app.jar

USER spring

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
