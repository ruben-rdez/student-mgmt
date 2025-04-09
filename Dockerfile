#BASE IMAGE
FROM eclipse-temurin:21-jdk-alpine

#INFORM THE PORT WHERE THE CONTAINER RUNS (INFORMATIVE)
EXPOSE 8080

#DEFINE ROOT DIRECTORY OF OUR CONTAINER
WORKDIR /app

#COPY AND PASTE FILES INTO THE CONTAINER
COPY ./pom.xml /app
COPY ./.mvn /app/.mvn
COPY ./mvnw /app

#DOWNLOAD DEPENDENCIES
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

#COPY THE SOURCE CODE INTO THE CONTAINER
COPY ./src /app/src

#BUILD OUR APPLICATION
RUN ./mvnw clean install -DskipTests

ENTRYPOINT ["java","-jar","/app/target/student-mgmt.jar"]
