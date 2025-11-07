## Design Patterns

# Change log
- 6/11/2025 Tomás Silva

# Prototype

Path: core/src/mindustry/world/Block.java
Package: mindustry.world
Instantiation: Block#newBuilding() and subclass constructors (e.g. Conveyor, Turret)
Cloning Behavior: Building objects created from Block prototypes

The Block class in Mindustry is a strong example of the Prototype design pattern.
Each subclass of Block (such as Conveyor, Wall, Turret, etc.) defines a prototype that stores all shared attributes 
— including visual properties, build requirements, and gameplay behavior. When a new block is placed in the world, 
the game does not construct it from scratch. Instead, it creates a new instance of the Building class based on the 
existing block prototype, usually through the newBuilding() method.
//Code snippet


# Strategy


//Code snippet


# Composite

//Code snippet
