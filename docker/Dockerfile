FROM openjdk:17-jdk

ARG JAR_FILE=/build/libs/RestPlaceForJ-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} backend.jar

ENTRYPOINT ["java","-jar","/backend.jar"]