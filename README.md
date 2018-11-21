# pricestool


## Building for production

To optimize the pricestool application for production, run:


To ensure everything worked, run:


## Testing

To launch your application's tests, run:

    ./gradlew test


### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Then, run a Sonar analysis:

```
./gradlew -Pprod clean test sonarqube
```


## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a  database in a docker container, run:

    docker-compose -f src/main/docker/.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    

Then run:

    docker-compose -f src/main/docker/app.yml up -d


