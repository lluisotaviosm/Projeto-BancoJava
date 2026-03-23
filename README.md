# 🏦 Sistema Bancário Java

Projeto desenvolvido para a disciplina de Programação, focado nos pilares de Orientação a Objetos: Herança, Polimorfismo e Encapsulamento.

## 👤 IDENTIFICAÇÃO
- **Nome:** LUIS OTÁVIO SILVA MONTEIRO FRANCO
- **Matrícula:** 2629854
- **Disciplina:** PROJETO DE PROGRAMAÇÃO
- **Professor:** AMAURY NOGUEIRA NETO

---

---

## ARQUITETURA
- **Abstração:** A classe `Conta` é abstrata pois serve apenas como molde, evitando a criação de contas sem um tipo definido.
- **Interfaces:** `Tributavel` e `Autenticavel` definem comportamentos que podem ser compartilhados por classes de hierarquias diferentes.
- **Polimorfismo:** A calculadora de impostos aceita qualquer objeto `Tributavel`, permitindo que o sistema cresça sem precisar mudar a lógica da calculadora.

---

## EXECUÇÃO

## COMO EXECUTAR
1. Instale o JDK (Java Development Kit) 17 ou superior.
2. Coloque todos os arquivos `.java` em uma pasta.
3. No terminal, execute: `javac *.java`
4. Inicie o programa com: `java Main`

---

## FUNCIONALIDADES IMPLEMENTADAS
- **Criar conta:** Suporte para Conta Corrente e Poupança.
- **Listar contas:** Exibe todas as contas no `ArrayList`.
- **Depositar:** Adição de valores com validação.
- **Sacar:** Retirada de valores (com taxa na Corrente).
- **Transferir:** Movimentação entre contas cadastradas.
- **Consultar saldo:** Exibe saldo e histórico de operações.
- **Excluir conta:** (BÔNUS) Remove uma conta do sistema.

---

## EXEMPLO DE EXECUÇÃO
```text
===== BANCO JAVA =====
1 - Criar conta
2 - Listar contas
3 - Depositar
4 - Sacar
5 - Transferir
6 - Consultar saldo / Extrato
7 - Excluir Conta
0 - Sair

Escolha: 1
Nome do titular: Joao
Número da conta: 101
Tipo de conta: 1 - Corrente
Conta criada com sucesso.