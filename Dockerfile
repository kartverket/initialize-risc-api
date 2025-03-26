# Use a minimal and secure base image
FROM eclipse-temurin:23.0.2_7-jre-alpine-3.21
RUN apk update && apk upgrade
COPY . .
RUN ./gradlew build
RUN mkdir /app
COPY build/libs/*.jar /app/initRiSc.jar
EXPOSE 8085
# Create a non-root
RUN adduser -D user && chown -R user /app
WORKDIR /app
USER user
ENTRYPOINT ["java","-jar","/app/initRiSc.jar"]