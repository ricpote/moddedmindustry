## Code Smells

# Change Log
   -6/11/2025 Joao Fernandes
   
# Large Method

   Location: core/src/mindustry/net/BeControl.Java
   
   This class has a method called showUpdateDialog() that clearly is too long. This method performs a lot of different 
   operations, such as managing download processes, it handles some operations, and some more independent operations.
   All of this joint together makes the code hard to read and hard to understand.

   Solution: The solution is to divide each operation in different methods and combine them all when needed.

   //Code snippet

    public void showUpdateDialog(){
        if(!updateAvailable) return;

        if(!headless){
            checkUpdates = false;
            ui.showCustomConfirm(Core.bundle.format("be.update", "") + " " + updateBuild, "@be.update.confirm", "@ok", "@be.ignore", () -> {
                try{
                    boolean[] cancel = {false};
                    float[] progress = {0};
                    int[] length = {0};
                    Fi file = bebuildDirectory.child("client-be-" + updateBuild + ".jar");
                    Fi fileDest = OS.hasProp("becopy") ?
                        Fi.get(OS.prop("becopy")) :
                        Fi.get(BeControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

                    BaseDialog dialog = new BaseDialog("@be.updating");
                    download(updateUrl, file, i -> length[0] = i, v -> progress[0] = v, () -> cancel[0], () -> {
                        try{
                            Runtime.getRuntime().exec(OS.isMac ?
                                new String[]{javaPath, "-XstartOnFirstThread", "-DlastBuild=" + Version.build, "-Dberestart", "-Dbecopy=" + fileDest.absolutePath(), "-jar", file.absolutePath()} :
                                new String[]{javaPath, "-DlastBuild=" + Version.build, "-Dberestart", "-Dbecopy=" + fileDest.absolutePath(), "-jar", file.absolutePath()}
                            );
                            System.exit(0);
                        }catch(IOException e){
                            ui.showException(e);
                        }
                    }, e -> {
                        dialog.hide();
                        ui.showException(e);
                    });

                    dialog.cont.add(new Bar(() -> length[0] == 0 ? Core.bundle.get("be.updating") : (int)(progress[0] * length[0]) / 1024/ 1024 + "/" + length[0]/1024/1024 + " MB", () -> Pal.accent, () -> progress[0])).width(400f).height(70f);
                    dialog.buttons.button("@cancel", Icon.cancel, () -> {
                        cancel[0] = true;
                        dialog.hide();
                    }).size(210f, 64f);
                    dialog.setFillParent(false);
                    dialog.show();
                }catch(Exception e){
                    ui.showException(e);
                }
            }, () -> checkUpdates = false);
        }else{
            Log.info("&lcA new update is available: &lyBleeding Edge build @", updateBuild);
            if(Config.autoUpdate.bool()){
                Log.info("&lcAuto-downloading next version...");

                try{
                    //download new file from github
                    Fi source = Fi.get(BeControl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                    Fi dest = source.sibling("server-be-" + updateBuild + ".jar");

                    download(updateUrl, dest,
                    len -> Core.app.post(() -> Log.info("&ly| Size: @ MB.", Strings.fixed((float)len / 1024 / 1024, 2))),
                    progress -> {},
                    () -> false,
                    () -> Core.app.post(() -> {
                        Log.info("&lcSaving...");
                        SaveIO.save(saveDirectory.child("autosavebe." + saveExtension));
                        Log.info("&lcAutosaved.");

                        netServer.kickAll(KickReason.serverRestarting);
                        Threads.sleep(500);

                        Log.info("&lcVersion downloaded, exiting. Note that if you are not using a auto-restart script, the server will not restart automatically.");
                        //replace old file with new
                        dest.copyTo(source);
                        dest.delete();
                        System.exit(2); //this will cause a restart if using the script
                    }),
                    Throwable::printStackTrace);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            checkUpdates = false;
        }
    }


# Feature Envy
   
   Location: core/src/mindustry/maps/SectorDamage.Java
   
   In this class, there's a method called applyCalculatedDamage(), always calling methods from other classes.
   
   Solution: Doing those operations in their respective classes, so it doesn't.

   //Code snippet
   
      public static void applyCalculatedDamage(){
        //calculate base damage fraction
        float damage = getDamage(state.rules.sector);

        //scaled damage has a power component to make it seem a little more realistic (as systems fail, enemy capturing gets easier and easier)
        float scaled = Mathf.pow(damage, 1.2f);

        Tile spawn = spawner.getFirstSpawn();

        //damage only units near the spawn point
        if(spawn != null){
            Seq<Unit> allies = new Seq<>();
            float sumUnitHealth = 0f;
            for(Unit ally : Groups.unit){
                if(ally.team == state.rules.defaultTeam && ally.within(spawn, state.rules.dropZoneRadius * 2.5f)){
                    allies.add(ally);
                    sumUnitHealth += ally.health;
                }
            }

            allies.sort(u -> u.dst2(spawn));

            //apply damage to units
            float unitDamage = damage * sumUnitHealth;

            //damage units one by one, not uniformly
            for(var u : allies){
                if(u.health < unitDamage){
                    u.remove();
                    unitDamage -= u.health;
                }else{
                    u.health -= unitDamage;
                    break;
                }
            }
        }

        if(state.rules.sector.info.wavesPassed > 0){
            //simply remove each block in the spawner range if a wave passed
            for(Tile spawner : spawner.getSpawns()){
                spawner.circle((int)(state.rules.dropZoneRadius / tilesize), tile -> {
                    if(tile.team() == state.rules.defaultTeam){
                        if(rubble && tile.floor().hasSurface() && Mathf.chance(0.4)){
                            Effect.rubble(tile.build.x, tile.build.y, tile.block().size);
                        }

                        tile.remove();
                    }
                });
            }
        }

        //finally apply scaled damage
        apply(scaled);
    }


# Data Clumps

   Location: core/src/mindustry/editor/MapEditor.Java
   
   In this class, we can see a lot of related data being always used together. For example the size variables like width, 
   heigth, x, y, floorID, etc. Always being used together in a lot of methods. This increases the chance of doing some error and makes the code hard to
   maintain. 
   
   Solution: Create an object using these variables and using this object instead of updating the variables one by one 
   always in the same places.

   //Code snippet

      public void beginEdit(int width, int height){
         reset();

         loading = true;
         createTiles(width, height);
         renderer.resize(width, height);
         loading = false;
      }

      private void createTiles(int width, int height){
         Tiles tiles = world.resize(width, height);

         for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                  tiles.set(x, y, new EditorTile(x, y, Blocks.stone.id, (short)0, (short)0));
            }
         }
      }

      public void resize(int width, int height, int shiftX, int shiftY){
         clearOp();

         Tiles previous = world.tiles;
         int offsetX = (width() - width) / 2 - shiftX, offsetY = (height() - height) / 2 - shiftY;
         loading = true;

         world.clearBuildings();

         Tiles tiles = world.tiles = new Tiles(width, height);

         for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                  int px = offsetX + x, py = offsetY + y;
                  if(previous.in(px, py)){
                     tiles.set(x, y, previous.getn(px, py));
                     Tile tile = tiles.getn(x, y);

                     Object config = null;

                     if(tile.build != null && tile.isCenter()){
                        config = tile.build.config();
                     }

                     tile.x = (short)x;
                     tile.y = (short)y;

                     if(tile.build != null && tile.isCenter()){
                        tile.build.x = x * tilesize + tile.block().offset;
                        tile.build.y = y * tilesize + tile.block().offset;

                        if(config != null){
                              Object out = BuildPlan.pointConfig(tile.block(), config, p -> {
                                 if(!tile.build.block.ignoreResizeConfig){
                                    p.sub(offsetX, offsetY);
                                 }
                              });
                              if(out != config){
                                 boolean prev = state.rules.editor;
                                 state.rules.editor = true;
                                 tile.build.configureAny(out);
                                 state.rules.editor = prev;
                              }
                        }
                     }

                  }else{
                      tiles.set(x, y, new EditorTile(x, y, Blocks.stone.id, (short)0, (short)0));
                  }
            }
         }

         renderer.resize(width, height);
         loading = false;
      }

      

      
   
   