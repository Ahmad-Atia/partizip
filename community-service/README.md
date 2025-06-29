# Community Service

Ein vollständiger Community-Service für das Partizip-Projekt, implementiert mit Spring Boot, Hibernate, JPA und MySQL.

## 🏗️ Architektur

Das Community Service implementiert ein soziales Community-System mit folgenden Hauptkomponenten:

### Entitäten
- **Post**: Beiträge mit Likes, Shares und Event-Tagging
- **Poll**: Umfragen mit Voting-Funktionalität  
- **Comment**: Kommentare zu Posts und Polls

### Design Patterns
- **Observer Pattern**: Für Echtzeit-Benachrichtigungen bei Community-Events
- **Repository Pattern**: Für Datenzugriff mit Spring Data JPA
- **DTO Pattern**: Für API-Datenübertragung

## 🚀 Features

### ✅ Funktionale Anforderungen

#### 1. **POST-Management**
- `POST /api/community/posts` - Erstellt einen neuen Post
- `GET /api/community/posts` - Holt alle Posts  
- `GET /api/community/posts/{postId}` - Holt einen spezifischen Post
- `POST /api/community/posts/{postId}/like` - Liked einen Post
- `DELETE /api/community/posts/{postId}/like` - Entfernt ein Like  
- `POST /api/community/posts/{postId}/share` - Teilt einen Post

#### 2. **POLL-Management**
- `POST /api/community/polls` - Erstellt eine neue Umfrage
- `GET /api/community/polls` - Holt alle Umfragen
- `GET /api/community/polls/{pollId}` - Holt eine spezifische Umfrage
- `POST /api/community/polls/{pollId}/vote` - Stimmt in einer Umfrage ab

#### 3. **COMMENT-Management**
- `POST /api/community/comments/{targetId}` - Fügt einen Kommentar hinzu
- `GET /api/community/comments/{targetId}` - Holt Kommentare für ein Target

### 📊 **Analytics & Monitoring**
- `GET /api/community/stats` - Community-Statistiken
- `GET /api/community/posts/popular` - Beliebte Posts
- `GET /api/community/polls/popular` - Beliebte Umfragen
- `GET /api/community/health` - Health Check

## 🔧 Technischer Stack

- **Backend**: Java 17, Spring Boot 3.2.0
- **Persistenz**: JPA/Hibernate, MySQL 8
- **Build**: Maven
- **Testing**: JUnit 5, Mockito, H2 (Test-DB)
- **API**: REST, JSON
- **Validation**: Bean Validation (Jakarta)

## 🛠️ Setup & Installation

### Voraussetzungen
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Docker (optional)

### 1. Datenbank Setup
```sql
CREATE DATABASE partizip_community;
CREATE USER 'partizip_user'@'localhost' IDENTIFIED BY 'partizip_password';
GRANT ALL PRIVILEGES ON partizip_community.* TO 'partizip_user'@'localhost';
```

### 2. Konfiguration
Anpassen der `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/partizip_community
    username: partizip_user
    password: partizip_password
```

### 3. Build & Run
```bash
# Build
mvn clean compile

# Tests ausführen
mvn test
# oder
./run-tests.ps1  # Windows PowerShell
./run-tests.sh   # Linux/Mac

# Anwendung starten
mvn spring-boot:run
```

### 4. Docker (Optional)
```bash
# Mit Docker Compose (im partizip Root-Ordner)
docker-compose up community-service
```

## 🧪 Testing

### Test-Kategorien
1. **Unit Tests**: Service-Logik mit Mockito
2. **Integration Tests**: Controller-Tests mit MockMvc  
3. **End-to-End Tests**: Vollständige Workflows

### Test-Ausführung
```bash
# Alle Tests
mvn test

# Spezifische Test-Kategorien
mvn test -Dtest="*Test"              # Unit Tests
mvn test -Dtest="*IntegrationTest"   # Integration Tests  
mvn test -Dtest="*EndToEndTest"      # E2E Tests

# Test-Report generieren
mvn surefire-report:report
```

## 📋 API-Dokumentation

### Post-Erstellung
```bash
POST /api/community/posts
Content-Type: application/json

{
  "authorID": "123e4567-e89b-12d3-a456-426614174000",
  "content": "Mein erster Post!",
  "mediaUrl": "https://example.com/image.jpg",
  "taggedEventId": "123e4567-e89b-12d3-a456-426614174001"
}
```

### Poll-Erstellung
```bash
POST /api/community/polls  
Content-Type: application/json

{
  "authorID": "123e4567-e89b-12d3-a456-426614174000",
  "question": "Was ist euer Lieblingskolor?",
  "options": ["Rot", "Blau", "Grün", "Gelb"]
}
```

### Voting
```bash
POST /api/community/polls/{pollId}/vote
Content-Type: application/json

{
  "userID": "123e4567-e89b-12d3-a456-426614174000", 
  "option": "Blau"
}
```

## 🔐 Business Rules

### Like-System
- ✅ Ein User kann einen Post nur einmal liken
- ✅ Mehrfaches Liken wird ignoriert (idempotent)
- ✅ Unlike entfernt das Like wieder

### Poll-System  
- ✅ Mindestens 2 Optionen erforderlich
- ✅ Ein User kann nur einmal abstimmen
- ✅ Vote-Änderung überschreibt vorherige Stimme
- ✅ Ungültige Optionen werden abgelehnt

### Comment-System
- ✅ Kommentare können zu Posts oder Polls hinzugefügt werden
- ✅ Target-Validation stellt sicher, dass das Ziel existiert

## 📈 Monitoring & Observability

### Health Checks
```bash
GET /api/community/health
```

### Metriken
```bash
GET /actuator/health
GET /actuator/metrics  
```

### Logging
- **DEBUG**: Business-Logik, SQL-Queries
- **INFO**: API-Aufrufe, wichtige Events
- **WARN**: Validation-Fehler, nicht gefundene Ressourcen
- **ERROR**: Unerwartete Fehler, System-Probleme

## 🔮 Erweiterungsmöglichkeiten

### Geplante Features
- 🔄 **WebSocket**: Echtzeit-Updates für Likes/Comments
- 🔐 **JWT-Security**: Authentifizierung und Autorisierung  
- 📨 **Kafka-Integration**: Event-Streaming für Microservices
- 🎯 **Event-Sourcing**: Vollständige Event-Historie
- 📱 **Push-Notifications**: Mobile Benachrichtigungen
- 🖼️ **File-Upload**: Direkte Medien-Uploads
- 🔍 **Search-API**: Volltextsuche in Posts/Polls
- 📊 **Analytics-Dashboard**: Erweiterte Community-Metriken

## 🐛 Troubleshooting

### Häufige Probleme

#### Database Connection
```
Error: Could not create connection to database server
→ Überprüfe MySQL-Server und Credentials in application.yml
```

#### Port bereits belegt
```  
Error: Port 8082 is already in use
→ server.port in application.yml ändern oder Prozess beenden
```

#### Test-Fehler
```
Error: Tests failing with H2 database
→ Überprüfe application-test.yml Konfiguration
```

## 👥 Contributing

1. Fork das Repository
2. Feature-Branch erstellen (`git checkout -b feature/amazing-feature`)
3. Änderungen committen (`git commit -m 'Add amazing feature'`)  
4. Branch pushen (`git push origin feature/amazing-feature`)
5. Pull Request erstellen

## 📄 Lizenz

Dieses Projekt ist Teil des Partizip-Projekts und unterliegt der entsprechenden Lizenz.

---

**Entwickelt mit ❤️ für die Partizip-Community**
