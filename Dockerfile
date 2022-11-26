FROM gradle:7.5.1-jdk17-alpine AS cache
COPY build.gradle settings.gradle ./
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
RUN gradle clean build -i

FROM gradle:7.5.1-jdk17-alpine AS builder
COPY build.gradle settings.gradle ./
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY src src
RUN gradle bootJar -i

FROM openjdk:17-alpine
COPY --from=builder /home/gradle/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${PROFILE}", "-DDATABASE_HOST=${DATABASE_HOST}"]