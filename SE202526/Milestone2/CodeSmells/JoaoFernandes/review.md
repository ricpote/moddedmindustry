## Review

## Change log
-7/11/2025 Joao Rodrigues
-7/11/2025 Ricardo Pote

# Long Method

-

# Feature Envy

This section correctly identifies the Feature Envy code smell. This is proven in the code snippet by the method's incessant references  from other classes, such as spawner and Unit groups.besides that, the proposed solution perfectly refactors the smell by employing the most common technique for resolution: the Move Method refactoring, which delegates the envious logic to its rightful class, thus improving cohesion.

# Data Clumps(Reviewer Joao Rodrigues)

-This section clearly identifies a Data Clumps issue in the MapEditor class, where related variables are frequently used
together across multiple methods. The explanation correctly points out that this increases maintenance difficulty and 
error risk. The suggested solution — grouping these variables into a dedicated object — is practical and aligns with good 
object-oriented design principles. It would improve code readability, reduce parameter passing, and make future changes 
easier.
