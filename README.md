# Onnet

A game built on an Java OpenGL based game engine

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


