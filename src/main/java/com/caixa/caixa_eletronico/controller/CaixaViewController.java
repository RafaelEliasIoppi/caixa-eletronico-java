package com.caixa.caixa_eletronico.controller;



import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @PostMapping("/login")
public String autenticar(@RequestParam String titular,
                         @RequestParam String senha,
                         Model model) {
    try {
        Conta conta = caixaService.autenticar(titular, senha);
        model.addAttribute("contaAutenticada", conta);
    } catch (RuntimeException e) {
        model.addAttribute("erroLogin", "Titular ou senha inválidos");
    }
    return "index";
}

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro() {
        return "cadastro"; // Isso renderiza cadastro.html
}

    @GetMapping("/listar")
    public String listarContas(Model model) {
        List<Conta> contas = caixaService.listarTodas();
        model.addAttribute("todasContas", contas);
        return "index";
    }

    @PostMapping("/criar")
public String criarConta(@RequestParam String titular,
                         @RequestParam Double valor,
                         @RequestParam String senha,
                         Model model) {
    try {
        if (valor == null || valor < 0) {
            throw new IllegalArgumentException("Valor inicial inválido");
        }

        if (!senha.matches("\\d{4,6}")) {
            throw new IllegalArgumentException("A senha deve conter entre 4 e 6 dígitos numéricos.");
        }

        Conta novaConta = new Conta();
        novaConta.setTitular(titular);
        novaConta.setSaldo(valor);
        novaConta.setSenha(senha);

        Conta contaCriada = caixaService.criarConta(novaConta);
        model.addAttribute("contaCriada", contaCriada);
    } catch (IllegalArgumentException e) {
        model.addAttribute("erroCriacao", e.getMessage());
    } catch (Exception e) {
        model.addAttribute("erroCriacao", "Erro ao criar conta");
    }
    return "cadastro";
}


// 🔍 Consultar saldo via POST
@PostMapping("/consultar")
public String consultar(@RequestParam Long consultaId,
                        @RequestParam String senha,
                        Model model) {
    try {
        Conta conta = caixaService.consultarComSenha(consultaId, senha);
        model.addAttribute("conta", conta);
        model.addAttribute("consultaId", consultaId);
    } catch (RuntimeException e) {
        model.addAttribute("erroConsulta", "Conta não encontrada ou senha incorreta");
    }
    return "index";
}


   // 💰 Realizar depósito
   @PostMapping("/depositar")
public String depositar(@RequestParam Long depositoId,
                        @RequestParam Double valor,
                        @RequestParam String senha,
                        Model model) {
    try {
        Double novoSaldo = caixaService.depositar(depositoId, valor, senha);
        model.addAttribute("novoSaldo", novoSaldo);
        model.addAttribute("depositoId", depositoId);
    } catch (RuntimeException e) {
        model.addAttribute("erroDeposito", e.getMessage());
    }
    return "index";
}

    // 💸 Realizar saque
    @PostMapping("/sacar")
public String sacar(@RequestParam Long saqueId,
                    @RequestParam Double valor,
                    @RequestParam String senha,
                    Model model) {
    try {
        Double novoSaldo = caixaService.sacar(saqueId, valor, senha);
        model.addAttribute("novoSaldo", novoSaldo);
        model.addAttribute("saqueId", saqueId);
    } catch (RuntimeException e) {
        System.out.println("Erro ao sacar: " + e.getMessage());
        model.addAttribute("erroSaque", e.getMessage());
    }
    return "index";
}

    @PostMapping("/excluir")
public String excluirConta(@RequestParam Long id,
                           @RequestParam String senha,
                           RedirectAttributes redirectAttributes) {
    try {
        Conta conta = caixaService.autenticarPorId(id, senha);
        caixaService.deletarPorId(conta.getId());
        redirectAttributes.addFlashAttribute("mensagem", "Conta excluída com sucesso!");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("erro", "Erro ao excluir conta: " + e.getMessage());
    }
    return "redirect:/listar";
}



}
