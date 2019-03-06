FROM gradle:jdk8-alpine as BUILD
ENV APP_HOME=/home/gradle
WORKDIR $APP_HOME
RUN rm -rf $APP_HOME/src || return 0
COPY --chown=gradle:gradle build.gradle settings.gradle gradlew $APP_HOME/
COPY --chown=gradle:gradle gradle $APP_HOME/gradle
COPY --chown=gradle:gradle src $APP_HOME/src
RUN gradle clean build -xtest --stacktrace

FROM openjdk:8
ENV ARTIFACT_NAME=hardinv-0.0.1-SNAPSHOT.war
ENV APP_HOME=/home/gradle
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/$ARTIFACT_NAME /
EXPOSE 8080
CMD ["java", "-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n", "-jar", "/hardinv-0.0.1-SNAPSHOT.war"]
