# User story 2
Health Bar
## Author(s)
Andre Narquel (67870)
Joao Fernandes (68180)
## Reviewer(s)

Ricardo Pote (68245)
## User Story:
Como jogadores sentimos dificuldade em perceber o HP (Health Points) de cada Wave de inimigos, e como esse valor é alterado 
pela ação das nossas defesas.
### Review
*(Please add your user story review here)*
## Use case diagram
![Use Case Diagram.png](images/Use%20Case%20Diagram.png)
## Use case textual description
Este use Case representa a adição de Health Bars nas Waves Inimigas, no nosso Player e no nosso Core da base.
O Player sofre dano durante o jogo, fazendo com que a sua Health Bar apareça no ecrã e seja atualizada automaticamente.
As Enemy Waves são geradas pelo Game System, dando spawn aos inimigos que também sofrem dano durante o jogo, fazendo com que 
as suas Health Bars se tornem visíveis e sejam atualizadas automaticamente.
A Health Bar do Core aparece automaticamente no início de cada Wave e desaparece no final, independentemente do dano recebido.

#### Atores:
###### Player (Primary Actor):
- Representa o(s) Player(s) que estão a jogar.

###### Game System (Primary Actor):
- Gera automaticamente as Waves de Inimigos durante o jogo.

#### Use Cases:
- Enemy Waves - O Game System inicia uma Wave de Inimigos.
- Show Core Health Bar - A Health Bar do Core aparece no início da Wave e desaparece no final, independentemente do dano recebido.
- Enemies - Representam os Inimigos que spawnam na Wave.
- Take Damage - Representa o Player e os Inimigos sofrerem dano.
- Show Health Bar - Representa a Health Bar do Player ou dos Inimigos e aparece quando Take Damage ocorre.

#### Relações:
- Enemy Waves includes Enemies - Sempre que há Enemy Waves, Inimigos dão spawn.
- Enemy Waves includes Show Core Health Bar - Sempre que uma Enemy Wave é iniciada, a Health Bar do Core é exibida.
- Take Damage includes Show Health Bar - Sempre que algum Player ou Inimigo levam dano, a Health Bar correspondente é exibida automaticamente.
### Review
*(Please add your use case review here)*
## Implementation documentation
### Implementation summary
Nesta implementação criámos 2 classes novas, a classe HealthBarRenderer() e a classe CoreHealthBar().
A classe HealthBarRenderer() é responsável por desenhar todas as barras de vida dos units presentes no jogo, percorrendo as
Units presentes no EntityGroup<Unit>.
Cada Unit possui uma referência a uma Team, que determina a cor da barra, caso seja da mesma equipa ou um inimigo.
![Defining Bar Color](images/Defining%20Bar%20Color.png) 

Ao percorrer cada Unit vai também procurar informações sobre a sua vida e o seu tamanho.
![Units Health Info](images/Units%20Health%20Info.png)

No fim, a classe é chamada na classe Renderer() para se desenhar assim a sua Health Bar.
![Drawing Health Bar](images/Drawing%20Health%20Bar.png)

A classe CoreHealthBar() é tratada como uma extensão de Elements. Nesta classe desejamos saber informações do Core no jogo, indo buscar
informações ao Core da Team().
![Core Health Info](images/Core%20Health%20Info.png)

Esta classe acaba por ser instanciada na classe HudFragments(), permitindo assim que a barra seja exibida constantemente 
no ecrã durante as Enemy Waves, independentemente do dano recebido, desaparecendo assim que a Wave acaba.
![Instance of CoreHealthBar](images/Instance%20of%20CoreHealthBar.png)
#### Review(Ricardo Pote 68245 REVIEWER)
O objectivo deste codigo é desenhar barras de vida, acredito que isto esteja corretamente identificado tambem me que fizeram  bem ao identificarem onde as duas classes criadas são utilizadas, de forma que concluo que a esta implementation summary faz um bom trabalho e é uma ferramente util para a comprensão do processo de implementação desta funcionalidade.
### Class diagrams
![Class Diagram.png](images/Class%20Diagram.png)
### Review
*(Please add your class diagram review here)*
### Sequence diagrams
![Sequence Diagram 1](images/Sequence%20Diagram%201.png)
- O processo da Core Health Bar inicia-se com o HudFragments a criar uma nova instância da CoreHealthBar, que se inicializa 
definindo o seu tamanho através da classe Element. De seguida, no ciclo de desenho (draw), a CoreHealthBar vai buscar os dados 
através da abstração CoreBuild, que obtém a vida atual e máxima do Core. Com os dados retornados, a barra executa uma lógica 
condicional para decidir a cor (selecionando COLOR_LOW_HEALTH se a vida for baixa, ou COLOR_DEFAULT caso contrário) e, após 
a seleção, finaliza o processo ao renderizar a Health Bar na interface.

![Sequence Diagram 2](images/Sequence%20Diagram%202.png)
- O processo inicia-se com o Renderer a invocar o método drawHealthBars() no HealthBarRenderer, que imediatamente passa a 
iterar sobre cada unidade contidas no EntityGroup através do método each(). Para cada unidade obtida, vai buscar a vida atual,
vida máxima possível, e o id da equipa de cada Unit, e compara-o com o id da equipa do Player. Com base nesta comparação, a cor da 
Health Bar é selecionada, entre COLOR_ALLY ou COLOR_ENEMY_X (X dependene da vida atual) e executa a renderização final ao chamar
o método Draw(), desenhando assim a Health Bar final.
#### Review
*(Please add your sequence diagram review here)*
## Test specifications
(*Test cases specification and pointers to their implementation, where adequate.*)
### Review
*(Please add your test specification review here)*
