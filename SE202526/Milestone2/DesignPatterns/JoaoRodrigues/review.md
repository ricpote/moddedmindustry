## Review

## Change log
- 7/11/25 - Miguel Cordeiro (Singleton)
- 7/11/25 - André Narquel (Template Method)
- 7/11/2025 - Joao Fernandes (Adapter)

# Template Method
- The Ability class uses the Template Method pattern, providing a base workflow with update(), addStats(), and draw().
Subclasses like ArmorPlateAbility and ForceFieldAbility override these methods to define specific behavior, making the
system flexible, reusable, and easy to extend.

# Singleton
- The design pattern isn't well identified because the class NetClient doesn't enforce it's own uniqueness (missing a private constructor). The example is well chosen because vars.netClient is used as a global acess point.

# Adapter
- The design pattern is well identified, as it has the Shaders class providing a consistent interface for multiple 
  shaders, allowing the game to use them interchangeably while decoupling from their specific implementations and
  enabling incompatible interfaces to work together.
