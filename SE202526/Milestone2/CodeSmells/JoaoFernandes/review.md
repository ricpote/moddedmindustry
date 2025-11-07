## Review

## Change log
- 07/11/2025 Joao Rodrigues (Reviewd Data Clumps)
- 07/11/2025 Ricardo Pote (Reviewd Feature Envy)
- 07/11/2025 Tomás Silva (Reviewd Long Method)

# Long Method

- The work correctly identifies the "Large Method" code smell in the showUpdateDialog() function, which is overloaded
with multiple responsibilities (user interface logic, download management, and restart execution).
- The proposed solution of dividing each operation into smaller, dedicated methods is the ideal way to increase code 
readability and maintainability, aligning it with the Tell, Don't Ask principle and improving cohesion.

# Feature Envy

- This section correctly identifies the Feature Envy code smell. This is proven in the code snippet by the method's incessant references  from other classes, such as spawner and Unit groups.besides that, the proposed solution perfectly refactors the smell by employing the most common technique for resolution: the Move Method refactoring, which delegates the envious logic to its rightful class, thus improving cohesion.

# Data Clumps

- This section clearly identifies a Data Clumps issue in the MapEditor class, where related variables are frequently used
together across multiple methods. The explanation correctly points out that this increases maintenance difficulty and 
error risk. The suggested solution — grouping these variables into a dedicated object — is practical and aligns with good 
object-oriented design principles. It would improve code readability, reduce parameter passing, and make future changes 
easier.
