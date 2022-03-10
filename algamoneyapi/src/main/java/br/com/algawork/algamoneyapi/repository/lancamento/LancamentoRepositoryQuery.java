package br.com.algawork.algamoneyapi.repository.lancamento;

import br.com.algawork.algamoneyapi.model.Lancamento;
import br.com.algawork.algamoneyapi.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LancamentoRepositoryQuery  {

    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

}
