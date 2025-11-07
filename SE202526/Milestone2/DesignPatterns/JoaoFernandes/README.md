## Design Patterns

# Change log
- 7/11/2025 Joao Fernandes

# Prototype

  Path: core/src/mindustry/editor/MapEditor.Java
        core/src/mindustry/editor/EditorTile.Java
        core/src/mindutry/world/Tile.Java

  In this case the class EditorTile extends Tile, serving as the prototype that customizes the inherited behavior for the map
  editing context like, tile placement, rotation, and team assignment. Finally, the MapEditor class functions as the client, 
  since it is responsible for creating and managing EditorTile instances instead of normal Tile objects when loading or
  editing maps. This design allows the editor to reuse the Tile structure while providing specialized editing functionality,
  demonstrating a clear application of the Prototype design pattern.

  //Code snippets

      public class EditorTile extends Tile{

      public EditorTile(int x, int y, int floor, int overlay, int wall){
        super(x, y, floor, overlay, wall);
      }
      ...
      }


      public class MapEditor{
        ...
        public void updateRenderer(){
          ...
          tiles.seti(i, new EditorTile(tile.x, tile.y, tile.floorID(), tile.overlayID(), build == null ? tile.blockID() : 0));
          ...
        }
        ...
      }

# Observer

  Path: core/src/mindustry/ai/WaveSpawner
        arc/Events.java 

  In this class, we can see the Observer Design Pattern. It registers event listeners through the global Events system, 
  subscribing to notifications such as WorldLoadEvent and TileOverlayChangeEvent. When these events are fired elsewhere
  in the program, the corresponding callback methods inside WaveSpawner, like reset(), are automatically executed. This
  allows the class to react to changes in the game. By using the Events system as a centralized publisher, the architecture
  remains modular, extensible, and easy to maintain.

  //Code snippet

    public class WaveSpawner{
         public WaveSpawner(){
            Events.on(WorldLoadEvent.class, e -> reset());
    
            Events.on(TileOverlayChangeEvent.class, e -> {
                if(e.previous == Blocks.spawn) spawns.remove(e.tile);
                if(e.overlay == Blocks.spawn) spawns.add(e.tile);
            });
        }
        ...
    }

# 
  
    