GustaveTutorServiceClient utilise :
- activation.jar
- jfxrt.jar
- javax.mail.jar

Les web services utilisent JavaSE-1.8, et, je ne comprend pas pourquoi, avec JavaSE-1.8, ces fichiers sont nécessaires, pour le bon fonctionnement de la classe WebView/JavaFX.

Aussi, pour que le projet se lance sans erreurs apr rapport à JavaFX, il faut que JavaFX dans Java Build Path soit ajouté en premier avant les autres librairies (dans le cas contraire, il faut tout retirer, puis rajouter JavaFX, et enfin les autres librairies).