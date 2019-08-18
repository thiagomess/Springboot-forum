package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.AtualizarTopicoForm;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {


    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    //    Passando na url o paramentro /topicos?nomeCurso=Spring Boot
    @GetMapping
    public List<TopicoDto> lista(String nomeCurso) {
        final List<Topico> topicos;
        if (nomeCurso == null) {
            topicos = topicoRepository.findAll();
        } else {
            topicos = topicoRepository.carregarCursoPeloNome(nomeCurso);
        }
        return TopicoDto.converter(topicos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@Valid @RequestBody TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        //Cria a URI
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        //Devolve o status 201, a URI e no body, devolve o objeto criado
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
        final Topico topico = topicoRepository.getOne(id); //Usando o getOne caso nao encontre, lançara exception e
        return new DetalhesDoTopicoDto(topico);            //será tratado no ErroDeValidacaoHandler
    }

    @PutMapping("{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarTopicoForm form) {
        final Optional<Topico> optional = topicoRepository.findById(id); //Outra forma de validar seria usando o findById
        if (optional.isPresent()) {                                     //Entao verifica se esta presente e nao precisaria doErroDeValidacaoHandler
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok().body(new TopicoDto(topico));
        } else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
