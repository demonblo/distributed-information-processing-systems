FROM maven:latest as build
COPY . /home/maven/src
WORKDIR /home/maven/src
RUN mvn package

FROM openjdk:17-oracle
COPY --from=build /home/maven/src/out/artifacts/persons_jar/persons.jar /usr/local/lib/persons.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/persons.jar"]