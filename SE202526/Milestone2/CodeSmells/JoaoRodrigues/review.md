## Review

## Change log
- 07/11/25 - Miguel Cordeiro (Reviewed Duplicated Code)
- 07/11/25 - André Narquel (Reviewed Large Class)
- 07/11/25 - Tomás Silva (Reviewed Long Method)

# Large Class
The code smell analysis is accurate. Vars is a Large Class violating the Single Responsibility Principle, as it mixes
methods like init(), loadSettings(), and loadLogger() with its original purpose of storing variables.
As correctly mentioned, refactoring into separate managers would improve cohesion, modularity, and testability.

# Duplicated Code
- This code smell is well identified and the solution to the problem is adequate. The example was well chosen because 
the two methods are basically identical only with small changes. The solution is perfect for this type of code smell.

# Long Method
- A análise está correta ao diagnosticar o método updateTile() (em BeamDrill.java) com o code smell "Long Method". 
Este método complexo viola diretamente o Princípio da Responsabilidade Única (SRP), acumulando funções díspares como 
inicialização de lasers, cálculo de boost, progresso de perfuração e gestão de recursos (dumping). A refatoração 
proposta—separar estas responsabilidades em métodos privados específicos—é a medida essencial para aumentar a coesão e 
garantir a segurança na manutenção e testabilidade isolada de cada componente.
