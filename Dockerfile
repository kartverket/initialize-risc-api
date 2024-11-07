# Use a minimal and secure base image
FROM eclipse-temurin:21.0.2_13-jre-alpine
RUN apk update && apk upgrade
# Create a non-root
RUN mkdir /app
EXPOSE 8085
RUN adduser -D user && chown -R user /app
WORKDIR .
COPY build/libs/*.jar /app/initRiSc.jar
USER user
ENTRYPOINT ["java","-jar","/app/initRiSc.jar"]