# IMDb Project

## Empathy.co Academy: Learning Project

We´ve created an IMDb-like search engine based on different filters using the IMDb's data sets.

### Tech stack

- Java
- Maven
- SpringBoot
- Elasticsearch
- Docker

### Installation

Clone the repository in your local machine:

```
git clone https://github.com/alvarorg14/Academy_Project.git
```

Then, go to the project folder:

```
cd Academy_Project
```

Deploy the containers:

```
docker-compose up --build
```

Finally, if you want to stop the containers:

```
docker-compose down
```

### Docker Images in Docker Hub

The docker images of both containers are also upload to Docker Hub. You can find them here:

#### Elasticsearch

[Docker Hub Repository](https://hub.docker.com/repository/docker/alvarorg14/imdb)

```
docker pull alvarorg14/imdb:es_index
```

#### Search API

[Docker Hub Repository](https://hub.docker.com/repository/docker/alvarorg14/search)

```
docker pull alvarorg14/search:latest
```

## API Documentation

The API documentation is also available in the following link when the system is deployed:

```
http://localhost:8080/swagger-ui/index.html
```

### Endpoints:

#### `GET /search`

It´s used to search movies using filters

##### Parameters:

- **genres** (String) - Value of genres to filter by multiple genres. It should be sent separeted by commas (e.g
  genres=Action,Sci-Fi)
- **type** (String) - Value of title type to filter by values. It should be sent in the same way as genres parameter
- **maxYear** (Integer) - Max value of start year to filter by
- **minYear** (Integer) - Min value of start year to filter by
- **maxMinutes** (Integer) - Max value of runtime minutes to filter by
- **minMinutes** (Integer) - Min value of runtime minutes to filter by
- **maxScore** (Double) - Max value of average rating to filter by
- **minScore** (Double) - Min value of average rating to filter by
- **maxNHits**  (Integer)  - Upper bound of the number of hits returned (500 by default)
- **sortRating** (String) - (asc/desc) The ordering of the sort

Example: `http://localhost:8080/search?maxYear=2022&minYear=2019&genres=Action&sortRating=asc`

#### `GET /search/names`

It's used to search information about directors and actors

##### Parameters:

- **ids** (String) - Nconsts of the people to search. Can be multiple ones separated by commas

Example: `http://localhost:8080/search/names?ids=nm0000093,nm0000095`

#### `POST /index`

It´s used to create an index of documents from different files

Parameters:

- **basics** * - File containing the basics information (title.basics.tsv)
- **ratings** * - File containing the ratings information (title.ratings.tsv)
- **akas** * - File containing the ratings information (title.rating.tsv)
- **principals** * - File containing the principals information (title.principals.tsv)
- **crew** * - File containing the crew information (title.crew.tsv)
