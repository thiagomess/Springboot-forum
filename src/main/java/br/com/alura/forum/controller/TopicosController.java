package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.config.security.UserService;
import br.com.alura.forum.config.validacao.AuthorizationException;
import br.com.alura.forum.controller.dto.AtualizarTopicoForm;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	// Passando na url o paramentro /topicos?nomeCurso=Spring Boot
	// Nova url Fica: http://localhost:8080/topicos?pagina=0&qtd=30&ordenacao=id
	// URL :http://localhost:8080/topicos?page=1&size=9&sort=mensagem,asc
	@GetMapping
	@Cacheable(value = "listaDeTopicos") // funciona como um identificador único do cache.
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(direction = Sort.Direction.ASC) Pageable paginacao
	/*
	 * @RequestParam int pagina, @RequestParam int qtd, @RequestParam String
	 * ordenacao
	 */) {

		// Cria um atributo para paginação, pertence ao import
		// org.springframework.data.domain.Pageable;
		// Para nao usar esta linha abaixo, usamos o Pagable direto no metodo
//        Pageable paginacao = PageRequest.of(pagina, qtd, Sort.Direction.ASC, ordenacao);
		
		final Page<Topico> topicos;
		if (nomeCurso == null) {
			topicos = topicoRepository.findAll(paginacao);
		} else {
			topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
		}
		return TopicoDto.converter(topicos);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@Valid @RequestBody TopicoForm topicoForm,
			UriComponentsBuilder uriBuilder) {
		Usuario user = UserService.authenticated(); // Pega o usuario, pelo token
		Topico topico = topicoForm.converter(cursoRepository, user);
		topicoRepository.save(topico);

		// Cria a URI
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		// Devolve o status 201, a URI e no body, devolve o objeto criado
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
		final Topico topico = topicoRepository.getOne(id); // Usando o getOne caso nao encontre, lançara exception e
		return new DetalhesDoTopicoDto(topico); // será tratado no ErroDeValidacaoHandler
	}

	@PutMapping("{id}")
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "listaDeTopicos", allEntries = true) // Remove o Cache que colocamos no GET
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarTopicoForm form) {
		final Optional<Topico> optional = topicoRepository.findById(id); // Outra forma de validar seria usando o
																			// findById
		if (optional.isPresent()) { // Entao verifica se esta presente e nao precisaria doErroDeValidacaoHandler
			Topico topico = form.atualizar(id, topicoRepository);
			// Pega o usuario logado, verifica se o usuario que esta consultando, é o
			// usuario logado ou tem perfil ADMIN
			Usuario user = UserService.authenticated();
			if (user == null || !user.hasRole("ADMIN") && !topico.getAutor().getId().equals(user.getId())) {
				throw new AuthorizationException("Acesso negado");
			}
			return ResponseEntity.ok().body(new TopicoDto(topico));
		} else
			return ResponseEntity.notFound().build();
	}

	@DeleteMapping("{id}")
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "listaDeTopicos", allEntries = true) // Remove o Cache que colocamos no GET
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		topicoRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
