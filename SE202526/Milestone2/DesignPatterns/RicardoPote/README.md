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






    

