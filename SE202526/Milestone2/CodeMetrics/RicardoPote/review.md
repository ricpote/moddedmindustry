## Review

## Change Log 
- 7/11/2025 Joao Fernandes

# Cyclomatic Complexity - CC
  This explanation defines Cyclomatic Complexity, which measures the number of independent paths through a method or class.
  It highlights that high CC increases testing difficulty, reduces maintainability, and makes the code more error-prone, 
  as there are many execution paths to consider.
  
# Coupling Between Objects - CBO
  This explanation defines CBO, showing how much a class depends on other classes.
  High CBO indicates tight coupling, reducing modularity and making the code fragile, which can lead to Feature Envy or
  Shotgun Surgery code smells.
  
# Response For a Class - RFC
  This explanation defines RFC, the number of methods that can be invoked in response to a message to a class.
  High RFC increases complexity, reduces understandability and maintainability, and can signal a God Class or a class
  with too many responsibilities.
  
# Weighted Methods per Class - WMC
  This explanation defines WMC, the sum of the complexities of all methods in a class.
  High WMC indicates a large, complex class that is harder to test and maintain, and may point to classes with multiple
  responsibilities or overly complex methods.