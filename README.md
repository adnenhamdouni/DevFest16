DevFest 2016 CodeLab

----------------------------------
Init firebase project 
----------------------------------

1. Fichier build.gradle au niveau du projet (<project>/build.gradle) :

buildscript {
dependencies {
// Add this line
classpath 'com.google.gms:google-services:3.0.0'
}
}

2. Fichier build.gradle au niveau de l'application (<project>/<app-module>/build.gradle) :

...
// Add to the bottom of the file
apply plugin: 'com.google.gms.google-services'

Firebase Analytics inclus par défaut help_outline

3. Enfin, appuyez sur Synchroniser dans la barre qui apparaît dans l'environnement de développement intégré


