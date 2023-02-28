# Genius Project 2023

## Pre install

### Linux

**[Official doc](https://openjfx.io/openjfx-docs/#install-javafx)**

Download JavaFX SDK 19 from [here](https://gluonhq.com/products/javafx/) and extract it.

Get the path to the lib folder (f.e. JAVAFXSDK = /home/user/Downloads/javafx-sdk-19/lib)

Go to ```File -> Project Structure -> Project```, and set the project SDK to 19.

Still in the Project Structure, go to ```Libraries```, and add the JavaFX SDK, which was downloaded earlier, as a
library.

Finally, add this line to the run configuration VM options:

```bash
--module-path _JAVAFXSDK_ --add-modules javafx.controls
```

### Mac

no

### Windows

triple no

#### Still stuck ?

that sucks