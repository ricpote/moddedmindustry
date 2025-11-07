## UseCases

## Change Log
- 7/11/2025 - Joao Fernandes

Base Defense with Logic

In this Use Case, the player automates base defense and management using logic processors, sensors, and drones. The 
diagram shows how the player, the game system, and automation devices interact to quickly react to attacks or events, 
keeping the base safe without constant intervention.

Primary Actor:
  Player – The player who programs logic processors, places sensors, and defines drone routes to automate base defense.

Secondary Actors: 
  Game System – Executes commands from the logic processor, fires turrets, controls drones, and activates defense systems.
  Sensors & Drones – Detect enemies, monitor areas, and execute automatic orders based on signals from the logic processor.


Use Cases: 
  Place Logic Processor – The player places a logic processor in the base to manage automated systems.

  Program Logic – The player writes scripts or sets instructions for turrets, sensors, and drones to react to events.

  Place Sensors – The player positions sensors that detect enemies or environmental changes.

  Automate Defense – The system fires turrets, sends drones, and activates mechanisms automatically based on logic.

  Monitor System – The player observes automation efficiency, detection rates, and responses to attacks.

  Adjust Logic – The player modifies scripts or logic processor parameters to improve responses or correct failures.
  
Relationships:
  Place Logic Processor extends Program Logic – Without a processor, the logic cannot be executed.

  Automate Defense includes Place Sensors – Automation depends on correct detection of events by sensors.

  Monitor System extends Automate Defense – Monitoring is optional and depends on automation being active.

  Adjust Logic extends Monitor System – Adjustments only occur after the player identifies failures or improvement opportunities.