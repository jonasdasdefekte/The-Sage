# The Sage
*A mechanic who studied about the cult of a blue bird.  
She learned flying a carpet and brewing potions in order to stop its rise.*

## Content

This mod for the brilliant game Slay the Sprire introduces:
- The Sage, a new character to play with currently... 
    - 59 cards
    - 4 Sage-only relics
    - 1 Sage-only potion
    
- one event for non-Sage characters
- 2 relics for any character
- 1 potion for any character

For details, feel free to check out our [design document](https://github.com/jonasdasdefekte/The-Sage/blob/master/The%20Sage "The Sage").

## Gameplay

#### Flight

The Sage's starting Relic is the **Flying Carpet**.

![Flying Carpet](https://github.com/jonasdasdefekte/The-Sage/blob/master/src/main/resources/sage/relics/Flying_Carpet.png "Flying Carpet")

*At the start of your second turn, gain 1 Flight.*

Flight is the same buff that we all know from our beloved Byrd friends. This changes the gameplay quite drastically, since the enemies' attacks are altered: As long as you have Flight, you will take 50% less attack damage. But beware, for each time you take unblocked attack damage, you will lose one charge! And surviving without Flight can be -- tough, especially because you don't start with the healthiest amount of max HP.

#### Brewing

Many cards from the Sage allow you to Brew potions.

![Brewing Potions](https://github.com/jonasdasdefekte/The-Sage/blob/master/src/main/resources/sage/cards/Quackster.png "Quackster")

**Quackster** (Uncommon Skill, 0 Energy): *Brew a random potion in 4 (3) turns.*

Every basegame character has its special take on a card type: The Ironclad likes Attacks, The Silent goes well with Skills and The Defect is specialized in playing Powers. Rather than introducing a new card type, this character focusses on another game element: Potions.  
Usually you have to wait a couple of turns before your potions are ready to use. But there is also a Brewing power that will help brewing your potions faster. For example, when playing the above card, Quackster with 2 Brewing power, you will recieve your random potion not in 4 but in 2 turns. The potion mechanic introduces many new tactical decisions because you will can only hold a couple of potions at the same time.

#### Other mechanics
Further mechanics included in the Sage's card set are:
- X cost cards (all being represented by guidebooks),
- Poison (also uses poison relics from The Silent),
- profit from Frail on the character,
- profit from Artifacts on the enemy.

Each of them can be a winning condition for your deck playing with the Sage!

## Our plans for the Sage
- **Balance** (we have been playtesting since end of 09/2018, most things should be reasonable by now but we are still tweaking here and there, feedback is welcome);
- growing the card set to **full size** of 75 (feel free to share your ideas if you like to);
- card **art** will be added in slow pace (Jonas has already made some and just finished the character art);
- full **German** localization (since we are both Germans);
- maybe we'll come up with some more suprises?

## How To Mod The Spire
If you have found this, chances are that you know how this works. Still we like to welcome everybody, so if you aren't familiar, you can look at this [tutorial video](https://www.youtube.com/watch?v=AAhs55x7oFo) by Rhapsody (the basics still work the same way).  
This mod requires to use [ModTheSpire](https://github.com/kiooeht/ModTheSpire/releases) as well as [Base Mod](https://github.com/daviscook477/BaseMod/releases).  
We want to thank the active modding community of this game to make such a project possible!

## Building (only important for Modders)
This Mod uses Gradle for building. After making changes, you can double click buildMod.cmd 
(or run ./gradlew build or gradlew build in a console) to 
compile it and build the jar. The Mod will be in the /build/libs folder. Copy it over to 
your mods directory and test it out.

If the task fails and there is no jar in /build/libs make sure you have a folder named 
lib next to the folder of this Project (not in it) where you have the jars of ModTheSpire, 
BaseMod and the Game's jar (desktop1.0.jar)

Your directory structure should look like this:
```
git (or wherever you store your git projects)
   \_The-Sage
             \_src...
   \_lib
        \_ModTheSpire.jar
          BaseMod.jar
          desktop1.0.jar
```
You can run "./gradlew listJars" (without the ") to see which jars are found in your 
libs folder. If there are no jars listed, you probably haven't configured it correctly

If you want to import your mod into an IDE, run:
 - For Eclipse: ./gradlew eclipse OR gradlew eclipse
 - For IntelliJ Idea: ./gradlew idea OR gradlew idea

And then import the project using your IDE's Wizard

