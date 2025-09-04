package com.caixa.caixa_eletronico.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
            .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
    }

    // Listar todas as contas
    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    // Criar ou atualizar conta
    public Conta salvarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    // Deletar conta por ID
    public void deletarPorId(Long id) {
        if (!contaRepository.existsById(id)) {
            throw new RuntimeException("Conta não existe");
        }
        contaRepository.deleteById(id);
    }

    // Realizar saque
    public Double sacar(Long id, Double valor) {
        Conta conta = buscarPorId(id);
        if (conta.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        contaRepository.save(conta);
        return conta.getSaldo();
    }

    // Realizar depósito
    public Double depositar(Long id, Double valor) {
        Conta conta = buscarPorId(id);
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);
        return conta.getSaldo();
    }
}
