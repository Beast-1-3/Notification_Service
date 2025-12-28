# Scalable Notification System

A microservices-based notification system built with Spring Boot, Kafka, and PostgreSQL for asynchronous email delivery.

## Architecture

The system consists of two microservices:

- **Notification API** - REST API for creating and querying notifications (Port 8080)
- **Notification Worker** - Background service that processes notifications and sends emails (Port 8081)

When a notification is created via the API, it's saved to PostgreSQL and sent to Kafka. The worker service picks it up from Kafka and sends the email asynchronously.

## Features

- RESTful API for notification management
- Asynchronous email processing using Kafka
- Automatic retry mechanism (3 attempts with 5-minute intervals)
- Email validation and error handling
- Docker Compose setup for easy deployment

## Tech Stack

- Java 17 & Spring Boot
- Apache Kafka for message queuing
- PostgreSQL for data persistence
- Spring Mail with Gmail SMTP
- Docker & Docker Compose

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose
- Gmail account with App Password

### Setup

1. **Clone the repository**
```bash
git clone https://github.com/Kapilgupta20/Notification_Service.git
cd notification-system
```

2. **Configure email credentials**

Create a `.env` file in the project root:
```env
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

Generate Gmail App Password at: https://myaccount.google.com/apppasswords

3. **Build the services**
```bash
# Build API
cd notification-api
./mvnw clean package -DskipTests

# Build Worker
cd ../notification-worker
./mvnw clean package -DskipTests
cd ..
```

4. **Start the system**
```bash
docker-compose up --build -d
```

5. **Initialize database**
```bash
docker cp schema.sql notification-postgres:/schema.sql
docker exec -it notification-postgres psql -U postgres -d notification_db -f /schema.sql
```

6. **Test the API**

Create a notification:
```bash
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -d '{"recipientEmail":"test@example.com","subject":"Test","body":"Hello!"}'
```

Get all notifications:
```bash
curl http://localhost:8080/api/notifications
```

## How It Works

1. Client sends POST request to API with email details
2. API validates input and saves to PostgreSQL (status: PENDING)
3. API sends notification ID to Kafka topic
4. Worker consumes message from Kafka
5. Worker sends email via Gmail SMTP
6. Worker updates status to SENT or FAILED
7. If failed, retry mechanism attempts up to 3 times

## Notification Status

- **PENDING** - Created, waiting to be sent
- **SENT** - Successfully sent
- **FAILED** - Failed after 3 retry attempts

## Future Enhancements

- API authentication and request verification
- SMS notification support
- Rate limiting for API endpoints
- Support for multiple email providers

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.