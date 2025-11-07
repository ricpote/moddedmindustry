## Review

## Change log
- 7/11/25 - Miguel Cordeiro (Data Class Reviewed)
- 7/11/25 - André Narquel (Data Clumps Reviewed)

# Data Class
- This code smell is well identified and the solution to the problem isn't adequate. The example was well chosen because the class only has variables. The solution on the other hand is very confusing because we have a class that only has variables and the solution seems to be implementing get's and set's. The correct solution would be to add an interesting method and convert the class to a record class.

# Speculative Generality
-

# Data Clumps
- The code smell analysis is accurate. The Block class currently repeats the parameter set(Team team, float x, float y,
- float range) across many methods, which indicates a missing abstraction and reduces cohesion. The suggested solution 
- to encapsulate these variables into a dedicated object with controlled access methods would improve readability, 
- maintainability, and encapsulation, as correctly mentioned in the analysis.
