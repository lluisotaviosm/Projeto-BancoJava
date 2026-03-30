package com.banco.api.modelo;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta")
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

    // Getters e Setters Manuais (Sem Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getLimiteSaque() {
        return limiteSaque;
    }

    public void setLimiteSaque(double limiteSaque) {
        this.limiteSaque = limiteSaque;
    }

    public boolean isBloqueada() {
        return bloqueada;
    }

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
    }
}
