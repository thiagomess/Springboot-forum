package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    //Classe responsavel por criar o token e pegar os atributos de segurança no application.properties
    @Value("${forum.jwt.expiration}")
    private String expiration;
    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {

        final Usuario logado = (Usuario) authentication.getPrincipal();
        final Date hoje = new Date();
        final Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("Api do Fórum da Alura") // Emissor do token;
                .setSubject(logado.getId().toString()) //Entidade à quem o token pertence, normalmente o ID do usuário;
                .setIssuedAt(hoje) //Timestamp de quando o token foi criado;
                .setExpiration(dataExpiracao) //Timestamp de quando o token irá expirar;
//                .setAudience("Destinatário do token, representa a aplicação que irá usá-lo") //Não obrigatório
                .signWith(SignatureAlgorithm.HS256, secret) //Senha e encriptação
                .compact();
    }
}
