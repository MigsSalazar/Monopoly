# A Guide to the Explorable Interface
So I'm rather excited by this interface as its vague declaration allows for some fun problem solving and creativity for other developers. Now clearly I have some high hopes for this interface but I realize the declaration and documentation I gave could use some work in describing the scope of what's possible with this interface. What I wrote was long and ranty but I wanted to make apparent that I want future developers (aka me and my small chimp brain) to try and think outside of the box with this interface. So down below, I want to provide a bigger, beefier set of descriptions, explanations, and examples to the docs I provided.

## Reading the Documentation
By this point, a full 5 minutes after the I first wrote the comment for the ```getReachablePoints``` method, I already hate it and know I'm going to change it so I'm gonna rewrite it here.

### What this Interface is NOT Intended to Handle
The goal of this interface, or rather any class that implements it, is to be used soley for finding reachable coordinates given a set of passed in information. The classes that implement this interface SHOULD NOT keep track of any game pieces themselves nor whould they really have any memory or awareness of the environment around them. In many ways, this method should be treated as a static method that returns a value given the arguments given. However, they are not static methods because these implemented classes sometimes need to remember different sets of data depending on the instance. Some implementing classes will only have to return an adjusted list of coordinates depending on an offset, in which case, storing the list somewhere is more useful than reproducing it everytime than with a mathematical equation. Such is the case with Monopoly.

In the case where you'd rather compute the list of possible coordinates with an equation, it may be useful to pass in that equation to a constructor esspecially if there are multiple pieces with mathematical path finding but require different equations to find their paths. In this case, depending on your implementation, it may make more sense to implement a single class that takes in an equation to use for later depending on the type of game piece. Such is the case with Chess where, once again depending on your implementation, it may be better to initiate an Explorable for each kind of game piece with an equation as the equation for each game piece never changes, but the each are mostly unique and the coordinates for game pieces change constantly.

This is why it is more sensible to keep player position logic (or just player logic in general) out entirely and soley rely on abstracted paths or equations to find lists of coordinates for game pieces.

### The Return Value(s)
So as it is, the return value I hope to be rather straight forward. The method will return a list of Point2D.Double's that are reachable given the information passed in. Sound's simple enough. But why a list? In most board games, you have a position and a dice roll that result in one final position for that turn. So why a list? For a few reasons

1. Many board games don't use this schema described of "old position + dice roll = new position". Games like Chess or Clue allow you to move up to the dice roll but do not require you to consume the full value of the path limit or dice roll. 
2. Having all points available between your start and finish can be useful for game clarity as game pieces that teleport from start position to end position can be confusing for players. Having game pieces hop from space to space, like you would in a real board game such as Monopoly, helps players follow along and keep track of their own pieces which can only be done if the code knows about all positions prior to the end position.

Thus, either we need to provide a list of end positions or it's incredibly useful to have it. I'm certain there are rare cases in games where you should absolutely without a shred of a doubt only get one result from the function because of game mechanics and clarity, but in those cases, returning a list still wouldn't be bad as you could return a list of size 1. So all in all, using a List just seemed to be the best design choice as it opens the door to any and all implementations without too much additional modification or filtering of resulting end positions.

Now one last thing to note about the return value and that it can return anything of type List. That may not seem all that important but in board games, lists of positions have a way of cycling and recycling themselves so the expected logic changes. Within the util package of this project includes such a diversion in list logic with the OuroborosList and SinusoidalList. These two lists help account for repeating values in lists when the bounds of an array are exceeded. Think of Python lists where you can request indices beyond the bounds of the list and below zero to get values from the array at a different offset. The UroborosList behaves in the same way as a Python list where reaching the end will cycle you back to the beginning and requesting a value before the beginning will get you to the end. (Thus the name OuroborosList as in the Ouroboros snake)

![Ouroborus snake](https://upload.wikimedia.org/wikipedia/commons/7/71/Serpiente_alquimica.jpg)

On the other end, with the SinusoidalList, these lists act more like... well... a sinusoidal function where the list, once it reaches the highest value/index, eventually returns to the lowest value/index but not before passing through every value/index in-between. In this way it can loop forever but instead of looping from head->body->tail->head->... like the UoroborosList, the SinusoidalList must traverse the body in full before reaching the head again like so: head->body->tail->reverse body->head->...

Now I understand this is a lot to say given there's only two edge cases that are infrequently used but many developers, myself included, rely on the first to last nature of lists that stop once they reach either of these end points. Board games, however, don't give a damn about easy code, and would rather implement unique and exciting game mechanics that give developers as many headaches as countries adding new date formats. The only difference is that the new game mechanics matter because they make new board games fun and interesting where as new date formats are useless because it's just politicians and bureaucrat throwing hissy fits trying to be edgy and unique.

### The Arguments
If you've already read the documentation for the arguments, then I'm sorry you have to read more. Hopefully you fully understood the documentation (/s) so to elaborate, the arguments can be used in any number of ways limited only by the imagination of the developer. Now that alone is incredibly vague so I want to give you 3 general use casses for the arguments:
1. The Classical Coordinates
2. The Named Use
3. Everything Else
 
#### The Clasical Coordinates
This one is hopefully straight forward and what I expect to be fairly common if not the most common. This schema completely ignores the names and documentation by treating the arguments as individual coordinates. This is completely fine and I actually encourage it for games like Chess, Checkers, Clue, Betrayal at the House on the Hill, and any other sandbox type games where the player can move anywhere on the board given a set of limits or rules about where they can go. These rules are often mathematical in practice or radius dependent so it makes more sense to use the two arguments as coordinates from which to calculate all possible coordinates. The recommended usage for this case is to use the ```characteristic``` argument as the x coordinate and the ```point``` argument as the y coordinate. However, there is nothing stopping you from reversing this, using one argument as a flattened 2D argument and ignoring the other argument all together, or any other variant that you as the developer see fit.

#### The Named Use
Now this usage was the first that came to mind (after the classical coordinates usage) when I was trying to think up how to make this interface and is called the "Named Use" because it's what I decided to name the arguments after: "characteristic" and "point". The idea is that most board games are not simple rectangular 2D grids where coordinates can be found just by looking a the board. Many board games have regions or areas of influence where the location of the piece influences how/where the piece can move. Take "Betrayal at the House on the Hill" for example with it's multiple room layouts and behaviors depending on the type of room. The characteristic argument opens the door to define unique behavior depending on the terrain while remaining independent of the grid system used. That's where the point argument comes in as that's where the grid system can come into play in the form of a flattened 2D coordinate without influencing the characteristics flag. The separation between layout and coordinate means you can have multiple coordinate systems withint he same game, each independent of each other, while mainting the same schema for requesting reachable positions. The design choice to use this use case as the "names usage" was to encourage out of the box thinking or bazaar retro-fitting as needed by a developer but also provide a template by which to do it.

#### Everything Else
This is the true out of the box thinking case as it includes every and all cases that are not the two cases listed above. I mentioned some of the possibilities in the method comment but I believe the posibilities are endless. For example:
- Using both arguments as flattened 2D coordinates to allow units to move relative to others
- Using one or both arguments as a product of primes and/or -1 to define behaviors depending on the factored primes and/or negative
- Taking one argument, using its value before the decimal a flag or coordinate and the value after the decimal as another flag of coordinate allowing for up to 8 unique 2D flattened coordinate inputs or two sets of coordinates and flags
- Submitting an entire game grid and contents by using one argument to record the height and width of the grid as a decimal separated list and parsing the grid's data into the next argument treating the double like a kind of numerical string.
- If a game has multiple paths like "The Game of Life" or "Chutes and Laders", parsing the diversions into one argument as a "0" separated list can come in handy
 
An that's just what I can come up with. I'm certain there's one jerk that can make us all feel stupid by implementing an entire suite of games using bitwise functions to manipulate the logic already embedded in the doubles themselves. To that jerk, thank you for finding my code and using it. May never happen, but you're the reason I'm writing this massive doc for this tiny little interface. So that someone can take this to extreme I never imagined.
 
## Suggestions for Implementation
With all that I've written, you'd think I'd be out of things to say, but to the contrary, I'm only half finished. That's right! I got a crap ton more! Thankfully for you dear reader, this part isn't necessary to read. This is just a list of ideas I had for implementations posibilities for the Explorable interface for different board games. If you only care about changing the Monopoly game, then there's literally no reason for you to read this beyond hoping for a little inspiration. For those of you who want to use my code for something else (unlikely) or to make your own board game, this will be helpful to give you insights or even use as a guide on how to use the Explorable interface given the board game you want to build.

### Monopoly
You know this was coming first

**Using Classical Coordinates**
- Returns an UoroborosList of Point2D.Doubles starting at the passed in coordinates. This is would be returned to the caller for the caller to determine how far and how to use the returned points

**Using the Named Use**
- The characteristic double is a whole value between 0-3(inclusive) stating which side of the board the user is on. The point is then a whole number between 0-10(inclusive) describing how far along the side the piece is positioned. The return is the same as classical coordinates, a list of Point2D.Double's declaring the coordinates to follow.

**Other Uses**
1. As a space type search engine: Flatten one of the earlier two schemas into one argument and then use the other as a flag to denote what kind of spot to search. For example, the nearest railroad going forward would be enumed to .1, the nearest utility going forward to .2, go to .3, a specific property to .4 with the preceding whole number being a flattened 2D coordinate, .5 being go directly to jail, and any negative number meaning to include the nearest spot going backwards and forwards. This would return a LinkedList starting from the passed in position to the final position requested.

### Chess and Checkers
Despite the complexity of Chess and the simplicity of Checkers, the logic behind both are very similar as Checkers really just uses an army of short ranged Chess bishops that can be crowned into long ranged Chess bishops.

**Using Classical Coordinates**
- Assuming each game piece uses a different Explorable instance, simply passing in the coordinates would allow the explorable to include all possible moves for each piece. Filtering out invalid moves would be left to the caller

**Using the Named Use**
- Using this paradigm, a developer could feasibly have only one instance of Explorable where the point argument would be a flattened 2D coordinate and the characteristics argument would specify what kind of piece is moving. In this case, filtering out invalid moves would be left to the caller

**Other Uses**
- Sending the whole board! This one is a bit extreme but possible. This methods requires the full use of the double while completely ignoring its numerical value. Instead, you encode all 64 spots of the board into all 62 bits of the double, treating it like a abstract bit type rather than a double! For this, you would encode 1 as friendly pieces that are illegal to land on and 0 as empty spaces or enemies which are legal to land on (assuming it can legally reach those spots). The second argument can then contain the coordinates as a flattened 2D coordinate as the whole value and the type of unit in the decimal. Then, the whole thing can be positive or negative to denote the color of the piece (white or black). This allows the Explorable instance to filter out invalid moves as well as return legally reachable moves.

