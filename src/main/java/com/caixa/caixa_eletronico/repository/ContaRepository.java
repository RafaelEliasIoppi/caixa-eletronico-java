package com.caixa.caixa_eletronico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caixa.caixa_eletronico.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    // Buscar conta por titular
    Optional<Conta> findByTitular(String titular);

    // Buscar conta por titular e senha (útil para autenticação simples)
    Optional<Conta> findByTitularAndSenha(String titular, String senha);
}