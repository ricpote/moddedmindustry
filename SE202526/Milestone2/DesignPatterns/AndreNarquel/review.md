## Review

## Change log
- 07/11/25 - Tomás Silva (Reviewed Prototype)
- 07/11/25 - Tomás Silva (Reviewed Memento)

# Prototype
- The analysis is correct and precise in identifying the Prototype Pattern in this code.
- The structure is a classic example of using the Prototype pattern for the efficient creation of complex objects 
(the game blocks). The original block (Battery or any other Block) acts as the Prototype, providing the basis for 
creating new instances.

# Memento
- The analysis is accurate and rigorous in identifying the Memento Pattern in the save/load architecture.
- The mapping of the classes is exact: the internal game state acts as the Originator, the SaveMeta class is the Memento
that stores the serialized state, and the Saves class is the Caretaker that manages the slots and coordinates the operations.
- Finally, the listed benefits (Encapsulation and Restoration) are the primary and correct advantages of the pattern.

# Observer
-
