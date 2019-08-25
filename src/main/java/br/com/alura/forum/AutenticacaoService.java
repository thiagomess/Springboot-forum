package br.com.alura.forum;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/*
* Essa classe deve ser um @Service, e implementar o UserDetailsService,
*que tem o metodo loadUserByUserName, que ira n banco de dados
* verificar se o usuario existe
*/
@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
        if (usuario.isPresent()){
            return usuario.get();
        }
        throw new UsernameNotFoundException("Usuario não encontrado");
    }
}
