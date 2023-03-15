# Contributions

## Conventions Java

Ceci sont des conventions pour tout le code, que ce soit dans `src` ou `test`. S’il y a des conventions spécifiques pour l’une ou l’autre partie, celles-ci sont mentionnées dans le dossier approprié.

### Formatage

Le formatage du code source est géré par IntelliJ, n’oubliez pas de vérifier que c’est bien fait avant de commit.

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
