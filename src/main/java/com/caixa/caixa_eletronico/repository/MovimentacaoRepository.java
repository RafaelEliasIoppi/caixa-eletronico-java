package com.caixa.caixa_eletronico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.caixa.caixa_eletronico.model.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByContaId(Long contaId);
}