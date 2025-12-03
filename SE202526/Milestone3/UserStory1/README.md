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
