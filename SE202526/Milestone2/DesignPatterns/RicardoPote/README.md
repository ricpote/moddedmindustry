1º Observer Design Pattern

I identified the Observer Design Pattern in core/src/net/Net.
This pattern is used here to allow the observers  in this case: ( NetClient, NetServer, and SNet)  to handle the incoming packets.
This way, the Net class does not need to know how to handle every different type of packet, instead, it gives that responsibility to the appropriate observer classes.
As a result, the Net class becomes less connected to the Mindustry game logic, making the system easier to extend, maintain, and reuse.

<img width="879" height="516" alt="image" src="https://github.com/user-attachments/assets/a1751da6-2b42-44af-b3a7-a63bd41065c9" />



    These are the Observer Collections in Net:(lines 35 and 36 respectively)
The observer collections store the registered listeners for both client and server packet handlers.
    
  <img width="1018" height="55" alt="image" src="https://github.com/user-attachments/assets/059d4116-25ae-44d3-b442-28244943dd00" />

    
    These are the Obeserver Registry Methods in Net:(lines 254 and 261 respectively)
These methods allow observers to register their interest in specific packet types.
    
  <img width="929" height="378" alt="image" src="https://github.com/user-attachments/assets/761e6636-05c2-4d1e-87f4-bc255c8609e0" />

    These are the Observer Notify Methods in Net:(lines 268 and 308 respectively)
 These methods notify the registered observers when a relevant event occurs in this case, when a packet is received from either the client or the server
  <img width="1044" height="559" alt="image" src="https://github.com/user-attachments/assets/cdffcf3e-19b3-4fe4-8eb6-c6d74f5d8d3f" />


    
   <img width="998" height="659" alt="image" src="https://github.com/user-attachments/assets/4c69c0ad-a1ca-4135-bad4-a13a8a0a8e11" />

    The concrete observer classes are SNet (with the update() method at line 52), along with NetClient and NetServer, which register themselves with the Net     class through the handleClient() and handleServer() methods in their constructors.


    
<img width="828" height="143" alt="image" src="https://github.com/user-attachments/assets/fbf20c67-f769-4e41-b217-5bedddb0c603" />




<img width="799" height="144" alt="image" src="https://github.com/user-attachments/assets/52edd0fb-f2ae-4d9f-8d40-886fd47d523a" />


    
Net does not directly reference any of these classes only cons and cons2 interfaces in this way allow for less dependence .


2ºFactory Object Design Pattern:
The Factory Object Design Pattern is found in the core/src/content/Items class.
This class acts as a factory responsible for creating and initializing preconfigured Item objects.
It centralizes object creation inside the load() method, ensuring consistent setup and easier maintenance of all Item instances used throughout the game.

<img width="232" height="468" alt="imagem_2025-11-05_174909357" src="https://github.com/user-attachments/assets/9312f82d-77a6-4a95-bf24-a9b59422060e" />




Factory Method:load() method this handles all object creation and configuration.
  <img width="923" height="552" alt="image" src="https://github.com/user-attachments/assets/dddbdd22-0eb2-47f4-8362-b104d3273e0b" />







Product(interface/class):Item
All created objects belong to this class
<img width="668" height="173" alt="image" src="https://github.com/user-attachments/assets/28e47ed9-cf47-4b43-bdf2-44561588b42d" />








 
Concrete prodcuts:ex:new Item("copper") These are the specific Item class instances configured
<img width="674" height="138" alt="image" src="https://github.com/user-attachments/assets/e75535da-957a-496a-a8cb-62c98a1af1be" />



Using the Object Factory pattern here allows centralized control over item creation, ensuring consistency across all game items.
It simplifies extendability as new items can be added or modified within a single method and prevents code duplication throughout the codebase , 






3ºFacade Design Pattern:
I identified this design pattern in core/src/mindustry/core/ContentLoader class.
This class loads all game content(items,blocks,unitc,etc...) offering a simple interface for loading game content whitout revealing the complexity of these classes 
[![](https://img.plantuml.biz/plantuml/svg/XLH1Ri8m4Bpx5JvIKNX0X10AvO045HAfUdQT1I8SExMTgeWYzJLzM5zI9x53abhoAVRExCQUcMIXD96wp1b2AECQv85Gm4jXbbpZwwrJt9AY86yC70YVvf6gBXbucRjzSYcWgY7AY58uPeAUxNgj8RVBJVPUPgdTlF2iRhFRlIQwLAl30QYsfHqZ7DmcBXc3RiCh47q2wTfDcv0x2Qhjs8CiIYOIeAT4WcjRIJXdiYqYTsHxaOqWAKXyHHYFgJIlW9WefyBe0KVuGvWhjEXMf3umCvVh5PYP-w-m54n8rIiVGSUNzTD8cuj6NNKVSPP6s1XKiMV9q91vs_nCSl2rSrEFZCmouyV1Z87PzImWz3IYWgi84qcdXvBJgS7Lp97kfnxZt-yVZ6wLcCW5PFOrcGm7es6gQb8J5YzKPyUBs2XviJPPNhGND2_3viv_s7iClRXBfV_LtP0E61LXc5WFPH3VVcedatcZfh4c9vmSGU6vXrF96g0q-WP8JkW0hQjuWDf87o3TFGWGhHcXutLS2L9x1WNOVQzwv8Nvxfh_CFe3)](https://editor.plantuml.com/uml/XLH1Ri8m4Bpx5JvIKNX0X10AvO045HAfUdQT1I8SExMTgeWYzJLzM5zI9x53abhoAVRExCQUcMIXD96wp1b2AECQv85Gm4jXbbpZwwrJt9AY86yC70YVvf6gBXbucRjzSYcWgY7AY58uPeAUxNgj8RVBJVPUPgdTlF2iRhFRlIQwLAl30QYsfHqZ7DmcBXc3RiCh47q2wTfDcv0x2Qhjs8CiIYOIeAT4WcjRIJXdiYqYTsHxaOqWAKXyHHYFgJIlW9WefyBe0KVuGvWhjEXMf3umCvVh5PYP-w-m54n8rIiVGSUNzTD8cuj6NNKVSPP6s1XKiMV9q91vs_nCSl2rSrEFZCmouyV1Z87PzImWz3IYWgi84qcdXvBJgS7Lp97kfnxZt-yVZ6wLcCW5PFOrcGm7es6gQb8J5YzKPyUBs2XviJPPNhGND2_3viv_s7iClRXBfV_LtP0E61LXc5WFPH3VVcedatcZfh4c9vmSGU6vXrF96g0q-WP8JkW0hQjuWDf87o3TFGWGhHcXutLS2L9x1WNOVQzwv8Nvxfh_CFe3)










As shown in this Diagram ContentLoader class initializes all these classes and provides and easy way for others classes of the mindustry application to acess their members whitout having to initialize them and search for specefic instances of them as contentloader classes simplifies this  the following methods:

The main Facade method here is createBaseContent(), which initializes all base content types by calling their respective load() methods. This makes all content data available within the system, avoiding the need for initialization in multiple places.
<img width="499" height="541" alt="image" src="https://github.com/user-attachments/assets/87cb9158-ca30-4909-a465-a267368e16c7" />




init()(line 90) and load()(line 98) method 
these methods are essencial for proper content type initialization this methods initialize all content types into the game making them actualy usable 


<img width="1238" height="424" alt="image" src="https://github.com/user-attachments/assets/0d20e42f-6dc2-4af3-93fe-93a85c9523cd" />



besides these this facade as more methods used to implement a look up map that allows the content of any content type to be acessable by using a simple look up these are handleContent() that adds new content to a contentMap and handleMappableContent() that does the same but registers by name making searches simpler.
    



<img width="757" height="184" alt="image" src="https://github.com/user-attachments/assets/289b9f8d-92b6-445d-8f68-0fdebdcb5613" />


<img width="1044" height="107" alt="image" src="https://github.com/user-attachments/assets/2e6baf53-e4ff-470e-a902-f27c6bb624ae" />



Said searches are handle by 
<img width="929" height="102" alt="image" src="https://github.com/user-attachments/assets/f3bc8072-e45d-4400-abe0-ad6da7fe29ba" />

This way centralizing both the initialization of these complex interfaces that is why this class acts as a facade, however it is not a perfect facade implementation as it lets the underlying complexity shine trough even allowing direct access because of that it still requires client to know something about the subsystem under it in this way falling at total abstration of it.

