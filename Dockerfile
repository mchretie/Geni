FROM maven:3-amazoncorretto-19-debian-bullseye

COPY . /app
WORKDIR /app
CMD ["mvn", "compile", "exec:java", "-Dexec.mainClass=ulb.infof307.g01.server.LaunchServer"]

EXPOSE 8080

