# Use a minimal and secure base image
ARG BUILD_IMAGE=eclipse-temurin:24.0.2_12-jdk-alpine-3.22
ARG IMAGE=eclipse-temurin:24.0.2_12-jre-alpine-3.22

FROM ${BUILD_IMAGE} AS build

# Get security updates
RUN apk upgrade --no-cache

COPY . .
RUN ./gradlew build -x test

FROM ${IMAGE}

# Get security updates
RUN apk upgrade --no-cache

RUN mkdir /app
COPY --from=build build/libs/*.jar /app/initRiSc.jar
EXPOSE 8085
# Create a non-root
RUN adduser -D user && chown -R user /app
WORKDIR /app
USER user
ENTRYPOINT ["java","-jar","/app/initRiSc.jar"]

HEALTHCHECK --start-period=30s --interval=5m \
    CMD wget -O - --quiet --tries=1 http://localhost:8085/health | grep "All good" || exit 1
