package com.banco.api.controlador;

import com.banco.api.dto.CriarContaDTO;
import com.banco.api.dto.OperacaoDTO;
import com.banco.api.dto.TransferenciaDTO;
import com.banco.api.modelo.Conta;
import com.banco.api.modelo.ContaCorrente;
import com.banco.api.modelo.ContaPoupanca;
import com.banco.api.servico.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*") // Permite acesso do front-end
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody CriarContaDTO dto) {
        Conta conta;
        if ("CORRENTE".equalsIgnoreCase(dto.getTipo())) {
            conta = new ContaCorrente(dto.getTitular(), dto.getNumero());
        } else if ("POUPANCA".equalsIgnoreCase(dto.getTipo())) {
            conta = new ContaPoupanca(dto.getTitular(), dto.getNumero());
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        Conta criada = contaService.criarConta(conta);
        return ResponseEntity.ok(criada);
    }

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        return ResponseEntity.ok(contaService.listarTodas());
    }

    @GetMapping("/{numero}")
    public ResponseEntity<Conta> buscarConta(@PathVariable int numero) {
        try {
            return ResponseEntity.ok(contaService.buscarConta(numero));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/depositar")
    public ResponseEntity<String> depositar(@RequestBody OperacaoDTO dto) {
        try {
            contaService.depositar(dto.getNumero(), dto.getValor());
            return ResponseEntity.ok("Depósito realizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/sacar")
    public ResponseEntity<String> sacar(@RequestBody OperacaoDTO dto) {
        try {
            contaService.sacar(dto.getNumero(), dto.getValor());
            return ResponseEntity.ok("Saque realizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaDTO dto) {
        try {
            contaService.transferir(dto.getOrigem(), dto.getDestino(), dto.getValor());
            return ResponseEntity.ok("Transferência realizada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tributos")
    public ResponseEntity<String> calcularTributo(@RequestParam int numero) {
        try {
            double tributo = contaService.calcularTributo(numero);
            return ResponseEntity.ok("Tributo calculado: R$ " + String.format("%.2f", tributo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bloquear")
    public ResponseEntity<String> bloquearConta(@RequestBody Map<String, Object> body) {
        try {
            int numero = (Integer) body.get("numero");
            boolean status = (Boolean) body.get("status");
            contaService.alterarBloqueio(numero, status);
            return ResponseEntity.ok("Bloqueio alterado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/top-saldos")
    public ResponseEntity<List<Conta>> topSaldos() {
        return ResponseEntity.ok(contaService.listarTopSaldos());
    }
}
