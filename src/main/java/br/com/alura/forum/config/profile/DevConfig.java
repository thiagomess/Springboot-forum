package br.com.alura.forum.config.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.alura.forum.DBService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	//recupera o valor da chave no application-dev.properties
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instatiateDatabase() {

		if(strategy.equalsIgnoreCase("create")){
			dbService.instantiateTestDatabase();
		}
		return true;
	}
}