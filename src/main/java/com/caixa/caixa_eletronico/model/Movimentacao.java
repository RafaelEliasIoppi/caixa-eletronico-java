package com.caixa.caixa_eletronico.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Conta conta;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private String tipo; // "DEPOSITO" ou "SAQUE"

    @Column(nullable = false)
    private LocalDateTime dataHora;
}