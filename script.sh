#!/bin/bash

echo "Extracting data..."
unzip -o IMDbData.zip

echo "Starting elastic and API containers..."
docker-compose -f docker-compose.back.yml up --build -d
sleep 30

echo "Creating the index..."
curl -X PUT http://localhost:8080/index/create

echo "Indexing the data..."
curl -F basics=@IMDbData/title.basics.tsv -F ratings=@IMDbData/title.ratings.tsv -F akas=@IMDbData/title.akas.tsv -F crew=@IMDbData/title.crew.tsv -F principals=@IMDbData/title.principals.tsv http://localhost:8080/index/imdb

echo "Stopping containers..."
docker-compose down

echo "Creating image..."
docker commit elasticsearch alvarorg14/imdb:es_index

echo "Pushing image to Docker Hub..."
docker push alvarorg14/imdb:es_index