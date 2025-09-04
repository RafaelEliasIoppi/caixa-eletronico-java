package com.caixa.caixa_eletronico.excpetion;



public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(Double saldoAtual, Double valorSolicitado) {
        super("Saldo insuficiente: disponível R$ " + saldoAtual + ", solicitado R$ " + valorSolicitado);
    }
}
