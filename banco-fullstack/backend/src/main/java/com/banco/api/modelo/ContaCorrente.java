package com.banco.api.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends Conta implements Tributavel {

    public ContaCorrente() {
    }

    public ContaCorrente(String titular, int numero) {
        super(titular, numero, 1000.0);
    }

    @Override
    public double calcularTributo() {
        return this.saldo * 0.1;
    }
}
