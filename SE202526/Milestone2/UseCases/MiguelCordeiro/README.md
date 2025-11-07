## UseCases

# Change Log

- 7/11/25 Miguel Cordeiro

This Use Case diagram represents the process through which the Player interacts with and manages the visual aspects. It describes the interactions between the Player and the System, focusing on the actions: configure graphics, receive visual information and in-game events such as watching a cutscene.

Actors:
- Player (Primary Actor): Initiates visual configurations, requests visual information (screenshots or the minimap) and observes system feedback.
- System (Secondary Actor): Manages the rendering logic, provides visual feedback(cutscenes) and processes graphics settings.

Use Cases:
- Graphics Configuration - The player selects and changes graphics settings to customize performance or the style of the game.
- Observe Visual Effects - The player passively receives visual cues from the system, such as screen shake from explosions of the core.
- Watch Minimap - The player can see and interact with the minimap to gain a more insightful and tactical perspective of the game.
- Take a Screenshot - The player commands the system to capture a high-resolution image of the entire map.
- Watch Cutscene - The player views a non-interactive, system-controlled sequence such as the core landing scene.

Relationships:
- Graphics Configuration extends Observe Visual Effects - The player can configure graphics while observing the game's effects.
- Take a Screenshot extends Observe Visual Effects - The player can take a screenshot while observing the game's effects.
- Watch cutscene includes Observe Visual Effects - Watching a cutscene mandatorily includes the observation of visual effects.
- Watch Minimap includes Observe Visual Effects - The player can view the minimap while observing the game's effects.

Diagram:

