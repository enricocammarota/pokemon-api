# hbi-order-taker-com.truelayer.service

Consumes new orders and generates an order number. Orders are then sent to
Kafka for downstream processing by hbi-order-event-com.truelayer.service and hbi-as400-adapter

## Build

- Set up a personal access token in GitLab to pull down any dependencies and ensure you have configured the token in your `.m2/settings.xml`, e.g.:

``` xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">
<servers>
    <server>
    <id>gitlab-maven</id>
    <configuration>
        <httpHeaders>
            <property>
                <name>Private-Token</name>
                <value>MY_TOKEN</value>
            </property>
        </httpHeaders>
    </configuration>
</server>
  </servers>
</settings>
```
- Build using Maven.

```bash
mvn install
```

If you want to additionally build the docker image, you can run with a profile as follows:
```bash
mvn install -P docker
```


## Running the application locally

- Configure the multi-container environment:
There are 2 compose files you can use:
  - docker-compose.yml (This one starts the application along with all dependencies e.g. Kafka, Postgres) 
```bash
    docker-compose up
```

  - docker-compose-dependencies.yml (This one only starts the dependencies.
Useful if you want to run the application in IntelliJ) 
```bash
    docker-compose -f docker-compose-dependencies.yml up
```

Both compose files, start up a local instance of Postgres on port 5432. The docker image used for Postgres
also comes with a GUI which can be access on port 8080.

## Swagger local
Accessible from http://localhost:8801/swagger-ui.html
