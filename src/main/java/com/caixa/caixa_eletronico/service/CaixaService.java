package com.caixa.caixa_eletronico.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.caixa.caixa_eletronico.excpetion.SaldoInsuficienteException;
import com.caixa.caixa_eletronico.model.Conta;
import com.caixa.caixa_eletronico.model.Movimentacao;
import com.caixa.caixa_eletronico.repository.ContaRepository;
import com.caixa.caixa_eletronico.repository.MovimentacaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaixaService {

    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;


    // Buscar conta por ID
    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada para o ID: " + id));
    }

    // Autenticar por ID e senha
    public Conta autenticarPorId(Long id, String senha) {
        Conta conta = buscarPorId(id);
        if (!conta.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta para a conta ID: " + id);
        }
        return conta;
    }

    // Autenticar por titular e senha
    public Conta autenticar(String titular, String senha) {
        return contaRepository.findByTitularAndSenha(titular, senha)
            .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));
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
        if (conta.getSenha() == null || !conta.getSenha().matches("\\d{4,6}")) {
            throw new IllegalArgumentException("Senha deve conter entre 4 e 6 dígitos numéricos");
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

    // Realizar saque com autenticação
    @Transactional
    public Double sacar(Long id, Double valor, String senha) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Valor inválido para saque");
        }

        Conta conta = autenticarPorId(id, senha);

        if (conta.getSaldo() == null) {
            throw new RuntimeException("Saldo da conta está nulo");
        }

        if (conta.getSaldo() < valor) {
            throw new SaldoInsuficienteException(conta.getSaldo(), valor);
        }
        conta.setSaldo(conta.getSaldo() - valor);
        contaRepository.save(conta);

        // ✅ Inserir movimentação sem alterar a lógica principal
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta(conta);
        movimentacao.setValor(valor);
        movimentacao.setTipo("SAQUE");
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacaoRepository.save(movimentacao);

        return conta.getSaldo();
        }

    // Realizar depósito com autenticação
  // Realizar depósito com autenticação
@Transactional
public Double depositar(Long id, Double valor, String senha) {
    if (valor == null || valor <= 0) {
        throw new IllegalArgumentException("Valor inválido para depósito");
    }

    Conta conta = autenticarPorId(id, senha);

    if (conta.getSaldo() == null) {
        conta.setSaldo(0.0);
    }

    conta.setSaldo(conta.getSaldo() + valor);
    contaRepository.save(conta);

    // ✅ Inserir movimentação sem alterar a lógica principal
    Movimentacao movimentacao = new Movimentacao();
    movimentacao.setConta(conta);
    movimentacao.setValor(valor);
    movimentacao.setTipo("DEPOSITO");
    movimentacao.setDataHora(LocalDateTime.now());
    movimentacaoRepository.save(movimentacao);

    return conta.getSaldo();
}

    // Consultar saldo com autenticação
    public Conta consultarComSenha(Long id, String senha) {
        return autenticarPorId(id, senha);
    }

    public List<Conta> listarTodasComTotais() {
    List<Conta> contas = contaRepository.findAll();

    for (Conta conta : contas) {
        List<Movimentacao> movs = movimentacaoRepository.findByContaId(conta.getId());

        double totalDepositos = movs.stream()
            .filter(m -> "DEPOSITO".equals(m.getTipo()))
            .mapToDouble(Movimentacao::getValor)
            .sum();

        double totalSaques = movs.stream()
            .filter(m -> "SAQUE".equals(m.getTipo()))
            .mapToDouble(Movimentacao::getValor)
            .sum();

        conta.setTotalDepositos(totalDepositos);
        conta.setTotalSaques(totalSaques);
    }

    return contas;
}

}


