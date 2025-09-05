package com.caixa.caixa_eletronico.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caixa.caixa_eletronico.excpetion.SaldoInsuficienteException;
import com.caixa.caixa_eletronico.model.Conta;
import com.caixa.caixa_eletronico.repository.ContaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaixaService {

    private final ContaRepository contaRepository;

    // Buscar conta por ID
    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada para o ID: " + id));
    }

    // Listar todas as contas
    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    // Criar nova conta
    public Conta criarConta(Conta conta) {
        conta.setId(null); // Garante que será criada uma nova conta
        if (conta.getTitular() == null || conta.getTitular().isBlank()) {
            throw new IllegalArgumentException("Titular não pode ser vazio");
        }
        if (conta.getSaldo() == null) {
            conta.setSaldo(0.0);
        }
        return contaRepository.save(conta);
    }

    // Atualizar conta existente
    public Conta atualizarConta(Long id, Conta dadosAtualizados) {
        Conta contaExistente = buscarPorId(id);
        if (dadosAtualizados.getTitular() == null || dadosAtualizados.getTitular().isBlank()) {
            throw new IllegalArgumentException("Titular não pode ser vazio");
        }
        contaExistente.setTitular(dadosAtualizados.getTitular());
        contaExistente.setSaldo(dadosAtualizados.getSaldo() != null ? dadosAtualizados.getSaldo() : contaExistente.getSaldo());
        return contaRepository.save(contaExistente);
    }

    // Deletar conta por ID
    public void deletarPorId(Long id) {
        if (!contaRepository.existsById(id)) {
            throw new RuntimeException("Conta não existe para o ID: " + id);
        }
        contaRepository.deleteById(id);
    }

    // Realizar saque com controle de concorrência
    @Transactional
    public Double sacar(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Valor inválido para saque");
        }

        Conta conta = buscarPorId(id);

        if (conta.getSaldo() == null) {
            throw new RuntimeException("Saldo da conta está nulo");
        }

        if (conta.getSaldo() < valor) {
            throw new SaldoInsuficienteException(conta.getSaldo(), valor);
        }

        conta.setSaldo(conta.getSaldo() - valor);
        return contaRepository.save(conta).getSaldo();
    }

    // Realizar depósito com controle de concorrência
    @Transactional
    public Double depositar(Long id, Double valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Valor inválido para depósito");
        }

        Conta conta = buscarPorId(id);

        if (conta.getSaldo() == null) {
            conta.setSaldo(0.0);
        }

        conta.setSaldo(conta.getSaldo() + valor);
        return contaRepository.save(conta).getSaldo();
    }
}
