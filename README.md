# Monopoly

My renditions of Hasbro's classic board game!

Why would I do this to myself multiple times you may ask? Simple! Besides hating myself and also trying to get rid of any friends I may have, recreating Monpoly with full graphics has given me a consistent challenge to code. If I don't like the way something looks in one version, I have the oppertunty to make it better in the future. This gives me new problems to solve, new oppertunities to learn other frameworks or practices to enhance my skills, and gives me a change to reflect on my old code. In addition, seeing how each iteration improves, while gives me a sense of pride to see how I've progressed, also gives me a bench mark of where I am now as I try to pour my all into the project letting me know where my strenghts and weaknesses lie. I highly encourage others to pick a project, be it Monopoly or something else, that you can create over and over again to improve your craft.

## Monpoly 4

For the most part, this is an obsolete project that has more or less been thrown away. Nevertheless, it was the first fully operation Monopoly game I made. The three version prior are not included for this reason, they were made so poorly, I trashed them and started over before they were fully playable games. Amongst the issues I wanted to improve upon from Version 4 into Version 5 was certainly the graphics and overall flow of the game. All the notifications were handled using annoying popups and rolling requires the player to go through a toolbar menu. Those are the main problems but there are certainly many many more that I wanted to fix.

### How to run Monopoly 4

Considering this is really old and the source code is gone with the wind, the only thing you can do is download the whole folder from here on github, and run the executable .jar file. Since it's Java, we all know it's portable so long as the resources folder is in the same directory. For compatability, run this on Java 1.7. It should still work on 1.8 though.

### Used Open Source Projects for V4

None. This didn't use any outside code at all. That was a mistake. It was brutal

## Monopoly 5

Right away, I hope the improvement in graphics is noticable. If not, then I have a terrible eye. By this point, I was very happy with the graphics. Regions were easily mapped, the notifications had a designated space, you could see whose turn it was, and there was some attempt at stylizing the GUI with borders. However, while the main design looked nice, underneth, the mechanics were messy. I didn't stick too closely with the Model-View-Controller pattern all too well which really came back to haunt me as the code grew. In addition, each square on the grid could only hold one icon at a time so if I wanted any layered images or a different colored background, I'd have to do it by hand. This was rather annoying when all I wanted was just the player pieces to move. Yes, more work needed to be done

### How to run Monoply 5

Having been a novice developer at the time, I didn't use Maven or Gradle to manage anything, but instead relied on my Eclipse IDE to do all the hard work. If using Eclipse Neon, right click the project folder (MonopolyV5) and select Export. From there, Under Java select "Runnable Jar File" > "Next", and in the following dialog box

1. under launch configurations, select "Main - MonopolyV5"
2. under Library Handling, select "Package Required libraries into generated JAR"
3. make sure "Save as ANT Script" is NOT selected
4. select the export destination as the location where all the resource folders are in
5. Click "Finish"

And you should be all set! Go ahead and try to un it! (I'm sorry)

### Used Open Source Projects for V5

I only used [Junit 1.4](https://junit.org/junit4/) and [Google's Gson](https://github.com/google/gson). Unfortunately, I never used Maven or Gradel (or any other dependency manager) sooo.... the compiled jars are included within this project. I don't remember seeing anywhere where that's wrong to do, my bad if it is, but if this project seems even bigger than it should be, that might be why.

## Monopoly 6

Now this one I'm rather proud of (but I'm slowly disowning it like I've done the last two). The goal of this version was to generalize and optimize the board such that it was open to texture packs. In other words, the board shouldn't care about the resources input, so long as it fit a simple format, it could load it. And for the most part, it worked. I managed to make a texture for a company that I was interning at which worked beautifully after some debugging. Granted, I knew what everything meant so it wasn't simple for someone else to learn. On top of that, the configuring .json file was huge! Using Gson's "pretty printing", the file was upwards for 10k lines! (granted, each element of an array, even a 30x30 2D array, was given it's own line, which accounted for most of that bulk) Without "pretty printing" though, that would all get compressed onto 1 line. But the goal was acheived and the project was better, so overall, this was a success.

### How to run Monopoly 6

Finally, a project that uses a dependency manager. That took way longer than it should have. (I blame my first uni that refused to teach its students about maven, gradle, or even pip. Seriously! Not even pip!?). So for this project, you need maven and, if you use an IDE, Project Lombok. Thankfully, that's not as bad as it sounds. Project Lombok has everything you need to know [here](https://projectlombok.org/) to install it on your IDE or to just run out of the box with Maven or Javac. To run with maven, preferably, you want to ignore failed tests since... well... I haven't implemented many of them. For that, you want to package the project in the following way:

`mvn package -Dmaven.test.failure.ignore=true`

After that, take the created jar, slap it in the same directory as the "resources" and "textures" folders. Sorry I didn't follow Maven conventions. Promise that will happen in the next version.

### Used Open Sources Project for V6

1. [Project Lombok](https://projectlombok.org/) for writing miles of boiler plate code
2. [Google's Gson](https://github.com/google/gson) for reading and writing json files
3. [Apache Log4j 2](https://logging.apache.org/log4j/2.x/) for translating dolphins (logging)
4. [Junit 1.4](https://junit.org/junit4/) for crying over Harambe (unit testing)
5. [Mockito](https://site.mockito.org/) for behavior testing

All of these free source softwares now hold a special place in my heart for making my life easier. Thank you all. My code does not deserve any of you

## Monopoly 7
This is the first time I'm starting the README before I finish the project but the goals for this one were made in the first commit so to remind myself of what they are and to make clear my design decisions for anyone that sees this repo, I've included all of them:
Goals:
 - no more JButtons or JLabels for displaying stickers. All images!
 - make the board 100% independent from Monopoly
 - simplify/break up the Environment class
 - better image resource handling
 	 - find, load, and save images without 10 million edge cases
 	 - keep better track of displayed images, esspecially between copies
 	 - actually follow Maven conventions with resources
 - better serialization/de-serialization to allow backwards compatibility without making plain text save files
   - huffman trees?
   - encrypted jsons?
   - smoke signals?
 - LESS POPUP MESSAGES!!! I THOUGHT V5 WAS SUPPOSED TO FIX THAT
 - Less dependency on the Environment class
 - better power separation/adherence to the model-view-controller pattern
 - possibly opening it up to webapp/online multi-player?
 - make building textures easier
 - open the doors to house rules
 	 - Free Parking lotto?
 	 - 1 rotation around the board before purchases can be made?
 	 - No auction?
 	 - endless house upgrades?
 	 - teaming up?
 - decent logging from the start!
 	 - meaningful logging info
 	 - using the correct calls for the correct situations
 	 - log instead of crashing
 	 - don't create 40GB worth of logging files
 		 - it's embarrassing to admit that
 - allow undo's?
So obviously I have my work cut out for me. I'll be including these goals in a separate README contianed within the project folder except I'll make these goals as a task list and eventually I'll remove these goals from this master README when that happens. But for now, on to the run instructions!
### How to run Monopoly Version 7
So just like Monopoly Version 6, we need [Project Lombok](https://projectlombok.org/) installed in our IDE to prevent IDE errors or we need to [Delombok](https://projectlombok.org/features/delombok) the project to avoid all of that. If you skipped over that part, Project Lombok handles a bunch of boiler plate code like getters and setter, constructors, and logging componenets with annotations instead of code. This comes with the disadvantage that its not... compiler portable?... IDE portable?... developer portable?... It will still run on any JVM but there's extra steps to compile it. [I recommend going to Project Lombok's site by clicking any part of this sentence as they do a better job of explaining this.](https://projectlombok.org/) Besides that, I currently have 3 working maven builds to choose from depending on your needs.
#### Compile to a JAR
This one is rather simple as it's just one Maven command
```
mvn package -Dmaven.test.failure.ignore=true
```
This will build the jar with the dependencies included within the jar as well as copy all of the resources as a separate folder within target. This jar is also executable so once the process has completed, you can start playing right away by double clicking the jar! I also ignore running any tests because I have yet to write any and, knowing me, I'll auto generate a thousand tests using Eclipse and they'll all fail because I don't implement any of them. Give me some time with this one.
#### Generate public JavaDocs
This next one does what the title says, generates a java doc of the whole project. That's right! I write documentation! It crushes my soul just like everyone else, but I strive to make my code readable to everyone, esspecially me three months from now. This command is as generic as it gets with the following command
```
mvn javadoc:javadoc
```
I include it in case there's anyone that doesn't know how... like me three minutes ago.
#### Generate developer JavaDocs
Thats right! Not only do I write documentation for casual developers who want to use my code, I also write documentation for psychopaths that also want to modify my code! Why are they psychopaths? Because only someone that insane would subject themselves to the torcher of reading my code. However, thanks to my fine grain documentation written for people who want to change/read/modify the source code, this pool of insane developers has widened beyond total psychopaths to partial psychopaths as well. That command goes as follows:
```
mvn javadoc:javadoc -Dshow="private" -DdestDir="[you/devdocs/directory]"
```
Note, maven will generate these developer JavaDocs relative to your "target/site/apidocs" within the project directory but does accept "/../" within the path. For my own developer JavaDocs, I use "/../apidevdocs/" as my destination directory and that works out just fine for me
### Used Open Sources Project for V7
(this was basically copied straight from V6)
1. [Project Lombok](https://projectlombok.org/) for writing miles of boiler plate code for me
2. [Google's Gson](https://github.com/google/gson) for reading and writing json files
3. [Apache Log4j 2](https://logging.apache.org/log4j/2.x/) for enumerating dolphins (for logging)
4. [Junit 1.4](https://junit.org/junit4/) for crying over Harambe (unit testing)
5. [Mockito](https://site.mockito.org/) for behavior testing
