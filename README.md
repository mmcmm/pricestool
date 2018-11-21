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


