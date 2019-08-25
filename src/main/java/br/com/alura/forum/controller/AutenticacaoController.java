package br.com.alura.forum.controller;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/*Como a autenticação, será feita via statless
* Precisamos criar um controller que será responsável por gerar o token
* e devolver via JSON
*/
@RestController
@RequestMapping("auth")
public class AutenticacaoController {

    // Classe que será responsavel por validar o usuario no banco, usando a AutenticacaoService que pega o usuario no banco
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;  //Classe que será responsavel por gerar o token

    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid LoginForm form) {

        UsernamePasswordAuthenticationToken dadosLogin = form.converter(); //convertemos os dados do form em dadosLogin

        try {
            final Authentication authentication = authManager.authenticate(dadosLogin); //Se o usuario existir, gerará o token

            String token = tokenService.gerarToken(authentication); //Gera o token no Service
            return ResponseEntity.ok(new TokenDto(token, "Bearer")); //Devolve o Token e o tipo de autenticação "Bearer"
        } catch (AuthenticationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
