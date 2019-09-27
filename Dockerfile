FROM openjdk:8-jdk-alpine
MAINTAINER damaya0226@gmail.com
COPY ./target/dev-assessment-1.0.0.jar dev-assessment-1.0.0.jar
ENTRYPOINT ["java","-jar","/dev-assessment-1.0.0.jar"]