# User story 1
Weekly Challenge
## Author(s)
- Ricardo Pote (68245)
- Miguel Cordeiro (68338)
## Reviewer(s)
(*Please add the user story reviewer(s) here, one in each line, providing the authors' name and surname, along with their student number. In the reviews presented in this document, add the corresponding reviewers.*)
## User Story:
Como jogador frequente de Mindustry, quero ter acesso a um modo de "Desafio Semanal" com um mapa gerado proceduralmente que muda a cada semana, para poder enfrentar situações novas regularmente e ter um motivo recorrente para jogar e tentar superar as pontuações dos meus amigos.
### Review
*(Please add your user story review here)*
## Use case diagram
<img width="787" height="601" alt="image" src="https://github.com/user-attachments/assets/ad53e025-dabf-4b20-ace3-dca3ab1a21ce" />
<img width="865" height="644" alt="image" src="https://github.com/user-attachments/assets/af8f345d-ac37-4910-b0e3-0ebc6d4fa29f" />

## Use case textual description
#### Nome: Play Weekly Challenge 
#### ID: UC1 
#### Descrição: The user starts a new game in the Weekly Challenge mode or resumes an existing one. 
#### Actores:

#### Principais: Player
#### Secundários: Mindustry Game System (Logic, SaveIO)
#### Pré-condições: The game is launched, and the main menu is displayed.

#### Cenário Principal:
- 1º Player selects the "Weekly Challenge" button in the main menu.
- 2º Player selects the play button in the "Weekly Challenge" sub-menu.
- 3º System checks if there is a save file for the current week's seed.
- 4º System determines there is no save file (or the week has changed).
- 5º System generates a new unique seed based on the current week number.
- 6º System generates a new map using WeeklyGenerator with the specific weekly seed.
	- Includes: Terrain generation, biome painting and ore placement.
- 7º System initializes the game state with Infinite mode rules (waves enabled, no win condition, etc).
- 8º System transitions the player to the "Playing" state in the newly generated map. 

#### Cenários Secundários:
- 4a. Resume Game: If a save file exists for the current week:
	- 1º System loads the WeeklyChallenge.msav file.
	- 2º System restores the game state (wave number, buildings, etc).
	- 3º System transitions player to "Playing" state.
- 4b. Corruption: If the save file is corrupted:
	- 1º System detects the error during load.
	- 2º System deletes the corrupted file.
	- 3º System proceeds to generate a new map (Step 5 of Main Scenario). 
#### Pós-condições: The player is playing the Weekly Challenge mode, and a save file is created/updated.

#### Nome: Submit Ranking 
#### ID: UC2 
#### Descrição: The system records the player's progress (highest wave reached) when they are defeated in the Weekly Challenge. 
#### Actores:

#### Principais: Player
#### Secundários:  Mindustry Game System (Logic)
#### Pré-condições: The player is currently playing a Weekly Challenge session. The player's core has been destroyed (Game Over). 

#### Cenário Principal:
- 1º The game detects that the player's core is destroyed (GameOverEvent).
- 2º System verifies that the current map is the "Weekly Challenge".
- 3º System retrieves the current wave number reached.
- 4º System checks the local WeeklyRankingInfo for the player's previous high score.
- 5º System determines the current wave is higher than the previous record.
- 6º System updates the ranking list with the new wave number and player name.
- 7º System saves the updated ranking data to the persistent settings file.
- 8º System displays the "Game Over" screen. 

#### Cenários Secundários:
- 5a. Lower Score: If the current wave is lower than or equal to the existing record:
	- 1º System does not update the ranking.
	- 2º System proceeds to Step 8.
- 4a. New Player: If the player has no previous record:
	- 1º System adds a new entry to the ranking list.
	- 2º System proceeds to Step 7. 
#### Pós-condições: The local high score table is updated with the latest result if it was a record, and the game session is marked as inactive.
### Review
*(Please add your use case review here)*
## Implementation documentation
(*Please add the class diagram(s) illustrating your code evolution, along with a technical description of the changes made by your team. The description may include code snippets if adequate.*)
### Implementation summary

#### 1. Core Management (WeeklyChalServ.java)

- Acts as the central controller for the mode. It manages the session lifecycle, distinguishing between starting a new week's challenge and resuming an existing run.
- It manages lightweight metadata (current wave, active status, rankings) that is serialized to JSON and stored in Core.settings via WeeklyGameInfo and WeeklyRankingInfo. The heavy game state (blocks, units) is managed via the engine's SaveIO system, saving to a dedicated WeeklyChallenge.msav file.
<img width="886" height="372" alt="image" src="https://github.com/user-attachments/assets/c4494457-5edd-488a-a39e-50a08247ae8e" />

- Ensures synchronization between the service and the game logic using specific map tags ("weekly": "Weekly Challenge") to trigger auto-saves correctly.

<img width="692" height="34" alt="image" src="https://github.com/user-attachments/assets/a1340d39-8ee9-4861-8d4a-69e77e5d47f9" />

- Automatically handles save corruption by validating the file upon load and regenerating the map if necessary.
#### 2. Procedural Map Generation (WeeklyGenerator.java)

- It generates the map by painting biomes using offset Simplex noise (to ensure variety), and then "carves" the island shape using a custom trimDark method.
- Includes a ensureOreConnectivity method that uses Raycasting to carve tunnels from ore deposits to the core, ensuring the player is never wall-locked from the ores.

<img width="886" height="874" alt="image" src="https://github.com/user-attachments/assets/707f2318-2399-49fa-8a2f-43636380c93f" />

#### 3. Infinite Wave Progression (InfiniteWaveClass.java)

- Replaced the static list approach (created by a cycle with 1000 iterations, that crashed saves) with a deterministic procedural generation. Waves are calculated in small windows of 5 waves based on the seed and wave number.
<img width="884" height="230" alt="image" src="https://github.com/user-attachments/assets/89cabc85-e137-44c1-bfb1-f45fb75f5f82" />

- Implements a difficulty curve that scales enemy count and tier (Unit Types) based on a mathematical formula, ensuring infinite progression without memory issues.

<img width="880" height="127" alt="image" src="https://github.com/user-attachments/assets/d8d08c0b-b37f-4529-bfd7-befc076baf3a" />

#### 4. Engine Integration (Logic.java)

- Modified the core game loop to intercept reset() (exit to menu) and gameOver() events.
- Hooks into the Game Over event to capture the final wave and submit it to the ranking system only if the player is defeated.
- Modifed wave spawning logic of only the weekly gamemode to properly accommodate the Infinite wave logic, we implemented.
#### 5. User Interface (MenuFragment.java)

- Added a dedicated "Weekly Challenge" button group to the main menu.
<img width="1125" height="89" alt="image" src="https://github.com/user-attachments/assets/ec7500a7-eb9a-4e9a-8d57-d309bba60bfc" />

- Implemented a custom BaseDialog to visualize the WeeklyRankingInfo data, displaying the top 10 runs for the current week.

<img width="1152" height="784" alt="image" src="https://github.com/user-attachments/assets/2de2b0ea-34d1-4524-a980-5cd87176510c" />

#### 6. Gamemode (Gamemode.java)

- Added a special mode called infinite. This separates the Weekly Challenge rules (infinite waves, no win condition) from the standard Survival mode, preventing logic conflicts.

<img width="736" height="459" alt="image" src="https://github.com/user-attachments/assets/eb0ce219-48a5-44b7-bac3-08e9b7b54bd0" />

#### 7. Global Hub (Vars.java)

- It's where we save our variables like InfSpawner, custService and others. They are initialized strictly within the init() lifecycle method. This ensures that all game assets (Settings, Content) are fully loaded before the service starts, preventing NullPointerException crashes on startup.

<img width="488" height="64" alt="image" src="https://github.com/user-attachments/assets/8d4ff471-a95d-4f1a-bb9b-f5b6c11f9044" />

- Here we calculate the seed of our week, based on the difference in weeks between the current system week and the first week of january 2001, using ChronoUnit methods.

<img width="886" height="158" alt="image" src="https://github.com/user-attachments/assets/b465d6d0-011b-46d0-a805-d4a97bcc17b5" />



#### Review
*(Please add your implementation summary review here)*
### Class diagrams



<img width="2304" height="2348" alt="image" src="https://github.com/user-attachments/assets/44af6d8f-178a-42db-955d-8d4f1a4033ef" />

- The WeeklyChalServ acts as the central controller of the weekly challenge mode, it coordinates map generation, wave creation, game progression, and stored game info. It connects Logic, Vars, and Gamemode to the InfiniteWaveClass and WeeklyGenerator, ensuring that seeds, wave data, and world layout are processed correctly. WeeklyGameInfo is the  core of saving  progression logic , while all generators and modifiers feed their output through WeeklyChalServ to maintain consistent weekly gameplay.

  
<img width="856" height="1718" alt="image" src="https://github.com/user-attachments/assets/1da0a574-32d3-4e06-b9de-f7cca47ac256" />


- WeeklyChalServ again serves as the controller, this time managing ranking storage, score submission, and UI access. Logic sends game results to it, while MenuFragment retrieves the processed ranking data. WeeklyRankingInfo holds all submitted scores, and ScoreEntry acts as the record, with WeeklyChalServ managing how these flow between gameplay and interface.
### Review
*(Please add your class diagram review here)*
### Sequence diagrams
(*Sequence diagrams and their discussion in natural language.*)
#### Review
*(Please add your sequence diagram review here)*
## Test specifications
(*Test cases specification and pointers to their implementation, where adequate.*)
### Review
*(Please add your test specification review here)*
