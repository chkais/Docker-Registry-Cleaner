FROM openjdk:11

ARG JAR_FILE

COPY ./build/libs/docker-registry-cleaner-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]