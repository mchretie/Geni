.PHONY: test
.PHONY: pack
.PHONY: run

test:
	mvn test

pack:
	mvn package

run:
	mvn clean javafx:run
