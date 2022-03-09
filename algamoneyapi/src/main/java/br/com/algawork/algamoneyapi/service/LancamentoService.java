package br.com.algawork.algamoneyapi.service;

import br.com.algawork.algamoneyapi.model.Lancamento;
import br.com.algawork.algamoneyapi.model.Pessoa;
import br.com.algawork.algamoneyapi.repository.LancamentoRepository;
import br.com.algawork.algamoneyapi.repository.PessoaRepository;
import br.com.algawork.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento){
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
        if(pessoa.isPresent() || pessoa.get().isInativo()){
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }
}
