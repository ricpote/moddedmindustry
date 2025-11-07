## UseCases

## Change Log
-7/11/2025 Tomás Silva


This Use Case Diagram represents the process through which the player initiates and manages the automated construction
of a base and the resource production chain in the game Mindustry. It describes the interactions between the player,
the game system, and the materials, focusing on the actions necessary to collect, process, and distribute resources in
a self-sustaining manner.

Actors:
- Player (Primary Actor): Initiate the construction, configuration, and management of the resource supply chain.
- Game Sistem (Secondary Actor): Manages the construction logic, verifies resource availability, calculates material
  processing, and controls energy consumption.
- Resources (Material) (Secondary Actor): Represents the extracted and processed materials (inputs/outputs) that are
  consumed or produced in the Use Cases.

Use Cases:
- Gather Resources - The player commands the central unit or miners to extract raw materials from the map.
- Build Structure - The player selects and places a structure (e.g., conveyor, miner, crafter) on the map,
  consuming resources.
- Configure Production Chain - The player connects structures with conveyors to automate the flow of materials
  between miners and crafters.
- Process Material - A crafter or processor receives materials (inputs) and transforms them into a new material
  (output), following a recipe.
- Manage Power - The player constructs generators and distributes power to the structures that require it to operate.


Relationships:
- Configure Production Chain includes Build Structure - The configuration of the production chain always involves
  placing structures/buildings.
- Configure Production Chain includes Process Material - The objective of the chain is always to ensure that the
  material is processed.
- Build Structure includes Gather Resources - Construction is only validated if the resources are subtracted from
  the inventory.
- Build Structure extends Manage Power - The structure may require power; the player may (optionally) have to
  configure the power connection.

Diagram:
![Use Case Diagram.png](Use%20Case%20Diagram.png)
