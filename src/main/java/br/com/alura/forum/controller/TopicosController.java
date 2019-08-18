package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicosController {


    @Autowired
    private TopicoRepository topicoRepository;

//    Passando na url o paramentro /topicos?nomeCurso=Spring Boot
    @RequestMapping(value = "/topicos", method = RequestMethod.GET)
    public List<TopicoDto> lista(String nomeCurso) {
        final List<Topico> topicos;
        if (nomeCurso == null) {
            topicos = topicoRepository.findAll();
        } else {
            topicos = topicoRepository.carregarCursoPeloNome(nomeCurso);
        }
        return TopicoDto.converter(topicos);
    }
}
