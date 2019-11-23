package br.com.alura.forum.controller.form;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.UsuarioRepository;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TopicoForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String titulo;
    @NotNull @NotEmpty @Length(min = 10)
    private String mensagem;
    @NotNull @NotEmpty
    private String nomeCurso;
    @NotNull
    private Long autor;
    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
    
    public Long getAutor() {
		return autor;
	}

	public void setAutor(Long autor) {
		this.autor = autor;
	}

	public Topico converter(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        final Curso curso = cursoRepository.findByNome(nomeCurso);
        final Usuario usuario = usuarioRepository.findById(autor).orElse(null);
        return new Topico(titulo, mensagem, curso, usuario);
    }
}
