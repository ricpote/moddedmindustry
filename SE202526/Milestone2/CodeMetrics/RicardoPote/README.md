RFC metric 
The first metric i found particularly interesting was a value of RFC or Response For Class metric from Chidamber-kemere metrics set this evaluates the number of total methods that can be called when a message is received by an object of the evaluated class
,this value is equivalent to the sum of the methods of the class, and all distinct methods that  are invoked directly within the class methods.
Additionally, inherited methods are counted, but overridden methods are not, because only one method of a particular signature will always be available to an object of a given class.
I found a verry high value for a RFC metric on ClientLauncher class the value i read is:





<img width="277" height="74" alt="image" src="https://github.com/user-attachments/assets/308c4640-fa44-4a02-87d3-c13996a905a4" />
,and Regular Range for this metric is:





<img width="259" height="32" alt="image" src="https://github.com/user-attachments/assets/fa516d8b-6423-44a4-b279-f5e5d01e04a2" />



such a big value for this metric might be an indicator that this class has to much responsability and that is negative for 
-Maintainability
-Efficiency
-Portability
-Makes reusability more atractive but less understandable 
these big value for RFC led me into searching to see if this class is too responsable and that led me too seing another metric for this class that was out of the ordanary
this one being CBO or Coupling bettwen objects from Chidamber-kemere metrics set.
CBO metric
this metric represents the number of other classes this class is coupled to. 
This metric was at:





<img width="289" height="75" alt="image" src="https://github.com/user-attachments/assets/38c20f7e-8816-475b-87f2-85d6355ff9e9" />




whilst its Regular Range is:




<img width="272" height="32" alt="image" src="https://github.com/user-attachments/assets/b7df583d-d10f-4d2d-a487-e5279af52b36" />




such a big value for this metric might be indicate that this class has too big of a depency whit other classes this is it is intertwined whit too many classes for its own good
this amplifies preocupations that the last metric created since this class seems to have a high level of coupling and control responsability along whit having 
too big of a response set this indicates that we might be in the presence of a god class code smell or of a feature envy code smell.





Before making rash decisions we should first take a look at the class and see what it actually does and then make conclusions

The ClientLauncher class takes on the role of initializing most major components of the game and managing the lifecycle of those systems. This central responsibility explains its unusually high RFC = 96 and CBO = 27 values. However, this also leads to the presence of clear code smells, most notably the God Class smell, as it handles many unrelated concerns. 
Despite this, the design choice is somewhat understandable given the class’s critical role in initializing the game client. Still, this structure increases the complexity helping to create a system that is harder to maintain, test, and extend over time.


The second metric value i found interesting is also in a specific class in this case the UnitType class and metric I wanted to take a look at in this class is the CCC metric or Cognetive Complexity from G.Ann Metric Set this metric evaluates the how difficult it is for human to understand this class this is roughly calculated by these rules:
-Adds +1 for each control structure (if, loop, catch, etc.).
-Adds +1 per nesting level so when using nests of ifs or fors the value quicly rises
-Penalizes recursion, complex boolean expressions, switch chains, etc.
-Does not reward flattened or early-return code that’s easier to read




Now talking about UnitTYpeclass in specific this class has a value in this metric of 296 whilst the regular value range for this metric is from zero to 32 



<img width="624" height="68" alt="image" src="https://github.com/user-attachments/assets/ee1cb29e-aea4-425b-ac34-810644d62cef" />



this means that this claas is too complex and most likely has the presence of code smells like long methods ,god classes , switch , if chains  or feature envy 
,but whit metrics we can easily check for the presence of these code smeels lets just look at these method metrics for this class to discover what exatly is causing these values 
First method Whit out of the ordanary metrics is the this method  createIcon on line 1160


<img width="1180" height="442" alt="image" src="https://github.com/user-attachments/assets/55a300ac-8ae6-48fa-8867-a8f2396056f7" />



whit these metrics 
<img width="477" height="512" alt="image" src="https://github.com/user-attachments/assets/40ce1756-dc19-4d45-8804-daf0e8fe16e3" />








These metrics indicate that this is a a method that depends heavily on other classes methods ,structures and fields 
Cognitive Complexity (24) means that this method as lots of (ifs,for,etc) 
in this case its high because of multiple loops and even nested for loops ;
several if conditions 
and lots of instance of references 
alond whit this class metric we could also check the maximum nexting depth of a method that in this case is at 5 
both of these metrics suggest that this method might have long method smeell 
Then if we look a bit at Copupling intensity and foreign data providers we can then assume that this class nteracts with 21 external elements (methods, classes, or globals) and uses 13 from other classes.
so it is heavily dependent on other classes which leads us to asumme we might be in the presence of a feuture envy code smeell .
From all this methods i am led to conclude that this method is a  monolithic method whit  high-dependency on others makin it more difficult to reason about and maintain


That method clearly adds to the complexity of this class over all helping to create the abnormal value found in CCC values but lets see what is the UnitTypec class actually used for 
well this class is responsibale for defining all unit types their graphics, behaviour, movement and overall interation whit the world.
this means our class is a factory a behavioural hub and a renderer all at same time and because of this it is an example of a god class.







