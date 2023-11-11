FROM openjdk:17-alpine
MAINTAINER nrpndr
WORKDIR /app/user-service
EXPOSE 9011
ARG JAR_FILE=target/user-service.jar
COPY ${JAR_FILE} user-service.jar
ENTRYPOINT ["java","-jar","user-service.jar"]