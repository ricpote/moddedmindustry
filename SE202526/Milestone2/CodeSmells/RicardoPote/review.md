## Review

## Change Log
- 7/11/2025 Joao Rodrigues(Large Class)
- 7/11/2025 Joao Fernandes(Reviewd Feature Envy and Long Parameter)

# Large Class
-This section correctly identifies a Large Class code smell in the InputHandler class. The evidence is strong — with a 
WMC of 746 and 2,286 lines of code, the class clearly handles too many unrelated responsibilities such as drag-and-drop 
actions, building placement, and camera movement. This excessive scope makes the class hard to understand, test, and 
maintain.

# Feature Envy 
- This code smell is well identified, and the solution to delegate the calue atribution not only solves it but also improves
  encapsulation and code maintainability.

# Long Parameter
- This code smell is well identified, just from the example we can see that it has too many parameters, and the suggested
  solution of grouping them into records makes the code more readable and maintainable. 
