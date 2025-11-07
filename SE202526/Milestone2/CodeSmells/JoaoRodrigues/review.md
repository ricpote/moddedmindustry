## Review

## Change log
- 7/11/25 - Miguel Cordeiro (Reviewed Duplicated Code)
- 7/11/25 - André Narquel (Reviewed Large Class)

# Large Class
The code smell analysis is accurate. Vars is a Large Class violating the Single Responsibility Principle, as it mixes
methods like init(), loadSettings(), and loadLogger() with its original purpose of storing variables.
As correctly mentioned, refactoring into separate managers would improve cohesion, modularity, and testability.

# Duplicated Code
- This code smell is well identified and the solution to the problem is adequate. The example was well chosen because the two methods are basically identical only with small changes. The solution is perfect for this type of code smell.

# Long Method
- 
