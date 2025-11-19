Requires Java 21+
Gradle 8.2+

Clean gradle
```
./gradlew clean
```

Compile build
```
./gradlew build
```

Package to portable .exe
```
./gradlew jpackagePortable
```

Play with Java options to fix any render issues
```
Wookster/app/Wookster.cfg

java-options=-Djpackage.app-version=1.0.0
java-options=-Dsun.java2d.opengl=true
java-options=-Dsun.java2d.d3d=false
java-options=-Dsun.java2d.noddraw=true
```