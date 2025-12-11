# Onnet

A student project game built on an Java OpenGL based game engine

For subject Computer graphics (CS304)

Members of the team:-
- Teracura (عبدالله عاطف عبدالله) - code: 2327220
- wizard1625 (عبدالرحمن اسامة) - code: 2327133
- helmy9999 (محمد حلمى) - code: 2328158
- yousefAtef00 (يوسف عاطف) - code: 2327471
- areda04 (احمد رضا احمد) - code: 2327492


How to use this project:
- Please check that you have JDK 23 or newer (we have in our project Records & Enums that don't supported till JDK 15)
- clone the library into the default idea folders you have, not any nested folders inside of it, so relative paths work as expected, or else you will receive path not found exceptions
- basically do not nest this project inside of another project

 
Components of this project explained:
## Game
- `GameLoop`: creates its own game loop, supporting 500 physics cycles per seconds, with the option of completely stopping the loop while keeping the screen rendered
- `InputHandler`: uses the `Input` bitwise flags enum to register and clear inputs given by the player, using the bitwise methods insidw `Input`
- `LeaderboardEntry`: entry which contains the user's name and score to be shown in leaderboard
- `LeaderboardHandler`: handles leaderboard saving and loading, saves into txt files and parses the txt files to a list of leaderboard entries
- `PageManager` (SCRAPPED): handles switching between frames, unused now due to pivoting to a single page (frame) application where the content changes but the frame remains
- `SoundHandler`: handles the life time of sounds, plays them and adjusts volume accordingly, has support for muting too
- `LoopState`: contains information about current frame time and last frame time to calculate deltaTime, used in `GameLoop`

## Physics
- `RandomUtils`: contains utilities for randomization od doubles, vectors, points and so on
- `ActionHandler`: uses Hashmap to bind the  `Input`enum to the `Action` interface, checking for inputs pressed every second and executing the Action's `execute` method mapped inside the Hashmap
### Physics/Collisions
- `Collider`: uses a pattern I think called neighbor pattern to support intersecting methods for all shapes for all children (It's a permutation of possibilities), also supports the get MTV function, MTV standing for. `Minimum transition Vector`, what is does is incase of physics failing and penetration happening between two objects, the hitter object is moved by an MTV amount ensuring it doesn't penetrate the other object
- `CircleCollider`: a child class if fhe Collider interface, the circle in this class is the hitter, the collide method checks for types, if circle then do collision circle to circle and calculate MTV, if rectangle do the rectangle intersection algorithm and so on, there are classes like this too for multiple different shapes... neighbor pattern
- `AABB`: stands for Axis-Aligned boundary box, which as the names implies represents the boundaries of any shape used for collision alforithms
- `MTVResult`: a helper record (Immutable class) that is used in particular to support MTV finding algorithms
- There exists other colliders but they are designed to be collided with not to collide (as the only controllable object in this game is the player's circle)

## Pages
- `SinglePageApplication`: the frame which contains the canvas shared across the lifetime of the whole project (Singleton), uses `JLabel` for content
### Pages/ContentPanels
as the name implies, they are panels that contain content, each extending `JPanel`, except `MenuButton` which is a button with special properties
- All names are self explanatory
- most notably, `Level` is a shared GUI across all levels

## Renderers
Renderers are classes that implement both `GlEventListener` and `GameLoop`, GlEventListener to handle initialization and drawing, GameLoop is for the custom game loop
### Renderers/Levels
- Contains all levels from 1 to 12, not including the background "level" which plays on the background of the main menu and level select

Note: while there are multipler renderers, there is only a single canvas which uses `GLJPanel`, instead of using multiple GLJPanels, we switch renderers on the same canvas, for performance reasons

## Shapes
Includes all shapes, each shape aggerates the appropriate collider and implements `Shape` all shapes are made via the Builder patterns
- All Shape names are self explanatory, yes, we treat Color as a shape

## Other content folders

### Sounds
includes all sound resources

### Libs
includes the JOGL jars needed for JOGL to function

### Libs/Natives
includes the native files for the operating systems: Windows, Linux and Mac (We haven't tested on a mac because we are broke), the program automatically detects your OS and uses the appropriate file

### Leaderboards
includes the txt files used for saving, each level with its own file thus its own leaderboard
Note: we could've used JSON, but that would require an external library named GSON and without Maven or Gradle it will be hard to do that, and making our own JSON reader/writer is way beyond the scope of the projecy



# The journey
this is the first project our university requested for us, and it was the first chance we were to work as a team, I am proud of the product we came out with, but there is one thing I haven't explained, how to use the app

## How to use the app
Upon starting the app, you will notice 6 things, a ball that bounces in the background, a menu button on the top right, credits, instructions, levels and leaderboard on the middle

### Leaderboard
inside you will notice a lot of things, a button for each level, a back button, and an ominous menu button still lingering on the top right, each level has its own leaderboard as described, when there are no scores, it shows that there are no leaderboard entries for the level with a gray-ish screen, that's intended behavior

### Instructions
tells you the controls, your target, what should you avoid touching or else you will die (ingame) and how score is calculated

### Credits
Credits every developer that worked on the project, and shows their roles

### Levels
opens a panel filled with levels, notice that for in all cases no matter where you will go the Menu button will keep ominously staying on the top right
upon entering a level, the level loads, there are three possibilities
- you get stuck
- you win
- you die
if you get stuck, that's intended behavior because some maps are designed to have traps, if you find yourself in one, just press R, it doesn't reset your timer and your score keeps going down, so don't worry, you are actively losing score

if you die, you reset, same as above the score doesn't reset

if you win, you get a winning screen, a few people clapping for you, and your score viewed (whether that's a good or bad thing), you can still press R to restart if you get a shameful score, but don't worry this shameful score is stored in the leaderboard forever (unless you modify the txt files which is cheating by the way)

### Menu
for the elephant in the room, the menu button, when you win a level, your only way to return and pick another level is to head for the menu and go to levels
you will find a few options
- Continue
- Return to main menu
- Exit
- Levels
- Leaderboard
all are self explanatory

in the end, we've all learned a lot from this project as a whole, and if you reach the end of this readme file totally not skimming through the whole file, It was of great fun to work on this project, I always prefer projects than quizzes.
