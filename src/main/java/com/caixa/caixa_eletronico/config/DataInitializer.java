package com.caixa.caixa_eletronico.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.caixa.caixa_eletronico.model.Conta;
import com.caixa.caixa_eletronico.repository.ContaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ContaRepository contaRepository;

    @Override
    public void run(String... args) {
        if (contaRepository.count() == 0) {
            Conta conta = new Conta();
            conta.setTitular("João da Silva");
            conta.setSaldo(1000.0);
            conta.setSenha("123456");
            contaRepository.save(conta);

            System.out.println("✅ Conta inicial criada: João da Silva com R$ 1000.00 - Senha 123456");
        }
    }
}
