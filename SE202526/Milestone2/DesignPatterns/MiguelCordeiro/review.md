# Review

# Change Log
-7/11/2025 Joao Rodrigues(Factory Method)
-07/11/2025 19:34 Ricardo Pote(Facade)

# Factory Method
-This section correctly identifies a Factory Method pattern in the Waves class. The get() and generate() methods 
encapsulate the creation of multiple SpawnGroup objects, hiding complex wave setup details from other parts of the code.
This improves encapsulation and makes it easier to modify wave creation without changing client code.

# Facade
-This design pattern is well identified as it uses a simple interface to hide a complex subsystem and the provided code snipets support this conclusion by showing the renderer holding the references to all the subsystems hiding their complexity by doing this in draw() method so that ClientLauncher can simply use an instance of renderer class and not deal whit the underlying classes.

PS:(class name is ClientLauncher not ClienteLaucher)
# Strategy
