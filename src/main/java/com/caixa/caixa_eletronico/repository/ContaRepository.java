package com.caixa.caixa_eletronico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caixa.caixa_eletronico.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}