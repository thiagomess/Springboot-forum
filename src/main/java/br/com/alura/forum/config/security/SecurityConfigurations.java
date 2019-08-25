package br.com.alura.forum.config.security;

import br.com.alura.forum.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService; //Service que será responsavel por validar o usuario

    @Override
    @Bean //Cria o bean, para o SPring conseguir injetar no AutenticacaoController
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // Configurações de autenticação para WEB(não API)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());

    }

    //Configurações de Autorização (URL, perfis de acesso)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .anyRequest().authenticated()  //Qual quer outra requisição deve ser autenticada
//                .and().formLogin(); // invoca o form padrao do Spring para login //Como nao usaremos Session, comentado
                .and().csrf().disable()//Como fará autenticação via token, pode desabilitar
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //não criará sessao
    }

    // Configurações de recursos estaticos (css, js, imagens...)
    @Override
    public void configure(WebSecurity web) throws Exception {
    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }
}
