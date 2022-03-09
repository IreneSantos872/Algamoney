package br.com.algawork.algamoneyapi.repository;

import br.com.algawork.algamoneyapi.model.Lancamento;
import br.com.algawork.algamoneyapi.repository.lancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
}
