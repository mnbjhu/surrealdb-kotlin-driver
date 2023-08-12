#!/bin/bash

echo "Starting container..."
docker run -d -p 8000:8000 surrealdb/surrealdb:nightly start --user root --pass root -- "memory"

echo " "
echo "Container is running!"
echo "Waiting 5 seconds"
echo "-----------------------------------------"

sleep 5
