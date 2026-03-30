const API_URL = "http://localhost:8080";

// Referência do número da conta do cliente logado
let contaAtual = null;

// =============================================
// HELPERS
// =============================================

function tratarErro(err, idMsg) {
    if (err.message && err.message.includes("Failed to fetch")) {
        document.getElementById('backend-error').style.display = 'flex';
    } else if (idMsg) {
        setMsg(idMsg, err.message || "Erro desconhecido.", false);
    }
}

function setMsg(id, texto, sucesso) {
    const el = document.getElementById(id);
    if (!el) return;
    el.innerText = texto;
    el.className = "msg " + (sucesso ? "ok" : "err");
}

function fecharModalErro() {
    document.getElementById('backend-error').style.display = 'none';
}

// =============================================
// TABS (Área do Cliente)
// =============================================

function mostrarTab(aba) {
    document.getElementById('aba-criar').style.display    = (aba === 'criar')   ? 'block' : 'none';
    document.getElementById('aba-acessar').style.display  = (aba === 'acessar') ? 'block' : 'none';
    document.getElementById('tab-criar').classList.toggle('active',   aba === 'criar');
    document.getElementById('tab-acessar').classList.toggle('active', aba === 'acessar');
}

// =============================================
// NAVEGAÇÃO ENTRE PAINÉIS
// =============================================

function mostrarPainelCliente(conta) {
    contaAtual = conta;
    document.getElementById('tela-inicial').style.display  = 'none';
    document.getElementById('painel-gerente').style.display = 'none';
    document.getElementById('painel-cliente').style.display = 'block';
    document.getElementById('nome-cliente').innerText   = conta.titular;
    document.getElementById('numero-cliente').innerText = conta.numero;
    document.getElementById('saldo-cliente').innerText  = conta.saldo.toFixed(2);
}

function voltarInicio() {
    contaAtual = null;
    document.getElementById('painel-cliente').style.display = 'none';
    document.getElementById('painel-gerente').style.display = 'none';
    document.getElementById('tela-inicial').style.display   = 'grid';
}

// =============================================
// CRIAR CONTA
// =============================================

function criarConta() {
    const titular = document.getElementById("titular").value.trim();
    const numero  = parseInt(document.getElementById("numero-nova").value);
    const tipo    = document.getElementById("tipo-conta").value;

    if (!titular || !numero) {
        setMsg("msg-criar", "Preencha todos os campos.", false);
        return;
    }

    fetch(`${API_URL}/contas`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ titular, numero, tipo })
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Esse número de conta já pode estar em uso.");
    })
    .then(conta => {
        setMsg("msg-criar", "Conta aberta com sucesso!", true);
        setTimeout(() => mostrarPainelCliente(conta), 800);
    })
    .catch(err => tratarErro(err, "msg-criar"));
}

// =============================================
// ACESSAR CONTA EXISTENTE
// =============================================

function acessarConta() {
    const numero = parseInt(document.getElementById("numero-acesso").value);

    if (!numero) {
        setMsg("msg-acesso", "Digite o número da conta.", false);
        return;
    }

    fetch(`${API_URL}/contas/${numero}`)
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Conta não encontrada.");
    })
    .then(conta => mostrarPainelCliente(conta))
    .catch(err => tratarErro(err, "msg-acesso"));
}

// =============================================
// ATUALIZAR SALDO NO PAINEL DO CLIENTE
// =============================================

function atualizarSaldo() {
    if (!contaAtual) return;
    fetch(`${API_URL}/contas/${contaAtual.numero}`)
    .then(res => res.json())
    .then(conta => {
        contaAtual = conta;
        document.getElementById('saldo-cliente').innerText = conta.saldo.toFixed(2);
    })
    .catch(() => {});
}

// =============================================
// OPERAÇÕES DO CLIENTE
// =============================================

function depositar() {
    const valor = parseFloat(document.getElementById("valor-dep").value);
    fetch(`${API_URL}/contas/depositar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ numero: contaAtual.numero, valor })
    })
    .then(async res => {
        const msg = await res.text();
        setMsg("msg-dep", msg, res.ok);
        if (res.ok) { atualizarSaldo(); document.getElementById("valor-dep").value = ""; }
    })
    .catch(err => tratarErro(err, "msg-dep"));
}

function sacar() {
    const valor = parseFloat(document.getElementById("valor-saque").value);
    fetch(`${API_URL}/contas/sacar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ numero: contaAtual.numero, valor })
    })
    .then(async res => {
        const msg = await res.text();
        setMsg("msg-saque", msg, res.ok);
        if (res.ok) { atualizarSaldo(); document.getElementById("valor-saque").value = ""; }
    })
    .catch(err => tratarErro(err, "msg-saque"));
}

function transferir() {
    const destino = parseInt(document.getElementById("destino-transf").value);
    const valor   = parseFloat(document.getElementById("valor-transf").value);
    fetch(`${API_URL}/contas/transferir`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ origem: contaAtual.numero, destino, valor })
    })
    .then(async res => {
        const msg = await res.text();
        setMsg("msg-transf", msg, res.ok);
        if (res.ok) { atualizarSaldo(); document.getElementById("valor-transf").value = ""; }
    })
    .catch(err => tratarErro(err, "msg-transf"));
}

function calcularTributo() {
    fetch(`${API_URL}/contas/tributos?numero=${contaAtual.numero}`)
    .then(res => res.text())
    .then(msg => setMsg("msg-tributo", msg, true))
    .catch(err => {
        if (err.message.includes("Failed")) { tratarErro(err, null); }
        else setMsg("msg-tributo", "Esta conta não possui tributos (conta poupança).", false);
    });
}

// =============================================
// LOGIN DO GERENTE
// =============================================

function loginGerente() {
    const user = document.getElementById("username").value;
    const pass = document.getElementById("password").value;

    fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ usuario: user, senha: pass })
    })
    .then(res => {
        if (res.ok) return res.text();
        throw new Error("Usuário ou senha inválidos.");
    })
    .then(() => {
        document.getElementById('tela-inicial').style.display   = 'none';
        document.getElementById('painel-gerente').style.display = 'block';
        listarContas();
    })
    .catch(err => tratarErro(err, "login-msg"));
}

// =============================================
// OPERAÇÕES DO GERENTE
// =============================================

function listarContas() {
    fetch(`${API_URL}/contas`)
    .then(res => res.json())
    .then(data => renderizarLista(data, "lista-contas"))
    .catch(err => tratarErro(err));
}

function renderizarLista(data, elementId) {
    const lista = document.getElementById(elementId);
    lista.innerHTML = "";

    if (!data || data.length === 0) {
        lista.innerHTML = "<li style='color:#9ca3af;'>Nenhuma conta cadastrada.</li>";
        return;
    }

    data.forEach(conta => {
        const li = document.createElement("li");
        const tipoBadge = conta.limiteSaque > 500
            ? `<span class="badge badge-corrente">Corrente</span>`
            : `<span class="badge badge-poupanca">Poupança</span>`;
        const bloqBadge = conta.bloqueada
            ? ` <span class="badge badge-bloqueada">Bloqueada</span>` : "";

        li.innerHTML = `
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <div>
                    <strong>#${conta.numero} — ${conta.titular}</strong><br>
                    <span style="color:#059669;font-weight:700;">R$ ${conta.saldo.toFixed(2)}</span>
                </div>
                <div>${tipoBadge}${bloqBadge}</div>
            </div>`;
        lista.appendChild(li);
    });
}

function alterarBloqueio() {
    const numero = parseInt(document.getElementById("numero-bloqueio").value);
    const status = document.getElementById("status-bloqueio").value === "true";

    fetch(`${API_URL}/contas/bloquear`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ numero, status })
    })
    .then(async res => {
        const msg = await res.text();
        setMsg("msg-bloqueio", msg, res.ok);
        if (res.ok) listarContas();
    })
    .catch(err => tratarErro(err, "msg-bloqueio"));
}

function topSaldos() {
    fetch(`${API_URL}/contas/top-saldos`)
    .then(res => res.json())
    .then(data => renderizarLista(data, "lista-contas"))
    .catch(err => tratarErro(err));
}
