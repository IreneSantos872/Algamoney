package br.com.algawork.algamoneyapi.resource;

import br.com.algawork.algamoneyapi.event.RecursoCriadoEvent;
import br.com.algawork.algamoneyapi.exceptionhandler.AlgamoneyExceptionHandler;
import br.com.algawork.algamoneyapi.model.Lancamento;
import br.com.algawork.algamoneyapi.repository.LancamentoRepository;
import br.com.algawork.algamoneyapi.repository.filter.LancamentoFilter;
import br.com.algawork.algamoneyapi.service.LancamentoService;
import br.com.algawork.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.algawork.algamoneyapi.service.LancamentoService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private MessageSource messageSource;

//    @GetMapping
//    public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter){
//        return lancamentoRepository.filtrar(lancamentoFilter);
//    }

    @GetMapping
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable){
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo){
        Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
        return lancamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);

    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo){
        lancamentoRepository.deleteById(codigo);
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<AlgamoneyExceptionHandler.Erro> erros = Collections.singletonList(new AlgamoneyExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }
}
