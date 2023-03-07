FROM gradle:7-alpine as client-builder
USER root
RUN apk add gcompat
WORKDIR /builder
ADD ./ /builder
RUN gradle build -x test --stacktrace

FROM eclipse-temurin:17-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=client-builder /builder/build/libs/*.jar ./client.jar

CMD ["java", "-jar", "client.jar"]

