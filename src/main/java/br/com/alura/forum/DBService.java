package br.com.alura.forum;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Perfil;
import br.com.alura.forum.modelo.StatusTopico;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.PerfilRepository;
import br.com.alura.forum.repository.TopicoRepository;
import br.com.alura.forum.repository.UsuarioRepository;

@Service
public class DBService {
	
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PerfilRepository perfilRepository;

	
	public void instantiateTestDatabase() {
		
		Perfil perfil = new Perfil();
		perfil.setId(null);
		perfil.setNome("ADMIN");
		
		perfilRepository.save(perfil);
		
		Usuario usuario = new Usuario();
		usuario.setNome("Aluno");
		usuario.setEmail("aluno@email.com");
		usuario.setSenha("$2a$10$q.PbGhpXGWh/vj/IXad/z.pzYYiXhqdB5tabHYr8/.y1akGTcip92");
		usuario.setPerfis(Arrays.asList(perfil));
		
		usuarioRepository.save(usuario);
		
		Curso curso1 = new Curso("Spring Boot", "Programação");
		Curso curso2 = new Curso("HTML 5", "Front-end");
		
		cursoRepository.saveAll(Arrays.asList(curso1, curso2));
		

		Topico top1 = new Topico(null, "Dúvida1", "Erro ao criar projeto", LocalDateTime.of(2017, 9, 30, 10, 32), StatusTopico.NAO_RESPONDIDO, usuario, curso1);
		Topico top2 = new Topico(null, "Dúvida2", "Projeto não compila", LocalDateTime.of(2018, 1, 12, 11, 40), StatusTopico.NAO_RESPONDIDO, usuario, curso1);
		Topico top3 = new Topico(null, "Dúvida3", "Tag HTML", LocalDateTime.of(2019, 11, 12, 17, 32), StatusTopico.NAO_RESPONDIDO, usuario, curso2);
		Topico top4 = new Topico(null, "Dúvida4", "Como usar o H2", LocalDateTime.of(2019, 1, 03, 9, 32), StatusTopico.NAO_RESPONDIDO, usuario, curso2);
		
		topicoRepository.saveAll(Arrays.asList(top1, top2, top3, top4));

	}

}
