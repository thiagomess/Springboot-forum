package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/* Um pouco mais sobre o poder do JpaRepository
* https://edermfl.wordpress.com/2017/04/27/aprendendo-spring-5-jparepository/
* https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-graph
*
*/
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    //Usando o padrão do Spring FindBy? ele cria a propria query, no caso de relacionamento, primeiro se da o nome
    // do relacionamento concaternado com o nome do atributo ou usando o "_" para separar
    Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);

    //Caso nao queira usar o padrão do findBy, pode se usar o @Query e escrever a query
    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
    List<Topico> carregarCursoPeloNome(@Param("nomeCurso") String nomeCurso);
}
