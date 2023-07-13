FROM gradle:7.4.2-jdk17-alpine as client-builder

RUN apk add gcompat

COPY --chown=gradle:gradle . /app

WORKDIR /app

RUN gradle bootJar --no-daemon


FROM eclipse-temurin:17-alpine

WORKDIR /app

COPY --from=client-builder /app/client/build/libs/*.jar ./client.jar

EXPOSE 8080

CMD ["java", "-jar", "client.jar"]

