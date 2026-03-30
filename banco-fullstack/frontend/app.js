const API_URL = "http://localhost:8080";

// Intercepta erros comuns como o Failed to Fetch (Backend desligado)
function tratarErroFetch(err, idElementoMensagem) {
    if (err.message.includes("Failed to fetch")) {
        document.getElementById('backend-error').style.display = 'flex';
    } else if (idElementoMensagem) {
        document.getElementById(idElementoMensagem).innerText = err.message;
        document.getElementById(idElementoMensagem).className = "msg text-danger";
    }
}

function fecharModalErro() {
    document.getElementById('backend-error').style.display = 'none';
}

function login() {
    const user = document.getElementById("username").value;
    const pass = document.getElementById("password").value;

    fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ usuario: user, senha: pass })
    })
    .then(res => {
        if (res.ok) {
            return res.text();
        } else {
            throw new Error("Credenciais inválidas");
        }
    })
    .then(data => {
        document.getElementById("welcome-screen").style.display = "none";
        document.getElementById("dashboard-container").style.display = "block";
        listarContas(); // Carrega as contas ao logar
    })
    .catch(err => tratarErroFetch(err, "login-msg"));
}

function logout() {
    document.getElementById("dashboard-container").style.display = "none";
    document.getElementById("welcome-screen").style.display = "grid";
    document.getElementById("login-msg").innerText = "";
    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
}

function criarConta() {
    const titular = document.getElementById("titular").value;
    const numeroStr = document.getElementById("numero-nova").value;
    const tipo = document.getElementById("tipo-conta").value;
    
    if(!titular || !numeroStr) {
        document.getElementById("msg-criar").innerText = "Preencha todos os campos.";
        document.getElementById("msg-criar").className = "msg text-danger";
        return;
    }

    const numero = parseInt(numeroStr);

    fetch(`${API_URL}/contas`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ titular, numero, tipo })
    })
    .then(res => {
        if (res.ok) {
            document.getElementById("msg-criar").innerText = "Conta aberta com sucesso! O Gerente já pode visualizar no painel.";
            document.getElementById("msg-criar").className = "msg text-success";
            document.getElementById("titular").value = "";
            document.getElementById("numero-nova").value = "";
            // Se o painel do gerente estiver aberto, atualiza a lista
            if(document.getElementById("dashboard-container").style.display === "block"){
                 listarContas();
            }
        } else {
            document.getElementById("msg-criar").innerText = "Erro ao criar conta. Talvez o número já exista.";
            document.getElementById("msg-criar").className = "msg text-danger";
        }
    })
    .catch(err => tratarErroFetch(err, "msg-criar"));
}

function renderizarLista(data, elementId) {
    const lista = document.getElementById(elementId);
    lista.innerHTML = "";
    
    if(data.length === 0){
        lista.innerHTML = "<p style='color:#6b7280; font-size:0.9rem;'>Nenhuma conta cadastrada.</p>";
        return;
    }

    data.forEach(conta => {
        const li = document.createElement("li");
        const tipoConta = conta.tributo !== undefined ? "Corrente" : "Poupança"; // Uma heuristica simples pelo JSON
        
        let spansExtras = "";
        if(conta.limiteSaque > 500) {
            spansExtras += `<span class="badge badge-corrente">Corrente</span>`;
        } else {
            spansExtras += `<span class="badge badge-poupanca">Poupança</span>`;
        }

        if(conta.bloqueada) {
            spansExtras += ` <span class="badge badge-bloqueada">Conta Bloqueada</span>`;
        }

        li.innerHTML = `
            <div style="font-weight: 600;">#${conta.numero} - ${conta.titular}</div>
            <div style="color: #059669; font-weight: 700; font-size: 1.1rem;">Saldo: R$ ${conta.saldo.toFixed(2)}</div>
            <div style="margin-top: 5px;">${spansExtras}</div>
        `;
        lista.appendChild(li);
    });
}

function listarContas() {
    fetch(`${API_URL}/contas`)
    .then(res => res.json())
    .then(data => {
        renderizarLista(data, "lista-contas");
    })
    .catch(err => tratarErroFetch(err));
}

function resolverOperacaoPadrao(promiseFetch, idMensagem) {
    promiseFetch
    .then(async res => {
        const msgTexto = await res.text();
        const msgEl = document.getElementById(idMensagem);
        if (res.ok) {
            msgEl.innerText = msgTexto;
            msgEl.className = "msg text-success";
            listarContas();
        } else {
            msgEl.innerText = msgTexto;
            msgEl.className = "msg text-danger";
        }
    })
    .catch(err => tratarErroFetch(err, idMensagem));
}

function depositar() {
    const numero = parseInt(document.getElementById("numero-op").value);
    const valor = parseFloat(document.getElementById("valor-op").value);
    const request = fetch(`${API_URL}/contas/depositar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ numero, valor })
    });
    resolverOperacaoPadrao(request, "msg-op");
}

function sacar() {
    const numero = parseInt(document.getElementById("numero-op").value);
    const valor = parseFloat(document.getElementById("valor-op").value);
    const request = fetch(`${API_URL}/contas/sacar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ numero, valor })
    });
    resolverOperacaoPadrao(request, "msg-op");
}

function transferir() {
    const origem = parseInt(document.getElementById("origem-transf").value);
    const destino = parseInt(document.getElementById("destino-transf").value);
    const valor = parseFloat(document.getElementById("valor-transf").value);
    const request = fetch(`${API_URL}/contas/transferir`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ origem, destino, valor })
    });
    resolverOperacaoPadrao(request, "msg-transf");
}

function alterarBloqueio() {
    const numero = parseInt(document.getElementById("numero-bloqueio").value);
    const status = document.getElementById("status-bloqueio").value === "true";
    const request = fetch(`${API_URL}/contas/bloquear`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ numero, status })
    });
    resolverOperacaoPadrao(request, "msg-bloqueio");
}

function topSaldos() {
    fetch(`${API_URL}/contas/top-saldos`)
    .then(res => res.json())
    .then(data => {
        // Redireciona a renderização para a lista de contas mas exibe só os top 5
        renderizarLista(data, "lista-contas");
    })
    .catch(err => tratarErroFetch(err));
}
