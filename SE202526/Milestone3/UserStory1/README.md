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
(*Please add the use case diagram here.*)
## Use case textual description
Nome: Play Weekly Challenge 
ID: UC1 
Descrição: The user starts a new game in the Weekly Challenge mode or resumes an existing one. 
Actores:

Principais: Player
Secundários: Mindustry Game System (Logic, SaveIO)
Pré-condições: The game is launched, and the main menu is displayed.

Cenário Principal:
1 - Player selects the "Weekly Challenge" button in the main menu.
2 - Player selects the play button in the "Weekly Challenge" sub-menu.
3 - System checks if there is a save file for the current week's seed.
4 - System determines there is no save file (or the week has changed).
5 - System generates a new unique seed based on the current week number.
6 - System generates a new map using WeeklyGenerator with the specific weekly seed.
	Includes: Terrain generation, biome painting and ore placement.
7 - System initializes the game state with Infinite mode rules (waves enabled, no win condition, etc).
8 - System transitions the player to the "Playing" state in the newly generated map. 

Cenários Secundários:
4a. Resume Game: If a save file exists for the current week:
	1 - System loads the WeeklyChallenge.msav file.
	2 - System restores the game state (wave number, buildings, etc).
	3 - System transitions player to "Playing" state.
4b. Corruption: If the save file is corrupted:
	1 - System detects the error during load.
	2 - System deletes the corrupted file.
	3 - System proceeds to generate a new map (Step 5 of Main Scenario). 
Pós-condições: The player is playing the Weekly Challenge mode, and a save file is created/updated.

Nome: Submit Ranking 
ID: UC2 
Descrição: The system records the player's progress (highest wave reached) when they are defeated in the Weekly Challenge. 
Actores:

Principais: Player
Secundários:  Mindustry Game System (Logic)
Pré-condições: The player is currently playing a Weekly Challenge session. The player's core has been destroyed (Game Over). 

Cenário Principal:
1 - The game detects that the player's core is destroyed (GameOverEvent).
2 - System verifies that the current map is the "Weekly Challenge".
3 - System retrieves the current wave number reached.
4 - System checks the local WeeklyRankingInfo for the player's previous high score.
5 - System determines the current wave is higher than the previous record.
6 - System updates the ranking list with the new wave number and player name.
7 - System saves the updated ranking data to the persistent settings file.
8 - System displays the "Game Over" screen. 

Cenários Secundários:
5a. Lower Score: If the current wave is lower than or equal to the existing record:
	1 - System does not update the ranking.
	2 - System proceeds to Step 8.
4a. New Player: If the player has no previous record:
	1 - System adds a new entry to the ranking list.
	2 - System proceeds to Step 7. 
Pós-condições: The local high score table is updated with the latest result if it was a record, and the game session is marked as inactive.
### Review
*(Please add your use case review here)*
## Implementation documentation
(*Please add the class diagram(s) illustrating your code evolution, along with a technical description of the changes made by your team. The description may include code snippets if adequate.*)
### Implementation summary
(*Summary description of the implementation.*)
#### Review
*(Please add your implementation summary review here)*
### Class diagrams
(*Class diagrams and their discussion in natural language.*)
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
