package com.banco.api.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta")
@Getter
@Setter
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titular;
    
    @Column(unique = true)
    private int numero;
    
    protected double saldo;
    private double limiteSaque;
    private boolean bloqueada;

    public Conta() {
    }

    public Conta(String titular, int numero, double limiteSaque) {
        this.titular = titular;
        this.numero = numero;
        this.limiteSaque = limiteSaque;
        this.saldo = 0.0;
        this.bloqueada = false;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
        }
    }

    public boolean sacar(double valor) {
        if (bloqueada) return false;

        if (valor > 0 && valor <= limiteSaque && this.saldo >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }
}
