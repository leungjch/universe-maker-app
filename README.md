# Universe Maker | Planetary simulation game
Universe Maker is an n-body simulation of a planetary system on an Android app, written in Java and Kotlin.
## Features
- Complete n-body simulation using an Euler integration physics solver.
- Objects coalesce upon colliding, and collisions are inelastic.
- A variety of celestial bodies including:
	- Asteroids
	- Planets
	- Stars
	- Black holes
	- White holes
	- AI Satellites
	- Player-controlled rocket ship

## Technical details
The entire app is run under `MainActivity.java`. In order to show a UI stacked on top of a canvas (a custom view `GameView`), I had to use a `RelativeLayout`. 

`GameView` sets up the simulation upon creation. It sets up and handles user input (e.g. touch, drag, pinch events). I also make use of `enum` objects to neatly keep track of the current user state (e.g. current type of celestial body selected, current placement type, etc). 

A `Universe` object is created in the `GameView` constructor. It contains an array of all celestial bodies in the system. The `universe` also contains and operates the inner "machinery" of how objects interact with each other through the `update()` function. The `update()` function loops through all celestial bodies and calculates the net force exerted by every other object on it and performs integration to obtain new positions in a nested loop. It also checks any collisions between objects in this loop. 

A `CelestialBody` is a superclass of all celestial objects that can be created. Every `CelestialBody` holds the physical properties of mass, radius, position, velocity, acceleration, and net force. Here's a list of all of the subclasses of `CelestialBody`:
- `Asteroid`: A small object with very low mass and radius. These objects move the fastest. During integration, we ignore the force exerted by these objects on other objects because it is negligible.
- `Planet`: An medium-sized object with considerable mass and radius. Place these objects in Orbit mode near a Star to simulate a solar system. 
- `Star`: A large-sized object with high mass and radius. You may find it useful to place these in the Fixed placement mode when playing around with planets and asteroids.
- `Black hole`: A dense object with extremely high mass and low radius. These exert the highest gravitational force of all celestial bodies.
- `White hole`: A hypothetical celestial body that yields negative mass and therefore repels any other objects with positive mass with its negative gravitational force.
- `Satellite`: An artificial object that circles the celestial body that is currently exerting the most force applied to it. Try it out in a binary star system and watch how it moves!
- `Player ship`: An object that can be controlled by the player. Touch and drag to apply thrust in the desired direction. Note that the ship can only be controlled when the `Player ship` option is selected.

There are also some other helper classes that I made.
- `Vector2D`: A utility class that implements 2D vectors and common operations on vectors, such as finding the magnitude of its x and y components, the angle between a vector and another vector, distance between two vectors, and dot product of two vectors.
- `MassRadiusTuple`: A utility class used to encapsulate the mass and radius definitions of different types of celestial bodies and their size types.
- `StarColors`: A class that maps the B-V color index of different star colour/temperatures to their RGB values. The B-V index can be a minimum of -0.4 (hottest / blue) and a maximum of 2.0 (coolest / red). A `StarColor` object is created upon initialization of a `ColorGenerator` object.
- `ColorGenerator`: A class that controls the appropriate colours assigned to every type of celestial body. Only one `ColorGenerator` is created upon initialization of the simulation.

