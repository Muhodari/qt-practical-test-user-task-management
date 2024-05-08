FROM openjdk:17

ENV SPRING_PROFILES_ACTIVE staging

WORKDIR /app

COPY target/userAccount-ms.jar userAccount-ms.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "userAccount-ms.jar"]
