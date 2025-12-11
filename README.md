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
- Please check that you have JDK 16 or newer (we have in our project Records & Enums that don't supported till JDK 15)
- clone the library into the default idea folders you have, not any nested folders inside of it, so relative paths work as expected
- go to run, press run configurations, and set VM options to the command you will find in Main class, that's crucial for the project to work, and set Main as main file

 
Components of this project explained:
## Game
- `GameLoop`: creates its own game loop, supporting 500 physics cycles per seconds, with the option of completely stopping the loop while keeping the screen rendered
- `InputHandler`: uses the `Input` bitwise flags enum to register and clear inputs given by the player, using the bitwise methods insidw `Input`

## Physics
- `RandomUtils`: contains utilities for randomization od doubles, vectors, points and so on
- `ActionHandler`: uses Hashmap to bind the  `Input`enum to the `Action` interface, checking for inputs pressed every second and executing the Action's `execute` method mapped inside the Hashmap
### Physics/Collisions
- `Collider`: uses a pattern I think called neighbor pattern to support intersecting methods for all shapes for all children (It's a permutation of possibilities), also supports the get MTV function, MTV standing for. `Minimum transition Vector`, what is does is incase of physics failing and penetration happening between two objects, the hitter object is moved by an MTV amount ensuring it doesn't penetrate the other object
- `CircleCollider`: a child class if fhe Collider interface, the circle in this class is the hitter, the collide method checks for types, if circle then do collision circle to circle and calculate MTV, if rectangle do the rectangle intersection algorithm and so on, there are classes like this too for multiple different shapes... neighbor pattern
- `AABB`: stands for Axis-Aligned boundary box, which as the names implies represents the boundaries of any shape used for collision alforithms
- `MTVResult`: a helper record (Immutable class) that is used in particular to support MTV finding algorithms
