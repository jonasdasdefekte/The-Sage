# The Sage
*A mechanic who studied about the cult of a blue bird.  
She learned flying a carpet and brewing potions in order to stop its rise.*

### [ :inbox_tray: Download](https://github.com/jonasdasdefekte/The-Sage/releases "Download")

Jump to: **[Content](#content) - [Gameplay](#gameplay) - [Our Plans](#our-plans-for-the-sage) - [How To Mod The Spire](#how-to-mod-the-spire) - [Building](#building-only-important-for-modders)**

## Content

This mod for the brilliant game Slay the Spire is currently under development.  
At the moment, the Sage Mod introduces:

| For all Runs      | For Sage Runs      |
|:-----------------:|:------------------:|
| 3 Cards           | 68 Cards           |
| 5 Relics          | 6 Relics           |
| 1 Potion          | 1 Potion           |
| 1 Event           |                    |

Also the mod introduces *Upgraded Potions* to the base game!

If you are interested in details, feel free to check out our [design document](https://github.com/jonasdasdefekte/The-Sage/blob/master/The%20Sage "The Sage - Mod for Slay The Spire").

*Supported Languages*: English, German.

*Disclaimer: We aren't artists! While we appreciate that there are some very beautiful mods out there, we went with rather goofy selfmade art. So don't set your expectations too high there. (If it randomly happens that you like making STS Art as well as the playstyle of this mod and you want to get involved into a project, hey, you could get involved into our project!)*


## Gameplay

#### Flight

The Sage's starting Relic is the **Flying Carpet**.

![The Sage: Flying Carpet](https://github.com/jonasdasdefekte/The-Sage/blob/master/The-Sage/src/main/resources/sage/relics/Flying_Carpet.png "Flying Carpet")

*At the start of your second turn, gain 1 Flight.*

Flight is the same buff that we all know from our beloved Byrd friends. This changes the gameplay quite drastically, since the enemies' attacks are altered: As long as you have Flight, you will take 50% less attack damage. But beware, for each time you take unblocked attack damage, you will lose one charge! If you lose the last charge, you will take double damage of all attacks, also in the same round. Surviving without Flight can be -- rough, especially because you don't start with the healthiest amount of max HP.

#### Brewing

Every basegame character has its special take on a card type: The Ironclad likes Attacks, The Silent goes well with Skills and The Defect is specialized in playing Powers. Rather than introducing a new card type, this character focusses on another game element: Potions.  

![The Sage: Brewing Potions](https://raw.githubusercontent.com/jonasdasdefekte/The-Sage/master/The-Sage/src/main/resources/sage/cards/skill/Quackster.png "Quackster")

*Quackster (Uncommon Skill, 0 Energy): Brew a random potion in 4 turns.*

Usually you have to wait a couple of turns before your potions are ready to use. But there is also a Brewing power that will help brewing your potions faster. For example, when playing the above card, Quackster with 2 Brewing power, you will recieve your random potion not in 4 but in 2 turns. The potion mechanic introduces many new tactical decisions because, just as you might know, you can only hold a couple of potions at the same time.

#### X cost cards

We decided to include 9 (sic, in words: nine!) cards with X cost in The Sage's card set. They share the theme of being guidebooks, like this one excempli gratia:

![The Sage: Guidebooks represent X cost cards](https://raw.githubusercontent.com/jonasdasdefekte/The-Sage/master/The-Sage/src/main/resources/sage/cards/skill/How_To_Befriend_A_Turtle.png "How To Befriend A Turtle")

*How To Befriend A Turtle (Common Skill, X Energy): Gain X (+3) Block. Gain X Energy.*

This may sound underwhelming, but we balanced the X cost cards so that you can build an entire archetype out of them. Some people say this makes Chemical X ridiculous, but we find it actually doesn't break the game (probably). You might even find a power that gives you Energy for reading those books... now *that* could be called ridiculous!

#### Artifact

Artifacts are the Power of Ancient technology. As a tinkerer, the Sage has grown familiar to that knowledge since her childhood. 

![Artifacts](https://raw.githubusercontent.com/jonasdasdefekte/The-Sage/master/The-Sage/src/main/resources/sage/cards/skill/Gearwheel_Master.png "Gearwheel Master")

*Gearwheel Master (Uncommon Skill, 0 Energy): EVERYBODY gains 1 Artifact. Exhaust. Can be upgraded any number of times.*

When playing this character, you will have to make strategic decisions about where you would like Artifacts to be and where not. Several cards allow you not only to gain Artifacts for yourself, but also to "help" the enemies with some of this Ancient Gearwheels. Artifacts on enemies will let you handle them easier. Do you want to stack Artifacts on your enemies or do you prefer giving them debuffs? Also there are cards that reward you to be Frail. Will you use the Artifact on yourself to protect you from Frailty or do you prefer being Frail for a while in order to get a big payoff? The tinkering is up to you...

![Character Button](https://raw.githubusercontent.com/jonasdasdefekte/The-Sage/master/The-Sage/src/main/resources/sage/character/button.png)

Each of these main Archetypes can be a winning condition for your deck playing with The Sage!  
There are more effects and mini-Archetypes. Although we better leave them for you to discover. :smile:  

## Our plans for The Sage
- **Balance** (we have been playtesting since end of 09/2018, most things should be reasonable by now but we are still tweaking here and there, feedback is welcome);
- growing the card set to **full size** of 75 (feel free to share your ideas if you like to);
- card **art** will be added in slow pace (Jonas made all of it so far, we would be glad for anyone who contributes);
- functions from [STSLib](https://github.com/kiooeht/StSLib).
- maybe we'll come up with some more suprises...?

## How To Mod The Spire
If you have found this, chances are that you know how this works. Still we want to welcome everybody, so if you aren't familiar to Modding, you can take a look at this [Tutorial Video](https://www.youtube.com/watch?v=AAhs55x7oFo) by Rhapsody (the basics still work the same way).  
This mod requires to use [ModTheSpire](https://github.com/kiooeht/ModTheSpire/releases) as well as [Base Mod](https://github.com/daviscook477/BaseMod/releases). Make sure you have the latest versions and - while you're at it - also check whether you use our [latest release](https://github.com/jonasdasdefekte/The-Sage/releases).  
We want to thank the active modding community of this game to make such an exciting project possible!

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
   \_The-Sage (git project folder)
             \_The-Sage (gradle project folder)
                       \_src...
   \_lib
        \_ModTheSpire.jar
          BaseMod.jar
          desktop1.0.jar
          StSLib.jar
```
You can run "./gradlew listJars" (without the ") to see which jars are found in your 
libs folder. If there are no jars listed, you probably haven't configured it correctly

If you want to import your mod into an IDE, run:
 - For Eclipse: ./gradlew eclipse OR gradlew eclipse
 - For IntelliJ Idea: ./gradlew idea OR gradlew idea

And then import the project using your IDE's Wizard

