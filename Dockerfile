FROM openjdk:17-jdk-slim AS build 


COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /flightreservation
COPY --from=build target/*.jar flightreservation.jar

ENTRYPOINT ["java", "-jar", "flightreservation.jar"]

# pom.xml = “projenin ihtiyaç listesi”
# .mvn src = “asıl kod”
