# CodeBase Metrics

The set that I chose is Lorenz-Kidd Metrics Set and the metrics are NOO,NOOM e NOA. The NOO(Number Of Operations) counts the number of methods implemented in a class, the NOOM(Number Of Overridden Methods) counts the number of methods that a child class rewrites from the superclass and the NOA(Number Of Attributes) counts the number of attributes of a class.

Here are the tables that contain the top 12 classes with the highest values metrics NOO,NOOM e NOA:

<img width="501" height="314" alt="image" src="https://github.com/user-attachments/assets/47acacde-6764-4246-8f4c-37abba46a760" />

<img width="502" height="313" alt="image" src="https://github.com/user-attachments/assets/5c1aba0e-8a19-4660-9325-c2dd08f8ff4c" />

<img width="505" height="318" alt="image" src="https://github.com/user-attachments/assets/920fb335-14e5-4e8a-82a2-fb3177414d5f" />

Seeing the tables we can see the classes with the most problematic metrics, for example the values of NOA in the class Blocks are way above the recommended. The NOA is one of the most important to see if a class is a god class(Code Smell) because the high number of attributes means that probably the class is dealing with a lot of responsabilities. In the class CoreBuild the values of NOOM are very high, which indicates that the child class doesn't want to use the implementation made by the super class, overwriting the methods. This code smell is named refused bequest. In the class UnitComp the values of NOO are very high (468), indicating that the class maybe has too many responsibilities (Code Smell - God Class).


