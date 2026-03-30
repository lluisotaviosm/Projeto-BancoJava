# 🏦 Sistema Bancário Full Stack

Evolução do projeto bancário da disciplina de Programação, focado em implementar uma arquitetura Full Stack com comunicação API REST, banco de dados e front-end web simples.

## 👤 IDENTIFICAÇÃO
- **Nome:** LUIS OTÁVIO SILVA MONTEIRO FRANCO
- **Matrícula:** 2629854
- **Professor:** AMAURY NOGUEIRA NETO

---

## 🏛️ ARQUITETURA DO SISTEMA
O sistema evoluiu para duas unidades de projeto independentes (`backend` e `frontend`) comunicando-se unicamente via JSON:
- **Spring Boot (Backend):** Camada de API rodando no servidor embutido Tomcat (porta 8080).
- **SQLite (Persistência):** Escolhido por ser um banco relacional embaucado num único arquivo (banco.db), dispensando a complexa instalação e configuração de servidores (como MySQL) por parte de quem for rodar o projeto.
- **Divisão em Camadas:** O código Java foi dividido rigorosamente em pacotes em português por conveniência original: `controlador` (recebe as requisições), `servico` (centraliza toda a lógica e validações), `modelo` (Entidades do JPA e OOP) e `repositorio`.

---

## ▶️ COMO EXECUTAR

### Backend (Spring Boot)
1. Certifique-se de ter o JDK 17 e o Maven instalados.
2. Navegue até o diretório `banco-fullstack/backend`:
```bash
cd banco-fullstack/backend
```
3. Execute através do Maven (Isso criará automaticamente o banco SQLite local):
```bash
mvn spring-boot:run
```

### Frontend (HTML, CSS e JS puros)
1. Não exige NPM nem Node.js.
2. Com o backend rodando, vá ao diretório `banco-fullstack/frontend`.
3. Simplesmente dê dois cliques no arquivo `index.html` ou abra-o em qualquer navegador de sua escolha. Use o usuário `admin` e a senha `admin123` para entrar, caso seja pedido.

---

## 🔗 ENDPOINTS DA API REST

Todos os endpoints operam mediante o endereço local (http://localhost:8080/contas) ou `/auth`.

| Método | Rota | Descrição |
|--------|------|-------------|
| POST | `/contas` | Cria uma nova conta (`CORRENTE` ou `POUPANCA`). |
| GET | `/contas` | Retorna o status de todas as contas cadastradas. |
| GET | `/contas/{numero}`| Busca atributos de uma conta pelo seu número. |
| POST | `/contas/depositar` | Recebe DTO com número e o valor, injetando saldo. |
| POST | `/contas/sacar` | Sacar valor, debitando saldo (rejeita caso bloqueado/sem limite). |
| POST | `/contas/transferir`| Subtrai de uma origem; deposita no destino. |
| GET | `/contas/tributos`| Recebe parâmetro na rota com o número da conta retornando a taxa. |
| POST | `/contas/bloquear`| Endpoint que bloqueia movimentações da conta. |
| GET | `/contas/top-saldos`| End-point autoral: Lista os 5 donos maiores posses. |
| POST | `/auth/login` | Rota para validação de entrada ao sistema. |

---

## 📝 EXEMPLO DE REQUISIÇÃO (POST)

**Requisição de criação de conta:**
```http
POST http://localhost:8080/contas
Content-Type: application/json

{
  "titular": "João da Silva",
  "numero": 101,
  "tipo": "CORRENTE"
}
```

**Retorno do Spring (Status 200 OK):**
```json
{
  "id": 1,
  "titular": "João da Silva",
  "numero": 101,
  "saldo": 0.0,
  "limiteSaque": 1000.0,
  "bloqueada": false
}
```

---
*OBS: Prints do funcionamento da interface estão inclusos na entrega.*
