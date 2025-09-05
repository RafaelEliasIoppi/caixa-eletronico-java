package com.caixa.caixa_eletronico.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.caixa.caixa_eletronico.model.Conta;
import com.caixa.caixa_eletronico.service.CaixaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CaixaViewController {

    private final CaixaService caixaService;

    // 🏠 Página inicial
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 🔍 Consultar saldo
   @GetMapping("/consultar")
public String consultar(@RequestParam Long consultaId, Model model) {
    try {
        Conta conta = caixaService.buscarPorId(consultaId);
        model.addAttribute("conta", conta);
        model.addAttribute("consultaId", consultaId);
    } catch (RuntimeException e) {
        model.addAttribute("erroConsulta", "Conta não encontrada");
    }
    return "index";
}

// 💰 Realizar depósito
@PostMapping("/depositar")
public String depositar(@RequestParam Long depositoId, @RequestParam Double valor, Model model) {
    try {
        Double novoSaldo = caixaService.depositar(depositoId, valor);
        model.addAttribute("novoSaldo", novoSaldo);
        model.addAttribute("depositoId", depositoId);
    } catch (RuntimeException e) {
        System.out.println("Erro ao depositar: " + e.getMessage());
        model.addAttribute("erroDeposito", e.getMessage());
    }
    return "index";
}

// 💸 Realizar saque
@PostMapping("/sacar")
public String sacar(@RequestParam Long saqueId, @RequestParam Double valor, Model model) {
    try {
        Double novoSaldo = caixaService.sacar(saqueId, valor);
        model.addAttribute("novoSaldo", novoSaldo);
        model.addAttribute("saqueId", saqueId);
    } catch (RuntimeException e) {
        System.out.println("Erro ao sacar: " + e.getMessage());
        model.addAttribute("erroSaque", e.getMessage());
    }
    return "index";
}

}
