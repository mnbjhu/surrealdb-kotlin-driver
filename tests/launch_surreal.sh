#!/bin/bash

# This script launches the surreal environment
docker run -p 8000:8000 -v /mydata:/mydata surrealdb/surrealdb:latest start --log trace --user root --pass root file:/mydata/mydatabase.db
