#!/bin/bash

echo "Starting elastic and API containers..."
docker-compose -f docker-compose.back.yml up --build -d
sleep 10

echo "Creating the index..."
curl -X PUT http://localhost:8080/index/create

echo "Indexing the data..."
curl -F basicsFile=@IMDb/title.basics.tsv -F ratingsFile=@IMDb/title.ratings.tsv -F akasFile=@IMDb/title.akas.tsv -F crewFile=@IMDb/title.crew.tsv http://localhost:8080/index/imdb

echo "Stopping containers..."
docker-compose down

echo "Creating image..."
docker commit elasticsearch alvarorg14/imdb:es_index

echo "Pushing image to Docker Hub..."
docker push alvarorg14/imdb:es_index