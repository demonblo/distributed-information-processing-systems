FROM maven:latest as build
COPY . /home/maven/src
WORKDIR /home/maven/src
ARG DB_URL
ENV DB_URL=$DB_URL
ARG DB_USERNAME
ENV DB_USERNAME=$DB_USERNAME
ARG DB_PASSWORD
ENV DB_PASSWORD=$DB_PASSWORD
ARG PORT
ENV PORT=$PORT
RUN mvn clean package
ARG JAR_FILE=/home/maven/src/target/persons-0.0.1-SNAPSHOT.jar
ARG TARGET_FOLDER=/home/maven/src/target
RUN find persons-0.0.1-SNAPSHOT.jar

FROM openjdk:17-oracle
EXPOSE 8080
RUN mkdir /app
WORKDIR app

COPY --from=build ${TARGET_FOLDER} "/app"
ENTRYPOINT ["java", "-jar", "/app/persons-0.0.1-SNAPSHOT.jar"]