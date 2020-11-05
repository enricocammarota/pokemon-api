## Pokemon Shakespearean API

The Pokemon Shakespearean API has the purpose of retrieving Pokemon description in a Shakespearean way.

The API retrieves the description from the `https://pokeapi.co/` and then passes it to the Shakespeare API 
`https://funtranslations.com/api/shakespeare` in order to retrieve a brand new description.

## Hot to run

Build using Maven.

```bash
mvn clean install
```

If you want to additionally build the docker image, you can run with a profile as follows:
```bash
mvn clean install -P docker
```

And you're now ready to query the API:

```bash
curl --location --request GET 'http://localhost:8801/pokemon/<pokemon-name>'
```


## Running the application locally

There is a compose file you can use:

- docker-compose.yml 

The file is configured to run the application on port 8801 and therefore will be possible for you 
to query the API through that port. 

```bash
docker-compose up
```
