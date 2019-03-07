# Monopoly
My renditions of the classic board game typically written in java
This is version 6 and the first version made with Maven

## Disclaimer
All rights belong to Hasbro. This project was made as an educational/practice project with no objectives to sell or distribute for any monetary gain. This is a personal project to challenge myself and as something to add to a potential portfolio of private work. 

## Getting Started
Languages: Java
Required: Maven, Project Lombok, Gson, JUnit

## Prerequisites

#### For MacOS
---
Install Homebrew

```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

Install Maven via Homebrew

```
brew install maven
```
#### For Windows
---
Just use an IDE that can support it like InteliJ or Eclipse. 

### Installing
##### Local Deployment
---

To launch the service locally with a JVM:   
Ensure your Java version and compiler is 1.8 compliant
```
java -version
javac -version
```

Compile and build runnable jar in maven project. Run the following in the base directory of the project.
```
mvn clean package -DskipTests
```

You should see a jar file name
```
Monopoly-1.0-SNAPSHOT-jar-with-dependencies.jar
```
Before running the jar, you must ensure that the logs, errorlogs, resources, saves, and textures folders
are in the same folder as the jar-with-dependencies. After that, run it like you would any application. 
Double click and play away.

Otherwise, if you have Eclipse, you can import it, right click the project > export > Java > Runnable Jar File > set "Launch configuration" to "MonopolyV6 - MonopolyV6" or "Starter - MonopolyV6", set your export destination, under "Library handling" select "Package required libraries into generated Jar", DON'T save as an ANT script, and press finish. Just like above, make sure the needed folders are in the same directory as the jar file.

If you don't want to/can't do literally any of this, I understand and a (potentially outdated) jar file is provided in the main directory amongst the needed resources and folders so it can be started "right out of the box". Depending on how well I made this, however, could have varying results depending on your operating system. But this is Java... so it should work.

## Built With
 * [Maven](https://maven.apache.org/) - For project and dependency Management
 * [Gson](https://github.com/google/gson) - For cross platform serializing 
 * [JUnit](https://junit.org/junit4/) - For unit testing
 * [Project Lombok](https://projectlombok.org/setup/overview) - For generating boilerplate code
 
## Author
**Miguel Salazar**

## License
I don't have a license for this... Not sure how to even get one or if it needs it. If you find this repo
and want to use some code, I'd ask that you include my github [MigsSalazar](https://github.com/MigsSalazar) but let's be real, I'm not going to hunt you down for this stuff. I'm aware github has a way to assign a license but I haven't read through them to make a decision. This also isn't really good for repurposing so use at your own risk.

## Acknowledgments
This project was made with JUnit, Gson, Project Lombok, and Maven. I am certain I am missing some very important info regarding licenses but I don't know how to properly cite that information but here's my best shot.
 * JUnit4 is licensed under the Eclipse Public License - v 1.0
 * Gson is licensed under the Apache License 2.0
 * Project Lombok is licensed under the MIT License
 * Maven is licensed under the Apache License 2.0
