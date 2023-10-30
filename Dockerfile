FROM openjdk:17-alpine
MAINTAINER nrpndr
WORKDIR /app/user-service
EXPOSE 8095
ARG JAR_FILE=target/user-service.jar
COPY ${JAR_FILE} user-service.jar
ENTRYPOINT ["java","-jar","user-service.jar"]