FROM openjdk:8 AS BUILD
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

FROM openjdk:8
ENV ARTIFACT_NAME=hardinv-0.0.1-SNAPSHOT.war
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/$ARTIFACT_NAME /
EXPOSE 8080
CMD ["java", "-jar", "/hardinv-0.0.1-SNAPSHOT.war"]
