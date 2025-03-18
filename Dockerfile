# Use a minimal and secure base image
FROM eclipse-temurin:21.0.5_11-jre-alpine
RUN apk update && apk upgrade
COPY . .
RUN ./gradlew build
RUN mkdir /app
COPY /build/libs/*.jar /app/initRiSc.jar
EXPOSE 8085
# Create a non-root
RUN adduser -D user && chown -R user /app
WORKDIR /app
USER user
ENTRYPOINT ["java","-jar","/app/initRiSc.jar"]