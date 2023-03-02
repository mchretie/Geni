# Génie des cartes

## Conventions Java

Ceci sont des conventions pour tout le code, que ce soit dans `src` ou `test`. S’il y a des conventions spécifiques pour l’une ou l’autre partie, celles-ci sont mentionnées dans le dossier approprié.

### Formatage

Le formatage du code source est géré par `clang-format`. Pour ne jamais l’oublier, exécutez ces commandes :

```bash
chmod +x hooks/pre-commit
ln hooks/pre-commit .git/hooks
```

Vous pouvez formater le code ajouté à Git avec :

```bash
git clang-format -v -f
```

### Structure du code

Veillez à suivre les conventions/pratiques suivantes :

- chaque classe, interface ou énumération doit être dans son propre fichier du même nom ;
- évitez les `import java.*`, seulement ce dont vous avez besoin ;
- ne mettez en `public` que le strict minimum dans vos classes ;
- mettez en `final` le plus de variables possible (si approprié) ;
- commentez le strict minimum nécessaire pour comprendre le code ;
- créez un nouveau `package` si plusieurs classes peuvent être groupées.

## Conventions Git

Veillez à suivre les conventions/pratiques suivantes :

- tout est en anglais ;
- utilisez une nouvelle branche pour tout travail ;
- mergez dès que vous avez fini et que les tests passent.

Voir [1] pour bien écrire des commits.

[1] https://cbea.ms/git-commit/#seven-rules

## Adding JavaFX to Intellij

**[Official doc](https://openjfx.io/openjfx-docs/#install-javafx)**

### Linux


Download JavaFX SDK 19 from [here](https://gluonhq.com/products/javafx/) and extract it.

Get the path to the lib folder (f.e. JAVAFXSDK = /home/user/Downloads/javafx-sdk-19/lib)

Go to ```File -> Project Structure -> Project```, and set the project SDK to 19.

Still in the Project Structure, go to ```Libraries```, and add the JavaFX SDK, which was downloaded earlier, as a
library.

Finally, add this line to the run configuration VM options:

```bash
--module-path JAVAFXSDK --add-modules javafx.controls
```

### Mac

I don't see why this wouldn't work for all operating systems but I only did this on macOS.

1. Download JavaFX SDK 19 from [here](https://gluonhq.com/products/javafx/) and extract it.

2. Get the path to the lib folder which is inside the extracted file (e.g ```/home/user/Downloads/javafx-sdk-19/lib```)

3. Go to ```File -> Project Structure -> Project``` and set the project SDK to 19 (normally should be done already).

4. Then go to ```Project Struture -> Libraries``` and add the path to the JavaFX SDK lib that you got in step 1.

5. Once that is done go to ```Run -> Edit Configuration```. If you don't already have a configuration set up click
   ```+ -> Application ```and enter the path to the ```Main``` class.

6. Finally, still in the ```Edit Configuration``` window, press ```Modify options``` which is to the left of
   ```Build and run```. In the text box that appears, paste the following command:
   ```--module-path {Path to JavaFX lib file (step 1)} --add-modules javafx.controls ```

### Windows

triple no

#### Still stuck ?

that sucks