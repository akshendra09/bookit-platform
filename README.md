# BookIt — event-driven booking platform

A Java 21 / Spring Boot microservices platform for booking venues and events, built to demonstrate real distributed-systems patterns: service discovery, an API gateway, concurrency-safe reservations, and event-driven communication over Kafka.

## Architecture

```
Client → API Gateway → [User | Catalog | Booking | Payment | Notification] services
                              ↑ registered with Eureka, config from Config Server
Booking service → Kafka (booking.created) → Payment service, Notification service
```

| Service | Port | Responsibility |
|---|---|---|
| discovery-server | 8761 | Eureka service registry |
| config-server | 8888 | Centralized configuration |
| api-gateway | 8080 | Single entry point, routes to downstream services |
| user-service | 8081 | Registration/login, JWT issuance |
| catalog-service | 8082 | Venues, Redis-cached reads |
| booking-service | 8083 | Reservation creation with optimistic-locking conflict prevention |
| payment-service | 8084 | Kafka consumer that processes payment for new bookings |
| notification-service | 8085 | Kafka consumer that sends booking confirmations |

Booking, Payment, and Notification are decoupled: Booking never calls Payment or Notification directly — it publishes a `booking.created` event to Kafka and moves on. This is the saga-style pattern worth calling out in interviews.

## Prerequisites

- Java 21 (`sdk install java 21-tem` if using SDKMAN)
- Maven 3.9+ (or use the Docker-based build below, which needs no local Maven)
- Docker + Docker Compose

## Run everything locally

```bash
docker compose up --build
```

This starts Postgres (with one database per service), Redis, Kafka/Zookeeper, and all eight Spring Boot services. First build takes a few minutes since each service's Dockerfile compiles with Maven inside the container.

Check services registered with Eureka: http://localhost:8761

## Try it out

```bash
# Register a user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"a@example.com","password":"pass123","fullName":"Ada"}'

# Create a venue
curl -X POST http://localhost:8080/api/catalog/venues \
  -H "Content-Type: application/json" \
  -d '{"name":"Downtown Hall","city":"Austin","capacity":200}'

# Book a slot — triggers booking.created → payment-service and notification-service consume it
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"venueId":1,"userId":1,"slotStart":"2026-10-01T18:00:00Z","slotEnd":"2026-10-01T20:00:00Z"}'
```

Watch the notification-service logs (`docker compose logs -f notification-service`) to see the consumed event.

## Build and test without Docker

```bash
mvn verify
```

## Roadmap / good next additions

- Circuit breakers (Resilience4j) between gateway and services
- Distributed tracing (OpenTelemetry + Zipkin/Jaeger)
- Payment/notification services publishing their own outcome events back to Kafka
- Testcontainers-based integration tests (real Postgres/Kafka per test run)
- Kubernetes manifests for a non-Compose deployment
- Deploy target: Render, Railway, or AWS ECS with a public demo URL

## CI/CD

`.github/workflows/ci.yml` builds and tests on every push/PR to `main`, then builds and pushes a Docker image per service to GitHub Container Registry (`ghcr.io/<owner>/bookit-<service>`) on merges to `main`.
