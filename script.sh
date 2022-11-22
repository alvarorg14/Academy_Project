#!/bin/bash
docker-compose -f docker-compose.back.yml up --build -d
sleep 10

curl -X PUT http://localhost:8080/index/create
curl -F basicsFile=@IMDb/title.basics.tsv -F ratingsFile=@IMDb/title.ratings.tsv -F akasFile=@IMDb/title.akas.tsv -F crewFile=@IMDb/title.crew.tsv http://localhost:8080/index/imdb
