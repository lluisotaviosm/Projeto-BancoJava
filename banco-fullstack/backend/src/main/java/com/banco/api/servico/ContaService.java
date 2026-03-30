package com.banco.api.servico;

import com.banco.api.modelo.*;
import com.banco.api.repositorio.ContaRepository;
import com.banco.api.repositorio.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Conta criarConta(Conta conta) {
        if (contaRepository.findByNumero(conta.getNumero()).isPresent()) {
            throw new RuntimeException("Já existe uma conta com este número.");
        }
        Conta novaConta = contaRepository.save(conta);
        registrarTransacao("Conta criada com limites: R$ " + novaConta.getLimiteSaque(), 0.0, novaConta);
        return novaConta;
    }

    public void depositar(int numeroConta, double valor) {
        Conta conta = buscarConta(numeroConta);
        conta.depositar(valor);
        contaRepository.save(conta);
        registrarTransacao("Depósito", valor, conta);
    }

    public void sacar(int numeroConta, double valor) {
        Conta conta = buscarConta(numeroConta);
        if (conta.sacar(valor)) {
            contaRepository.save(conta);
            registrarTransacao("Saque", -valor, conta);
        } else {
            if (conta.isBloqueada()) {
                throw new RuntimeException("Conta bloqueada para saques.");
            }
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
            
            registrarTransacao("Transferência enviada para conta " + numeroDestino, -valor, origem);
            registrarTransacao("Transferência recebida da conta " + numeroOrigem, valor, destino);
        } else {
            throw new RuntimeException("Saldo ou limite insuficiente na conta de origem ou conta bloqueada.");
        }
    }

    public double calcularTributo(int numeroConta) {
        Conta conta = buscarConta(numeroConta);
        if (conta instanceof Tributavel) {
            return ((Tributavel) conta).calcularTributo();
        }
        throw new RuntimeException("Esta conta não é tributável.");
    }

    public List<Conta> listarTodas() {
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
        registrarTransacao(status ? "Conta bloqueada" : "Conta desbloqueada", 0.0, conta);
    }

    // Endpoint Exclusivo (Anti-IA): top-saldos
    public List<Conta> listarTopSaldos() {
        List<Conta> todas = contaRepository.findAll();
        todas.sort((c1, c2) -> Double.compare(c2.getSaldo(), c1.getSaldo())); // Ordem decrescente
        return todas.isEmpty() ? todas : todas.subList(0, Math.min(todas.size(), 5)); // Retorna os top 5
    }

    private void registrarTransacao(String descricao, double valor, Conta conta) {
        Transacao transacao = new Transacao(descricao, valor, conta);
        transacaoRepository.save(transacao);
    }
}
