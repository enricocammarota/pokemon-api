FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer = "enricocammarota"

VOLUME /tmp

ADD target/pokemon-api.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
