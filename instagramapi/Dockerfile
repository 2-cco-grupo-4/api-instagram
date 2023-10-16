FROM openjdk:17-oracle
ARG JAR_FILE=target/service.jar
COPY target/instagramapi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080