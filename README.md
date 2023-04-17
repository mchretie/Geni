# Génie des cartes

Une application d’étude à répétition espacée.


## Compilation et lancement

### CLI

La compilation et la gestion des dépendances sont gérées par `maven`. Ainsi, exécuter :

```bash
mvn test        # pour tester le programme
mvn package     # pour générer le .jar
mvn javafx:run  # pour lancer l’application frontend
mvn compile exec:java -Dexec.mainClass="ulb.infof307.g01.server.LaunchServer" # pour lancer le backend
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

## Librairies externes

Les librairies suivantes sont utilisées :

- Junit (5.9.2)
- SQLite-JDBC (3.41)
- OpenJFX (19)
- Ikonli (12.3.1)


## Contributions

Voir le fichier `CONTRIBUTING.md`.
