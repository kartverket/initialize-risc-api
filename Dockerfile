# Use a minimal and secure base image
ARG BUILD_IMAGE=eclipse-temurin:23.0.2_7-jdk-alpine-3.21
ARG IMAGE=eclipse-temurin:23.0.2_7-jre-alpine-3.21

FROM ${BUILD_IMAGE} AS build
RUN apk update && apk upgrade
COPY . .
RUN ./gradlew build -x test

FROM ${IMAGE}
RUN mkdir /app
COPY build/libs/*.jar /app/initRiSc.jar
EXPOSE 8085
# Create a non-root
RUN adduser -D user && chown -R user /app
WORKDIR /app
USER user
ENTRYPOINT ["java","-jar","/app/initRiSc.jar"]