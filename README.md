# Projeto em Springboot 2.1.4 utilizando o MySql como banco de dados

## Certificados 

#### Spring Boot Parte 1: Construa uma API Rest
https://cursos.alura.com.br/certificate/5e32daba-67d9-459d-af4a-5a934e73de0d

#### Spring Boot Parte 2: Segurança da API, Cache e Monitoramento
https://cursos.alura.com.br/certificate/42a5d8bf-adcd-4037-b55c-986d7804d2d0



## Projeto publicado no HEROKU, com os seus endpoints funcionando:

### Para efetuar login na aplicação e obter um TOKEN

https://forum-api-alura.herokuapp.com/auth
## POST

 ```json
{
	"email":"aluno@email.com",
	"senha":"123456"
}
```

## GET
 https://forum-api-alura.herokuapp.com/topicos
 
##### Para as chamadas abaixo, é necessário ser enviado um token bearer no header, obtido apos efetuar o login
## POST

 ```json
 {
	"titulo":"Duvida",
	"mensagem":"Testando um post",
	"nomeCurso":"Spring Boot",
	"autor":"1"
}
 ```
## PUT
 ```json
{
	"titulo":"Duvida",
	"mensagem":"Testando um put"

}
```

## DELETE
 https://forum-api-alura.herokuapp.com/topicos/1