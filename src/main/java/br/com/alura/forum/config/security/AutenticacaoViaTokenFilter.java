package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* Filter que será responsável por interceptar o token que vem no header da requisição
* Está classe é adicionada no metodo configure do SecurityConfigurations, para
* informar que ela será executada antes
* Está classe extends OncePerRequestFilter do Spring
* */

@Component
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;

//    Como esta classe nao pode utilizar o @Autowired, a classe que for da new nela, sera obrigada a passar via construtor
    public AutenticacaoViaTokenFilter(TokenService tokenService,UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperaToken(request);

        boolean valido = tokenService.isTokenValido(token);
        if (valido){
            autenticarUsuario(token);
        }
        filterChain.doFilter(request, response);
    }

    private void autenticarUsuario(String token) {
        final long idUsuario = tokenService.getIdUsuario(token); //Obtem o id do Usuario
        final Usuario usuario = usuarioRepository.findById(idUsuario).get(); //Busca o usuario no banco
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperaToken(HttpServletRequest request) {
        final String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7, token.length());
    }
}
