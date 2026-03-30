package com.banco.api.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private double valor;
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    public Transacao() {
    }

    public Transacao(String descricao, double valor, Conta conta) {
        this.descricao = descricao;
        this.valor = valor;
        this.conta = conta;
        this.dataHora = LocalDateTime.now();
    }
}
