FROM gradle:7-alpine as builder
USER root
RUN apk add gcompat
WORKDIR /builder
ADD ./server /builder
RUN gradle build -x test --stacktrace

FROM eclipse-temurin:17-alpine
WORKDIR /app
EXPOSE 8081 9090
COPY --from=builder /builder/build/libs/*.jar ./server.jar
CMD ["java", "-jar", "server.jar"]
