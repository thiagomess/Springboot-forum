package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

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

    public boolean isTokenValido(String token) {
        try {
            //Senão ocorre exception, é pq o token é valido
            getInformacoesDoToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //retorna o username do token jwt
    public long getIdUsuario(String token) {
        return Long.parseLong(getClaimFromToken(token, Claims::getSubject));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getInformacoesDoToken(token);
        return claimsResolver.apply(claims);
    }

    //para retornar qualquer informação do token nos iremos precisar da secret key
    private Claims getInformacoesDoToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

//    //retorna expiration date do token jwt
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    //verifica se o token está expirado
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
}
