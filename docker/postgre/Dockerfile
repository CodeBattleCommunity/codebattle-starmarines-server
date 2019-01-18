FROM postgres:10
ENV POSTGRES_USER HIapp
ENV POSTGRES_PASSWORD hardcoded2014
ENV POSTGRES_DB hardinv
ADD ./10-schema.sql /docker-entrypoint-initdb.d/
ADD ./20-data.sql /docker-entrypoint-initdb.d/
