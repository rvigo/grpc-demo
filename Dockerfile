FROM gradle:7-alpine as builder
USER root
RUN apk add gcompat
WORKDIR /builder
ADD . /builder
RUN gradle build -x test --stacktrace

FROM eclipse-temurin:17-alpine
WORKDIR /app
EXPOSE 8080 9090
COPY --from=builder /builder/build/libs/grpcPoc-0.0.1-SNAPSHOT.jar ./server.jar
CMD ["java", "-jar", "server.jar"]
