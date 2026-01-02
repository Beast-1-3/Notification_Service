#!/bin/bash

# Configuration
USER_EMAIL="akarsh7376@gmail.com"
USER_NAME="Beast-1-3"
export GIT_AUTHOR_NAME="$USER_NAME"
export GIT_COMMITTER_NAME="$USER_NAME"
export GIT_AUTHOR_EMAIL="$USER_EMAIL"
export GIT_COMMITTER_EMAIL="$USER_EMAIL"

# Function to commit with a specific date
commit_at() {
    local date_str="$1"
    local message="$2"
    export GIT_AUTHOR_DATE="$date_str"
    export GIT_COMMITTER_DATE="$date_str"
    git commit -m "$message"
}

# 1. Dec 28, 2025
git add .gitignore README.md schema.sql
commit_at "2025-12-28 10:15:00" "Initial project structure and database schema"

git add docker-compose.yml
commit_at "2025-12-28 15:30:00" "Setup Docker infrastructure for Kafka and PostgreSQL"

# 2. Dec 29, 2025
git add notification-api/pom.xml notification-api/src/main/resources/
commit_at "2025-12-29 09:45:00" "notification-api: dependency setup and properties"

git add notification-api/src/main/java/com/notification/notification_api/entity/ notification-api/src/main/java/com/notification/notification_api/repository/
commit_at "2025-12-29 13:20:00" "notification-api: database entities and repository layer"

git add notification-api/src/main/java/com/notification/notification_api/dto/
commit_at "2025-12-29 18:05:00" "notification-api: request/response DTOs for notification lifecycle"

# 3. Dec 30, 2025
git add notification-api/src/main/java/com/notification/notification_api/service/
commit_at "2025-12-30 11:10:00" "notification-api: kafka producer and notification service implementation"

git add notification-api/src/main/java/com/notification/notification_api/controller/ notification-api/src/main/java/com/notification/notification_api/exception/ notification-api/src/main/java/com/notification/notification_api/NotificationApiApplication.java
commit_at "2025-12-30 16:45:00" "notification-api: REST controllers and global exception handling"

# 4. Dec 31, 2025
git add notification-worker/pom.xml notification-worker/src/main/resources/
commit_at "2025-12-31 10:30:00" "notification-worker: bootstrap and configuration"

git add notification-worker/src/main/java/com/notification/notification_worker/entity/ notification-worker/src/main/java/com/notification/notification_worker/repository/
commit_at "2025-12-31 14:15:00" "notification-worker: shared entity models and repository access"

git add notification-worker/src/main/java/com/notification/notification_worker/service/EmailService.java
commit_at "2025-12-31 17:50:00" "notification-worker: SMTP email service integration"

# 5. Jan 01, 2026
git add notification-worker/src/main/java/com/notification/notification_worker/service/KafkaConsumerService.java
commit_at "2026-01-01 12:00:00" "notification-worker: kafka consumer logic for async processing"

git add notification-worker/src/main/java/com/notification/notification_worker/service/NotificationProcessor.java notification-worker/src/main/java/com/notification/notification_worker/NotificationWorkerApplication.java
commit_at "2026-01-01 18:30:00" "notification-worker: core processing engine and entry point"

# 6. Jan 02, 2026
git add notification-worker/src/main/java/com/notification/notification_worker/service/RetryScheduler.java
commit_at "2026-01-02 10:20:00" "notification-worker: implemented distributed retry mechanism with scheduling"

git add notification-api/.gitattributes notification-api/.gitignore notification-worker/.gitattributes notification-worker/.gitignore
commit_at "2026-01-02 14:30:00" "Refined project attributes and git configuration"

git add notification-api/src/test/ notification-worker/src/test/
commit_at "2026-01-02 17:50:00" "Finalized initial test suite boilerplate"

git add git_history_script.sh
commit_at "2026-01-02 21:00:00" "Added deployment and history automation scripts"
