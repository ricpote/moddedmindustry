# User story 3
Natural Disasters
## Author(s)
- João Rodrigues (67912)
- Tomás Silva (68725)
## Reviewer(s)
(*Please add the user story reviewer(s) here, one in each line, providing the authors' name and surname, along with their student number. In the reviews presented in this document, add the corresponding reviewers.*)
## User Story:
Como jogadores achamos que as condições climáticas e eventos naturais deviam ter mais impacto no jogo, poderiam haver catástrofes naturais que interferiam diretamente com as construções dos jogadores de um modo que estes não podessem simplesmente selecionar e clicar B para corrigir (como tsunamis, terramotos ou meteoritos).
### Review
*(Please add your user story review here)* 
## Use case diagram
![Use Case Diagram (UserStory3).png](Use%20Case%20Diagram%20%28UserStory3%29.png)
## Use case textual description
Este Use Case Diagram representa o funcionamento do sistema de Desastres Naturais implementado para o jogo Mindustry.
Os eventos — Meteor, Earthquake e Tsunami — são gerados autonomamente pelo Game System, em locais e momentos aleatórios.
As construções são afetadas de várias maneiras diferentes consoante a catástrofe em causa.
 
#### Atores:
###### Game System (Primary Actor):
- Gera automaticamente desastres naturais baseado em regras internas, ciclos temporais e probabilidades.
- Executa a lógica de cada tipo de desastre.
- Modifica o mapa e constrói os efeitos físicos nos objetos atingidos.
###### Structures (Secondary Actor)
- Representam as construções que podem ser destruídas, baralhadas ou empurradas.

#### Use Cases:
- Generate Meteor Event - O Game System ativa o Meteor Event
- Generate Tsunami Event - O Game System ativa o Tsunami Event
- Generate Earthquake Event - O Game System ativa o Earthquake Event
- Destroy Structures - As Estruturas são destruídas como efeito da catástrofe.
- Push Structures - As Estruturas são empurradas como efeito da catástrofe.
- Shuffle Structures - As Estruturas são baralhadas como efeito da catástrofe.
#### Relações:
- Generate Meteor Event includes Destroy Structures - O Generate Meteor Event inclui sempre a destruição de estruturas (Destroy Structures).
- Generate Tsunami Event includes Pushes Structures - O Generate Tsunami Event inclui sempre o empurrão de estruturas (Push Structures).
- Generate Earthquake Event includes Shuffles Structures - O Generate Earthquake Event inclui sempre a embaralhar de estruturas (Shuffle Structures).
- Destroy Structures extends Generate Tsunami Event - O Generate Tsunami Event poderá causar a destruição de estruturas (Destroy Structures).
- Destroy Structures extends Generate Earthquake Event - O Generate Earthquake Event poderá causar a destruição de estruturas (Destroy Structures).
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
