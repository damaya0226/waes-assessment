FROM openjdk:8-jdk-alpine
MAINTAINER damaya0226@gmail.com

COPY ./ ./

RUN ./mvnw clean package

ENTRYPOINT ["java","-jar","./target/dev-assessment-1.0.0.jar"]