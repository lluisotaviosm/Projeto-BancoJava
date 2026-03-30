package com.banco.api.dto;

import lombok.Data;

@Data
public class TransferenciaDTO {
    private int origem;
    private int destino;
    private double valor;
}
