1º Observer Design Pattern:

I Observed the Observer Design Pattern in core/src/net/Net 
this pattern is used here in order to let the observers in this case clients and servers handle the packets coming in 
making it so Net class doesnt need to know how to handle all different kinds of packet it just gives it to the classes that handle it.This way net class becomes less connected to mindustry logic making it easier to extend and reuse.




    These are the Observer Collection in Net:(lines 35 and 36 respectively)

    
    private final ObjectMap<Class<?>, Cons> clientListeners = new ObjectMap<>();
    
    private final ObjectMap<Class<?>, Cons2<NetConnection, Object>> serverListeners = new ObjectMap<>();
    
    These are the Obeserver Registry Methods in Net:(lines 254 and 261 respectively)

    
    public <T> void handleClient(Class<T> type, Cons<T> listener){
        clientListeners.put(type, listener);
    }
    
    public <T> void handleServer(Class<T> type, Cons2<NetConnection, T> listener){
        serverListeners.put(type, (Cons2<NetConnection, Object>)listener);
    }
    These are the Observer Notify Methods in Net:(lines 268 and 308 respectively)
    
     public void handleClientReceived(Packet object){
        object.handled();

        if(object instanceof StreamBegin b){
            streams.put(b.id, currentStream = new StreamBuilder(b));

        }else if(object instanceof StreamChunk c){
            StreamBuilder builder = streams.get(c.id);
            if(builder == null){
                throw new RuntimeException("Received stream chunk without a StreamBegin beforehand!");
            }
            builder.add(c.data);

            ui.loadfrag.setProgress(builder.progress());
            ui.loadfrag.snapProgress();
            netClient.resetTimeout();

            if(builder.isDone()){
                streams.remove(builder.id);
                handleClientReceived(builder.build());
                currentStream = null;
            }
        }else{
            int p = object.getPriority();

            if(clientLoaded || p == Packet.priorityHigh){
                if(clientListeners.get(object.getClass()) != null){
                    clientListeners.get(object.getClass()).get(object);
                }else{
                    object.handleClient();
                }
            }else if(p != Packet.priorityLow){
                packetQueue.add(object);
            }
        }
    }

    
    public void handleServerReceived(NetConnection connection, Packet object){

        try{
            if(connection.hasConnected || object.getPriority() == Packet.priorityHigh){
                object.handled();

                //handle object normally
                if(serverListeners.get(object.getClass()) != null){
                    serverListeners.get(object.getClass()).get(connection, object);
                }else{
                    object.handleServer(connection);
                }
            }
        }catch(ValidateException e){
            //ignore invalid actions
            debug("Validation failed for '@': @", e.player, e.getMessage());
        }catch(RuntimeException e){
            //ignore indirect ValidateException-s
            if(e.getCause() instanceof ValidateException v){
                debug("Validation failed for '@': @", v.player, v.getMessage());
            }else{
                //rethrow if not ValidateException
                throw e;
            }
        }
    }
    The observer interfaces are the interfaces of the collections in this case cons and cons2 and the concrete observers the classe is (Snet.java with update method on line 52)



2ºFactory Object Design Pattern:
Said design pattern is found in core/src/content/Items class.
This class defines a factory of Item objects that are alrady configured uses load() method to create and initialize all Item instances 

<img width="232" height="468" alt="imagem_2025-11-05_174909357" src="https://github.com/user-attachments/assets/9312f82d-77a6-4a95-bf24-a9b59422060e" />

Product(interface/class):Item
All created objects belong to this class
Concrete prodcuts:ex:new Item("copper") These are the specific Item class instances configured
Creator:Items class
Factory Method:load() method this handles object creation and configuration.







3ºFacade Design Pattern:
I identified this design pattern in core/src/mindustry/core/ContentLoader class.
This class loads all game content(items,blocks,unitc,etc...) offering a simple interface for loading game content whitout revealing the complexity of these classes 
[![](https://img.plantuml.biz/plantuml/svg/XLH1Ri8m4Bpx5JvIKNX0X10AvO045HAfUdQT1I8SExMTgeWYzJLzM5zI9x53abhoAVRExCQUcMIXD96wp1b2AECQv85Gm4jXbbpZwwrJt9AY86yC70YVvf6gBXbucRjzSYcWgY7AY58uPeAUxNgj8RVBJVPUPgdTlF2iRhFRlIQwLAl30QYsfHqZ7DmcBXc3RiCh47q2wTfDcv0x2Qhjs8CiIYOIeAT4WcjRIJXdiYqYTsHxaOqWAKXyHHYFgJIlW9WefyBe0KVuGvWhjEXMf3umCvVh5PYP-w-m54n8rIiVGSUNzTD8cuj6NNKVSPP6s1XKiMV9q91vs_nCSl2rSrEFZCmouyV1Z87PzImWz3IYWgi84qcdXvBJgS7Lp97kfnxZt-yVZ6wLcCW5PFOrcGm7es6gQb8J5YzKPyUBs2XviJPPNhGND2_3viv_s7iClRXBfV_LtP0E61LXc5WFPH3VVcedatcZfh4c9vmSGU6vXrF96g0q-WP8JkW0hQjuWDf87o3TFGWGhHcXutLS2L9x1WNOVQzwv8Nvxfh_CFe3)](https://editor.plantuml.com/uml/XLH1Ri8m4Bpx5JvIKNX0X10AvO045HAfUdQT1I8SExMTgeWYzJLzM5zI9x53abhoAVRExCQUcMIXD96wp1b2AECQv85Gm4jXbbpZwwrJt9AY86yC70YVvf6gBXbucRjzSYcWgY7AY58uPeAUxNgj8RVBJVPUPgdTlF2iRhFRlIQwLAl30QYsfHqZ7DmcBXc3RiCh47q2wTfDcv0x2Qhjs8CiIYOIeAT4WcjRIJXdiYqYTsHxaOqWAKXyHHYFgJIlW9WefyBe0KVuGvWhjEXMf3umCvVh5PYP-w-m54n8rIiVGSUNzTD8cuj6NNKVSPP6s1XKiMV9q91vs_nCSl2rSrEFZCmouyV1Z87PzImWz3IYWgi84qcdXvBJgS7Lp97kfnxZt-yVZ6wLcCW5PFOrcGm7es6gQb8J5YzKPyUBs2XviJPPNhGND2_3viv_s7iClRXBfV_LtP0E61LXc5WFPH3VVcedatcZfh4c9vmSGU6vXrF96g0q-WP8JkW0hQjuWDf87o3TFGWGhHcXutLS2L9x1WNOVQzwv8Nvxfh_CFe3)

As seen in this Diagram ContentLoader class initializes all these classes and provides and easy way for others classes of the mindustry application to acess 
their members whitout having to initialize them or search for specefic instances of them as contentloader classes simplifies this whit these methods:






    





