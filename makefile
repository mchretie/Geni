.PHONY: test
.PHONY: pack
.PHONY: run

test:
	mvn test

pack:
	mvn package

run-client:
	mvn clean javafx:run

run-server:
	mvn compile exec:java -Dexec.mainClass="ulb.infof307.g01.server.LaunchServer"

run-client-mobile:
	flutter run mobile_deckz
