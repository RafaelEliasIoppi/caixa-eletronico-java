package com.caixa.caixa_eletronico.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private Double saldo = 0.0;

    @Column(nullable = false, length = 6) // exemplo: senha de até 6 dígitos
    private String senha;

    @Version
    private Long versao;
}
