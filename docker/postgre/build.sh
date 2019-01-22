#!/usr/bin/env bash

cp ../../src/main/resources/PostgreSQL_CreationOfTables.sql ./10-schema.sql
cp ../../src/main/resources/PostgreSQL_Data.sql ./20-data.sql
docker build -t dojo/postgres:10 .

