FROM gradle:7-alpine as server-builder
USER root
RUN apk add gcompat
WORKDIR /builder
ADD ./ /builder
RUN gradle build -x test --stacktrace

FROM eclipse-temurin:17-alpine
WORKDIR /app
EXPOSE 8081 9090
COPY --from=server-builder /builder/build/libs/*.jar ./server.jar

CMD ["java", "-jar", "server.jar"]
