package com.banco.api.dto;

import lombok.Data;

@Data
public class CriarContaDTO {
    private String titular;
    private int numero;
    private String tipo; // CORRENTE ou POUPANCA
}
