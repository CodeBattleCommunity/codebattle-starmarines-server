FROM openjdk:8 AS BUILD
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY src $APP_HOME/src
COPY gradle $APP_HOME/gradle
RUN ./gradlew build

FROM openjdk:8
ENV ARTIFACT_NAME=hardinv-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app
#WORKDIR $APP_HOME
RUN mkdir /app
COPY --from=BUILD $APP_HOME/build/libs/$ARTIFACT_NAME /app
ENTRYPOINT ["java"]
CMD ["-jar", "/app/hardinv-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080