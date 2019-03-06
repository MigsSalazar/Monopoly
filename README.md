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

