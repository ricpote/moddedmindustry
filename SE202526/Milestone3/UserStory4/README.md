# User story 3
Trading System
## Author(s)
- André Narquel (67870)
- João Fernandes (68180)
## Reviewer(s)
(*Please add the user story reviewer(s) here, one in each line, providing the authors' name and surname, along with their student number. In the reviews presented in this document, add the corresponding reviewers.*)
## User Story:
Como jogadores sentimos que certos minerais perdem utilidade e por consequência são muito acumulados durante o jogo e por contrário 
certos minerais são complicados de armazenar e acabamos por ter sempre muito poucos.
### Review
*(Please add your user story review here)* 
## Use case diagram
![Use Case Diagram.png](Use Case Diagram.png)

## Use case textual description
Este Use Case representa o funcionamento do Trading System implementado. O Player interage com o Trader,que apresenta 
opções aleatórias de trade de minerais. O Player pode selecionar uma das opções ou decidir não trocar nada.
Cada Trade selecionada é validada pelo Game System, que verifica se o Player possui os recursos necessários e, em caso afirmativo, realiza a troca.
O Player pode realizar até cinco trades, após a quinta trade ou após 10 minutos, as opções de trade são automaticamente resetadas e novas opções são apresentadas.

#### Atores:
###### Player (Primary Actor):
- Interage com o Trader e seleciona uma das opções de trade disponíveis.

###### Game System (Primary Actor):
- Gera as opções de trade aleatórias.
- Valida as Trades selecionadas pelo Player.
- Controla o número de trades feitas e o timer de 10 minutos, resetando as opções quando necessário.

#### Use Cases:
- Trader Interaction – O Player inicia a interação com o Trader.
- Show Trade Options – O Game System apresenta cinco opções de trade aleatórias.
- Select Trade – O Player seleciona uma das opções de trade.
- Validate Trade – O Game System verifica se a trade selecionada é válida, ou seja, se tem recursos suficientes.
- Execute Trade – O Game System realiza a troca de recursos entre Player e Trader.
- Number of Uses – O Game System controla o número de Trades realizadas pelo Player.
- Timer – O Game System controla o tempo decorrido até o reset automático das Trades.
- Reset Trades – O Game System reseta as opções de Trade, seja por ter realizado 5 trades ou por timer de 10 minutos.

#### Relações:
- Trader Interaction includes Show Trade Options – Sempre que o Player interage com o Trader, as cinco opções de trade são apresentadas.
- Show Trade Options includes Select Trade – O Player pode então selecionar uma das opções disponíveis.
- Select Trade includes Validate Trade – Todas as Trades selecionadas pelo Player são validadaa pelo Game System.
- Validate Trade includes Execute Trade – Se a trade for válida, a troca é executada.
- Execute Trade includes Number of Uses – Após a troca ser executada, o contador de trades é atualizado.
- Number of Uses extends Reset Trades – Quando o Player realiza cinco Trades, as opções são resetadas.
- Timer extends Reset Trades – Quando passam 10 minutos, as opções de trade são resetadas automaticamente.
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
