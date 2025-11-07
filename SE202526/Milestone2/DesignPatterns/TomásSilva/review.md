## Review

## Change log
-07/11/25 20:08-Ricardo Pote(Factory)
- 7/11/25 - Miguel Cordeiro (Strategy)
-7/11/2025 Joao Rodrigues(Composite)


# Factory Method
- This section adheres to a core principle of the Factory pattern by uniting the initialization and creation of instances for a specific object, the Liquid objects. This centralization is clearly visible in the provided code snippets.
The primary difference from a classical implementation is in the naming and scope of the creation method. In typical Factory patterns, the client often makes a request to a dedicated factory method (ex., createLiquid(type)) and receives a pre-configured instance.
In this case, the load() method  performs a one-time, bulk initialization of all preconfigured Liquids used in the game.

# Strategy
- The design patern is perfectly chosen because it encapsulates the algorithms. The class InputHandler serves as the abstract strategy, defining the "interface" for all the input types. The MobileInput and DesktopInput are the concrete strategies that provide the specific implementation. The Context holds a reference for the strategy to be implemented. The createPlayer method is responsible for the selection of the strategy to apply at the execution.

# Composite
-This section accurately identifies the Composite Design Pattern implemented in the mindustry.gen package. The 
explanation clearly maps each component of the pattern. Entityc as the Component interface defining the shared operations,
EntityGroup as the Composite managing collections of entities and delegating behavior, Bullet as the Leaf implementing 
core functionality without managing children.
