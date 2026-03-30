# Decisões Arquiteturais e de Implementação

## 1. Arquitetura do Sistema
Optei por utilizar uma arquitetura padrão em camadas utilizando Spring Boot, com a seguinte divisão:
- **Modelo (`modelo`)**: As entidades de banco de dados mapeadas com JPA (`Conta`, `ContaCorrente`, `ContaPoupanca`, `Transacao`). Utilizei a estratégia de herança `SINGLE_TABLE` para deixar o banco SQLite mais simples, pois teremos pouco volume de dados neste contexto acadêmico.
- **Repositório (`repositorio`)**: Interfaces centralizando a comunicação com o banco através do Spring Data JPA.
- **Serviço (`servico`)**: Isola toda a regra de tributo, saques, depósitos bloqueios, evitando colocar regra de negócio na camada de controle ou pior, no front-end.
- **Controlador (`controlador`)**: A camada expõe os endpoints REST para o Frontend consumir (com suporte ao CORS via `@CrossOrigin` para contornar problemas de Same-Origin-Policy).
- **DTOs (`dto`)**: Foram criados para estruturar a entrada das informações no ato de depósitos e transferências sem expor inteiramente ou corromper o modelo interno de banco de dados.

Optei por nomear os pacotes das camadas de `modelo`, `servico`, `repositorio` e `controlador` (em português) para respeitar o padrão de nomenclatura que a turma vinha utilizando nos projetos acadêmicos e manter as raízes do projeto inicial. O Front e Back-end mantive os nomes em inglês por ser padrão forte da indústria (`frontend` / `backend`).

## 2. Interface (Frontend)
Foi solicitado um front-end ou camada Desktop. Escolhi HTML/CSS/JS (Web) puro e simples para simular perfeitamente um fluxo real entre API REST e Client Web, através da `Fetch API`. Optei por não utilizar um framework pesado como React ou Angular para evitar dependências adicionais e manter a execução acessível ao professor. A identidade visual foi feita "do zero", sem bootstrap ou afins.

## 3. Requisito Autoral (Anti-IA)
Foi adicionada a funcionalidade de Bloqueio da Conta como diferencial: uma conta bloqueada não pode fazer saques nem transferir (apenas receber depósitos).
Como endpoint exigido na regra, criei `/contas/top-saldos`, que puxa as 5 contas mais ricas do banco.

## 4. Dificuldades Encontradas
Uma pequena dificuldade surgiu ao configurar o autocommit do SQLite junto ao Spring Boot 3. Acabou sendo resolvido apenas passando para a versão padrão de Dialeto Comunitário do Hibernate, que já suporta SQLite de forma orgânica. 
Outra restrição sentida foi em simular autenticação. Para não complicar a avaliação, fiz um AuthController estático (`admin` / `admin123`) e validei o acesso à página, focado apenas no fluxo da API.
