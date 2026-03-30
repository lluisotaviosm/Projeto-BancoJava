package com.banco.api.servico;

import com.banco.api.modelo.*;
import com.banco.api.repositorio.ContaRepository;
import com.banco.api.repositorio.TransacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    // Logger que escreve mensagens legíveis no terminal
    private static final Logger log = LoggerFactory.getLogger(ContaService.class);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Conta criarConta(Conta conta) {
        if (contaRepository.findByNumero(conta.getNumero()).isPresent()) {
            throw new RuntimeException("Já existe uma conta com este número.");
        }
        Conta novaConta = contaRepository.save(conta);
        log.info("[CONTA CRIADA] Titular: {} | Conta Nº {} | Tipo: {} | Limite de Saque: R$ {}",
                novaConta.getTitular(), novaConta.getNumero(),
                novaConta instanceof ContaCorrente ? "Corrente" : "Poupança",
                novaConta.getLimiteSaque());
        registrarTransacao("Conta criada com sucesso!", 0.0, novaConta);
        return novaConta;
    }

    public void depositar(int numeroConta, double valor) {
        Conta conta = buscarConta(numeroConta);
        conta.depositar(valor);
        contaRepository.save(conta);
        log.info("[DEPOSITO] {} depositou R$ {} na Conta Nº {} | Novo saldo: R$ {}",
                conta.getTitular(), String.format("%.2f", valor),
                numeroConta, String.format("%.2f", conta.getSaldo()));
        registrarTransacao("Depósito", valor, conta);
    }

    public void sacar(int numeroConta, double valor) {
        Conta conta = buscarConta(numeroConta);
        if (conta.sacar(valor)) {
            contaRepository.save(conta);
            log.info("[SAQUE] {} sacou R$ {} da Conta Nº {} | Novo saldo: R$ {}",
                    conta.getTitular(), String.format("%.2f", valor),
                    numeroConta, String.format("%.2f", conta.getSaldo()));
            registrarTransacao("Saque", -valor, conta);
        } else {
            if (conta.isBloqueada()) {
                log.warn("[SAQUE NEGADO] Tentativa de saque na Conta Nº {} bloqueada.", numeroConta);
                throw new RuntimeException("Conta bloqueada para saques.");
            }
            log.warn("[SAQUE NEGADO] Conta Nº {} sem saldo ou acima do limite.", numeroConta);
            throw new RuntimeException("Saldo ou limite insuficiente.");
        }
    }

    public void transferir(int numeroOrigem, int numeroDestino, double valor) {
        Conta origem = buscarConta(numeroOrigem);
        Conta destino = buscarConta(numeroDestino);

        if (origem.sacar(valor)) {
            destino.depositar(valor);
            contaRepository.save(origem);
            contaRepository.save(destino);
            log.info("[TRANSFERENCIA] {} (Conta {}) transferiu R$ {} para {} (Conta {})",
                    origem.getTitular(), numeroOrigem,
                    String.format("%.2f", valor),
                    destino.getTitular(), numeroDestino);
            registrarTransacao("Transferência enviada para conta " + numeroDestino, -valor, origem);
            registrarTransacao("Transferência recebida da conta " + numeroOrigem, valor, destino);
        } else {
            log.warn("[TRANSFERENCIA NEGADA] Conta Nº {} sem saldo suficiente ou bloqueada.", numeroOrigem);
            throw new RuntimeException("Saldo ou limite insuficiente na conta de origem ou conta bloqueada.");
        }
    }

    public double calcularTributo(int numeroConta) {
        Conta conta = buscarConta(numeroConta);
        if (conta instanceof Tributavel) {
            double tributo = ((Tributavel) conta).calcularTributo();
            log.info("[TRIBUTO] Conta Nº {} de {} | Tributo calculado: R$ {}",
                    numeroConta, conta.getTitular(), String.format("%.2f", tributo));
            return tributo;
        }
        throw new RuntimeException("Esta conta não é tributável.");
    }

    public List<Conta> listarTodas() {
        log.info("[LISTAGEM] Gerente consultou todas as contas cadastradas.");
        return contaRepository.findAll();
    }

    public Conta buscarConta(int numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada."));
    }

    public List<Transacao> listarExtrato(Long contaId) {
        return transacaoRepository.findByContaIdOrderByDataHoraDesc(contaId);
    }

    // Funcionalidade Extra (Anti-IA): Bloqueio de conta
    public void alterarBloqueio(int numeroConta, boolean status) {
        Conta conta = buscarConta(numeroConta);
        conta.setBloqueada(status);
        contaRepository.save(conta);
        if (status) {
            log.warn("[BLOQUEIO] Gerente bloqueou a Conta Nº {} de {}", numeroConta, conta.getTitular());
        } else {
            log.info("[DESBLOQUEIO] Gerente desbloqueou a Conta Nº {} de {}", numeroConta, conta.getTitular());
        }
        registrarTransacao(status ? "Conta bloqueada pelo gerente" : "Conta desbloqueada pelo gerente", 0.0, conta);
    }

    // Endpoint Exclusivo (Anti-IA): top-saldos
    public List<Conta> listarTopSaldos() {
        List<Conta> todas = contaRepository.findAll();
        todas.sort((c1, c2) -> Double.compare(c2.getSaldo(), c1.getSaldo()));
        log.info("[TOP SALDOS] Gerente consultou o ranking dos maiores saldos.");
        return todas.isEmpty() ? todas : todas.subList(0, Math.min(todas.size(), 5));
    }

    private void registrarTransacao(String descricao, double valor, Conta conta) {
        Transacao transacao = new Transacao(descricao, valor, conta);
        transacaoRepository.save(transacao);
    }
}
