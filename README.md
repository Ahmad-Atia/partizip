# Partizip Microservices Architecture

A Spring Boot microservices architecture project demonstrating a distributed system using Docker, Docker Compose, and Spring Cloud Gateway.

## 🏗️ Project Structure

```
partizip/
├── pom.xml                     # Parent POM
├── docker-compose.yml          # Docker Compose configuration
├── README.md                   # This file
├── .gitignore                  # Git ignore rules
├── api-gateway-service/        # API Gateway (Port: 8080)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/partizip/gateway/
├── community-service/          # Community Service (Port: 8081)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/partizip/community/
├── event-service/              # Event Service (Port: 8082)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/partizip/event/
└── user-service/               # User Service (Port: 8083)
    ├── pom.xml
    ├── Dockerfile
    └── src/main/java/com/partizip/user/
```

## 🚀 Technologies Used

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Cloud Gateway** - API Gateway
- **Spring Web** - REST API framework
- **Spring Boot Actuator** - Health checks and monitoring
- **Maven** - Build tool and dependency management
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## 📋 Prerequisites

- Java 17 or later
- Maven 3.6+ (or use the Maven wrapper)
- Docker and Docker Compose
- Git (optional)

## 🔧 Building the Project

### Option 1: Using Maven (Local Build)

```powershell
# Build all services
mvn clean package

# Build individual service
cd community-service
mvn clean package
```

### Option 2: Using Docker Compose (Recommended)

```powershell
# Build and start all services
docker-compose up --build

# Build without starting
docker-compose build
```

## 🏃‍♂️ Running the Application

### Using Docker Compose (Recommended)

```powershell
# Start all services in background
docker-compose up -d

# Start with logs
docker-compose up

# Stop all services
docker-compose down
```

### Running Individual Services Locally

```powershell
# Start API Gateway
cd api-gateway-service
mvn spring-boot:run

# Start Community Service (in new terminal)
cd community-service
mvn spring-boot:run

# Start Event Service (in new terminal)
cd event-service
mvn spring-boot:run

# Start User Service (in new terminal)
cd user-service
mvn spring-boot:run
```

## 🌐 API Endpoints

### Direct Service Access

| Service | Port | Health Check | Hello Endpoint |
|---------|------|--------------|----------------|
| API Gateway | 8080 | http://localhost:8080/actuator/health | - |
| Community Service | 8081 | http://localhost:8081/actuator/health | http://localhost:8081/hello |
| Event Service | 8082 | http://localhost:8082/actuator/health | http://localhost:8082/hello |
| User Service | 8083 | http://localhost:8083/actuator/health | http://localhost:8083/hello |

### Through API Gateway

| Endpoint | Target Service | URL |
|----------|----------------|-----|
| `/api/community/hello` | Community Service | http://localhost:8080/api/community/hello |
| `/api/events/hello` | Event Service | http://localhost:8080/api/events/hello |
| `/api/users/hello` | User Service | http://localhost:8080/api/users/hello |

## 🧪 Testing the Services

### Test Individual Services

```powershell
# Test Community Service
curl http://localhost:8081/hello

# Test Event Service
curl http://localhost:8082/hello

# Test User Service
curl http://localhost:8083/hello
```

### Test Through API Gateway

```powershell
# Test Community Service through Gateway
curl http://localhost:8080/api/community/hello

# Test Event Service through Gateway
curl http://localhost:8080/api/events/hello

# Test User Service through Gateway
curl http://localhost:8080/api/users/hello
```

### Health Checks

```powershell
# Check API Gateway health
curl http://localhost:8080/actuator/health

# Check individual service health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

## 🔍 Monitoring and Management

All services include Spring Boot Actuator for monitoring:

- **Health Checks**: `/actuator/health`
- **Application Info**: `/actuator/info`
- **Gateway Routes** (API Gateway only): `/actuator/gateway/routes`

## 🐳 Docker Commands

```powershell
# View running containers
docker-compose ps

# View logs for all services
docker-compose logs

# View logs for specific service
docker-compose logs community-service

# Restart a specific service
docker-compose restart community-service

# Remove all containers and networks
docker-compose down --volumes
```

## 🔧 Development

### Adding New Services

1. Create new service directory
2. Add service to parent `pom.xml` modules section
3. Create service-specific `pom.xml`
4. Implement Spring Boot application
5. Add Dockerfile
6. Update `docker-compose.yml`
7. Configure routes in API Gateway if needed

### Configuration

Each service can be configured through:
- `application.yml` - Default configuration
- Environment variables
- Docker Compose environment section

## 🚨 Troubleshooting

### Common Issues

1. **Port conflicts**: Make sure ports 8080-8083 are available
2. **Docker build fails**: Ensure Docker daemon is running
3. **Service not accessible**: Check if all services are healthy using health endpoints
4. **Gateway routing issues**: Verify service names match Docker Compose service names

### Checking Service Status

```powershell
# Check if services are running
docker-compose ps

# Check service logs
docker-compose logs [service-name]

# Check service health
curl http://localhost:808[0-3]/actuator/health
```

## 📝 Next Steps

This is a basic starter template. Consider adding:

- Database integration (PostgreSQL, MongoDB)
- Service discovery (Eureka)
- Configuration management (Spring Cloud Config)
- Message queues (RabbitMQ, Apache Kafka)
- Monitoring (Prometheus, Grafana)
- Security (Spring Security, OAuth2)
- API documentation (OpenAPI/Swagger)
- Testing (Integration tests, Contract testing)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
