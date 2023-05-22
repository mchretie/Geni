# Génie des cartes

Une application d’étude à répétition espacée.


## Compilation et lancement

### CLI

La compilation et la gestion des dépendances sont gérées par `maven`. Ainsi, exécuter :

```bash
mvn test        # pour tester le programme
mvn package     # pour générer le .jar
mvn javafx:run  # pour lancer l’application frontend
mvn compile exec:java -Dexec.mainClass="ulb.infof307.g01.server.LaunchServer" # pour lancer le serveur
```

Un makefile avec les cibles `test`, `pack`, `run-server` et `run-client` est également fourni.

### IntelliJ

Il faut rajouter une configuration de lancement comme suit :

`Run` → `Edit Configurations…` → `+` → `Maven`

Et mettre la commande suivante dans le champ `Command line` :

```bash
javafx:run
```

Vous pouvez aussi lancer le backend en créant une configuration de lancement comme suit :

```bash
compile exec:java -Dexec.mainClass="ulb.infof307.g01.server.LaunchServer"
```

## Variables d’environnement

Vous pouvez modifier les variables d’environnement dans le fichier `.env` à la racine du projet.

## Librairies externes

Les librairies suivantes sont utilisées :

- Junit (5.9.2)
- SQLite-JDBC (3.41)
- OpenJFX (19)
- Ikonli (12.3.1)
- Dotenv-Java (3.0.0)
- Spark (2.9.4)
- Gson (2.10.1)
- JJWT (0.11.5)
- JSoup (1.15.3)

## Application mobile

Voir le fichier `mobile_deckz/README.md`.

## Docker

Pour lancer le serveur dans un conteneur Docker, exécuter :

```bash
docker build -t genie-des-cartes .
docker run -p 8080:8080 genie-des-cartes
```

L'application n'a pas de volume, donc les données sont perdues à chaque redémarrage du conteneur.
Ceci n'est pas un problème pour le développement, mais il faudrait utiliser un volume pour la production.

## Contributions

Voir le fichier `CONTRIBUTING.md`.
