# Базовый образ с Playwright и браузерами
FROM mcr.microsoft.com/playwright/java:v1.60.0

# Установка зависимостей проекта
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn -version
ENV CI=true
CMD ["mvn", "test", "-Dtest=CheckboxTest"]