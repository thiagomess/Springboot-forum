package br.com.alura.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil,Long> {

}
