FROM gradle:7.4.2-jdk17-alpine as server-builder

RUN apk add gcompat

COPY --chown=gradle:gradle . /app

WORKDIR /app

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-alpine

WORKDIR /app

COPY --from=server-builder /app/server/build/libs/*.jar ./server.jar

EXPOSE 8081 9090

CMD ["java", "-jar", "server.jar"]
