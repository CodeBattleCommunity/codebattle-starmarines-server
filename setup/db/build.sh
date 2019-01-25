#!/usr/bin/env bash

cp ./PostgreSQL_CreationOfTables.sql ./10-schema.sql
cp ./PostgreSQL_Data.sql ./20-data.sql
docker build -t dojo/postgres:10 .
rm -f ./10-schema.sql
rm -f ./20-data.sql

