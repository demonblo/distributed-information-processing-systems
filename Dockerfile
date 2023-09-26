FROM openjdk:17-jdk-alpine
RUN mkdir /app
WORKDIR app
EXPOSE 8080
ARG JAR_FILE=./out/artifacts/persons_jar/persons.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Dserver.port=$PORT","/app/app.jar"]