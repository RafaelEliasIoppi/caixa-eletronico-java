package com.caixa.caixa_eletronico.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caixa.caixa_eletronico.model.Conta;
import com.caixa.caixa_eletronico.service.CaixaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaService caixaService;

    // 🔍 Buscar conta por ID
    @GetMapping("/conta/{id}")
    public ResponseEntity<Conta> buscarConta(@PathVariable Long id) {
        Conta conta = caixaService.buscarPorId(id);
        return ResponseEntity.ok(conta);
    }

    // 📋 Listar todas as contas
    @GetMapping("/contas")
    public ResponseEntity<List<Conta>> listarContas() {
        return ResponseEntity.ok(caixaService.listarTodas());
    }

    // 💾 Criar ou atualizar conta
    @PostMapping("/conta")
    public ResponseEntity<Conta> salvarConta(@RequestBody Conta conta) {
        Conta novaConta = caixaService.salvarConta(conta);
        return ResponseEntity.ok(novaConta);
    }

    // 🗑️ Deletar conta
    @DeleteMapping("/conta/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        caixaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // 💸 Realizar saque
    @PostMapping("/conta/{id}/saque")
    public ResponseEntity<Double> sacar(@PathVariable Long id, @RequestParam Double valor) {
        Double novoSaldo = caixaService.sacar(id, valor);
        return ResponseEntity.ok(novoSaldo);
    }

    // 💰 Realizar depósito
    @PostMapping("/conta/{id}/deposito")
    public ResponseEntity<Double> depositar(@PathVariable Long id, @RequestParam Double valor) {
        Double novoSaldo = caixaService.depositar(id, valor);
        return ResponseEntity.ok(novoSaldo);
    }
}
