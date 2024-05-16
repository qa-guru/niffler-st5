#!/bin/bash

echo '### Remove docker containers ###'
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

echo '### Run postgres ###'
docker run --name niffler-all -p 5432:5432 -e POSTGRES_PASSWORD=secret -v pgdata-st5:/var/lib/postgresql/data -d postgres:15.1
echo '### Run zookeeper & kafka ###'
docker run --name=zookeeper -e ZOOKEEPER_CLIENT_PORT=2181 -p 2181:2181 -d confluentinc/cp-zookeeper:7.3.2
docker run --name=kafka -e KAFKA_BROKER_ID=1 \
-e KAFKA_ZOOKEEPER_CONNECT=$(docker inspect zookeeper --format='{{ .NetworkSettings.IPAddress }}'):2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
-e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
-e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
-p 9092:9092 -d confluentinc/cp-kafka:7.3.2

echo '### Run front ###'
cd ./niffler-frontend/
npm i
npm run build:windows
cd ../
