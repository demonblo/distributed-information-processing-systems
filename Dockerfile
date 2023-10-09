FROM maven:latest as build
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn package

FROM openjdk:17-oracle
EXPOSE 8080
RUN mkdir /app
WORKDIR app
ARG JAR_FILE=/home/maven/src/out/artifacts/persons_jar/persons.jar
ARG DB_URL
ENV DB_URL=$DB_URL
ARG DB_USERNAME
ENV DB_USERNAME=$DB_USERNAME
ARG DB_PASSWORD
ENV DB_PASSWORD=$DB_PASSWORD
ARG PORT
ENV PORT=$PORT
COPY --from=build ${JAR_FILE} "/app/app.jar"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]