# Academy Project

## How to deploy the project using Docker

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

### Important!!

If you want to start again the containers without losing the indexed data run:

```
docker-compose up
```
