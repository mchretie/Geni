# Librairies

## Ajout d’une nouvelle librairie

Si vous voulez ajouter une nouvelle librairie au projet, télécharger ses archives `.jar`, ajoutez les dans un dossier à son nom ici et mettez des instructions pour l’ajouter au projet IntelliJ.

## Instructions

Les librairies suivantes sont utilisées :

- JavaFX (19.0.2.1)
- Junit (5.9.2)
- SQLite-JDBC (3.41.0.0)

### JavaFX

Ajouter la librairie :

1. `File > Project Structure… > Libraries`
2. `+ > Java > lib/javafx/[linux|mac|win]` selon l’OS du système

Configurer le lancement de l’application :

1. `Run > Edit Configuration`
2. `+ > Application`
3. Sélectionner la classe `Main`
4. `Modily options > Add VM Options`
5. Entrer comme option `--module-path lib/javafx/[linux|mac|win] --add-modules javafx.controls` selon l’OS du système

### Junit

1. `File > Project Structure… > Libraries`
2. `+ > Java > lib/junit`

### SQLiteJDBC

1. `File > Project Structure… > Libraries`
2. `+ > Java > lib/sqlite-jdbc`