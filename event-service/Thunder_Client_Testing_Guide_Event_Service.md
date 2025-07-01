# 🌩️ Thunder Client Testing Guide - Event Service

## 📋 Voraussetzungen

1. **Thunder Client Extension** in VS Code installiert
2. **Event Service** läuft auf `http://localhost:3003`
3. **MySQL Database** ist verfügbar oder H2 für Tests

## 🚀 Service starten

```bash
# Im event-service Verzeichnis
cd partizip/event-service

# Mit Maven starten
mvn spring-boot:run

# Oder mit Docker
docker-compose up -d
```

---

## 📡 API Endpoints Testing

### 1. 📅 Event erstellen

**Request:**
```
Method: POST
URL: http://localhost:3003/events
Headers: Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Summer Coding Workshop 2025",
  "description": "Ein intensiver Workshop über moderne Webtechnologien und Best Practices.",
  "date": "2025-08-15T10:00:00",
  "location": "TechHub Berlin, Hauptstraße 123",
  "creatorID": "123e4567-e89b-12d3-a456-426614174000",
  "isPublic": true,
  "status": "PLANNED"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "generated-uuid",
  "name": "Summer Coding Workshop 2025",
  "description": "Ein intensiver Workshop über moderne Webtechnologien und Best Practices.",
  "date": "2025-08-15T10:00:00",
  "location": "TechHub Berlin, Hauptstraße 123",
  "creatorID": "123e4567-e89b-12d3-a456-426614174000",
  "isPublic": true,
  "status": "PLANNED",
  "participants": [],
  "feedbacks": []
}
```

---

### 2. 📋 Alle Events abrufen

**Request:**
```
Method: GET
URL: http://localhost:3003/events
```

**Expected Response (200 OK):**
```json
[
  {
    "id": "event-uuid-1",
    "name": "Summer Coding Workshop 2025",
    "description": "Ein intensiver Workshop über moderne Webtechnologien...",
    "date": "2025-08-15T10:00:00",
    "location": "TechHub Berlin, Hauptstraße 123",
    "creatorID": "123e4567-e89b-12d3-a456-426614174000",
    "isPublic": true,
    "status": "PLANNED",
    "participants": [],
    "feedbacks": []
  }
]
```

---

### 3. 🔍 Event Details abrufen

**Request:**
```
Method: GET
URL: http://localhost:3003/events/{EVENT_ID_FROM_STEP_1}
```

**Expected Response (200 OK):**
```json
{
  "id": "event-uuid-from-step-1",
  "name": "Summer Coding Workshop 2025",
  "description": "Ein intensiver Workshop über moderne Webtechnologien und Best Practices.",
  "date": "2025-08-15T10:00:00",
  "location": "TechHub Berlin, Hauptstraße 123",
  "creatorID": "123e4567-e89b-12d3-a456-426614174000",
  "isPublic": true,
  "status": "PLANNED",
  "participants": [],
  "feedbacks": []
}
```

---

### 4. 👥 Event Teilnahme registrieren

**Request:**
```
Method: POST
URL: http://localhost:3003/events/{EVENT_ID_FROM_STEP_1}/participants?userId=456e7890-e12b-34c5-d678-901234567890
```

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Participation registered successfully"
}
```

---

### 5. 💬 Feedback zu Event hinzufügen

**Request:**
```
Method: POST
URL: http://localhost:3003/events/{EVENT_ID_FROM_STEP_1}/feedback
Headers: Content-Type: application/json
```

**Body (JSON):**
```json
{
  "userID": "456e7890-e12b-34c5-d678-901234567890",
  "content": "Fantastisches Event! Die Organisation war perfekt und ich habe viel gelernt. 🎉",
  "rating": 5
}
```

**Expected Response (200 OK):**
```json
{
  "feedbackID": "generated-uuid",
  "userID": "456e7890-e12b-34c5-d678-901234567890",
  "content": "Fantastisches Event! Die Organisation war perfekt und ich habe viel gelernt. 🎉",
  "rating": 5,
  "createdAt": "2025-08-16T14:30:00",
  "event": {
    "id": "event-uuid-from-step-1",
    "name": "Summer Coding Workshop 2025"
  }
}
```

---

### 6. ✅ Service Health Check

**Request:**
```
Method: GET
URL: http://localhost:3003/hello
```

**Expected Response (200 OK):**
```
Hello from Event Service!
```

---

### 7. 🏥 Health Status

**Request:**
```
Method: GET
URL: http://localhost:3003/health
```

**Expected Response (200 OK):**
```
Event Service is running
```

---

## 🧪 Thunder Client Collections

### Collection: Event Service Tests

**Erstellen Sie eine neue Collection in Thunder Client:**

1. Öffnen Sie Thunder Client (Blitz-Symbol in der Seitenleiste)
2. Klicken Sie auf "New Collection"
3. Name: "Event Service API Tests"

**Fügen Sie folgende Requests hinzu:**

#### Request 1: Create Event
- **Name:** "1. Create Test Event"
- **Method:** POST
- **URL:** `{{baseUrl}}/events`
- **Body:** JSON aus Schritt 1 oben

#### Request 2: Get All Events
- **Name:** "2. Get All Events"
- **Method:** GET
- **URL:** `{{baseUrl}}/events`

#### Request 3: Get Event Details
- **Name:** "3. Get Event Details"
- **Method:** GET
- **URL:** `{{baseUrl}}/events/{{eventId}}`

#### Request 4: Register Participation
- **Name:** "4. Register for Event"
- **Method:** POST
- **URL:** `{{baseUrl}}/events/{{eventId}}/participants?userId={{userId}}`

#### Request 5: Add Feedback
- **Name:** "5. Add Event Feedback"
- **Method:** POST
- **URL:** `{{baseUrl}}/events/{{eventId}}/feedback`
- **Body:** JSON aus Schritt 5 oben

#### Request 6: Health Check
- **Name:** "6. Service Hello"
- **Method:** GET
- **URL:** `{{baseUrl}}/hello`

#### Request 7: Health Status
- **Name:** "7. Service Health"
- **Method:** GET
- **URL:** `{{baseUrl}}/health`

---

## 🔧 Environment Variables

**Erstellen Sie ein Environment in Thunder Client:**

1. Klicken Sie auf "Env" Tab
2. Erstellen Sie "Event Service Local"
3. Fügen Sie folgende Variablen hinzu:

```json
{
  "baseUrl": "http://localhost:3003",
  "eventId": "wird-nach-event-creation-gesetzt",
  "userId": "456e7890-e12b-34c5-d678-901234567890",
  "creatorId": "123e4567-e89b-12d3-a456-426614174000",
  "participantId": "789e0123-e45f-67g8-h901-234567890123"
}
```

---

## 📊 Test Scenarios

### Scenario 1: Happy Path - Complete Event Lifecycle
1. ✅ Create Event → Status 200
2. ✅ Get All Events → Status 200, Array contains new event
3. ✅ Get Event Details → Status 200, Complete event data
4. ✅ Register Participation → Status 200, Success message
5. ✅ Add Feedback → Status 200, Feedback created
6. ✅ Health Check → Status 200, Service running

### Scenario 2: Error Cases
1. ❌ Create Event with missing required fields → Status 400
2. ❌ Get non-existent event → Status 404
3. ❌ Register for non-existent event → Status 400
4. ❌ Add feedback with invalid rating (< 1 or > 5) → Status 400
5. ❌ Add feedback to non-existent event → Status 400

### Scenario 3: Edge Cases
1. 🔄 Register same user twice for same event
2. 📅 Create event with past date
3. 📝 Very long event description (test limits)
4. 🔗 Invalid UUID formats
5. ⭐ Feedback with rating boundaries (1 and 5)

### Scenario 4: Event Status Management
1. 📋 Create event with status "PLANNED"
2. ▶️ Update event status to "ACTIVE"
3. ❌ Update event status to "CANCELLED"
4. 🔍 Verify status changes in event details

---

## 🐛 Debugging Tips

### Common Issues:

**1. Connection Refused (ECONNREFUSED)**
```bash
# Check if service is running
curl http://localhost:3003/hello

# Start service if not running
mvn spring-boot:run
```

**2. 404 Not Found**
- ✅ Check URL: `http://localhost:3003/events`
- ✅ Verify no `/api` prefix needed
- ✅ Check event ID in URL path

**3. 400 Bad Request**
- ✅ Check JSON syntax
- ✅ Verify required fields (name, date, location, creatorID)
- ✅ Check UUID format for IDs
- ✅ Verify date format (ISO 8601)
- ✅ Check rating range (1-5)

**4. 500 Internal Server Error**
- ✅ Check application logs
- ✅ Verify database connection
- ✅ Check for missing dependencies
- ✅ Verify foreign key constraints

### Logging:
```bash
# Check application logs
tail -f logs/event-service.log

# Or in Docker
docker logs event-service -f

# Check specific errors
grep ERROR logs/event-service.log
```

---

## 📈 Response Validation

### Success Indicators:
- ✅ **Status Codes:** 200 (OK), 201 (Created)
- ✅ **Response Time:** < 500ms
- ✅ **Valid JSON:** Proper structure returned
- ✅ **UUID Format:** Valid UUID in responses
- ✅ **Date Format:** ISO 8601 timestamps

### Data Validation:
- ✅ **Event Status:** Valid enum values (PLANNED, ACTIVE, CANCELLED)
- ✅ **Participation Status:** Valid enum values (REGISTERED, CANCELLED, WAITING)
- ✅ **Rating Range:** 1-5 for feedback
- ✅ **Relationships:** Proper participant and feedback associations
- ✅ **Content:** No XSS or injection vulnerabilities

---

## 🎯 Advanced Testing Scenarios

### Performance Testing:
1. **Load Testing:** Create 100+ events rapidly
2. **Concurrent Registrations:** Multiple users register simultaneously
3. **Bulk Feedback:** Add multiple feedback entries

### Data Integrity:
1. **Cascade Operations:** Delete event and verify participant cleanup
2. **Constraint Validation:** Test foreign key relationships
3. **Transaction Rollback:** Simulate database errors

### Security Testing:
1. **Input Validation:** Test with malicious content
2. **SQL Injection:** Test database query safety
3. **XSS Prevention:** Test script injection in descriptions

---

## 🚀 Quick Start Checklist

1. **[ ]** Thunder Client Extension installed
2. **[ ]** Event Service running on port 3003
3. **[ ]** MySQL database connected and running
4. **[ ]** Collection "Event Service API Tests" created
5. **[ ]** Environment variables configured
6. **[ ]** Test Scenario 1 (Happy Path) executed successfully
7. **[ ]** All endpoints return expected status codes
8. **[ ]** Response data matches expected format
9. **[ ]** Database tables created (events, participants, feedbacks)
10. **[ ]** Service health endpoints responding

**Ready to test your Event Service! 🎯**

---

## 📚 Additional Resources

### Sample Test Data:

**Multiple Events:**
```json
[
  {
    "name": "React Advanced Workshop",
    "description": "Deep dive into React hooks and performance optimization",
    "date": "2025-09-20T14:00:00",
    "location": "Online via Zoom",
    "creatorID": "123e4567-e89b-12d3-a456-426614174000",
    "isPublic": true
  },
  {
    "name": "DevOps Meetup",
    "description": "Docker, Kubernetes and CI/CD best practices",
    "date": "2025-10-05T18:30:00",
    "location": "Innovation Hub München",
    "creatorID": "123e4567-e89b-12d3-a456-426614174000", 
    "isPublic": false
  }
]
```

**Sample Feedback Entries:**
```json
[
  {
    "userID": "user1-uuid",
    "content": "Excellent content and presentation!",
    "rating": 5
  },
  {
    "userID": "user2-uuid", 
    "content": "Good event but could be better organized",
    "rating": 3
  },
  {
    "userID": "user3-uuid",
    "content": "Amazing networking opportunities",
    "rating": 4
  }
]
```
