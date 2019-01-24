#/usr/bin/env bash

REBUILD=false
PORT=8081
DB_PORT=5433
DB_HOST=epruizhsa0001t2

while true; do
	case "$1" in
		-b | --build) REBUILD=true; shift ;;
		-p | --port) PORT="$2"; shift ;;
		-d | --db-port) DB_PORT="$2"; shift ;;
		-h | --db-host) DB_HOST="$2"; shift ;;
		* ) break ;;
	esac
done

if [ "$REBUILD" = true ]; then
	./gradlew clean build
	if [ $? -ne 0 ]; then
		echo "Build failed"
		exit 1
	fi
fi

java -jar -Dserver.port="$PORT" build/libs/hardinv-0.0.1-SNAPSHOT.jar --spring.datasource.host="$DB_HOST" --spring.datasource.port="$DB_PORT"
