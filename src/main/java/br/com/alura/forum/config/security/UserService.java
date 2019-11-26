package br.com.alura.forum.config.security;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.alura.forum.modelo.Usuario;

/**
 * Classe que tem a responsabilidade de pegar e devolver o usuario logado
 */
public class UserService {

	public static Usuario authenticated() {
		try {
			return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
