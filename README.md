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

Usuário com role ADMIN

 ```json
{
	"email":"admin@email.com",
	"senha":"123456"
}
```

Usuário com role comum:

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

O usuário com role comum, tem permisão apenas para alterar um tópico que ele criou, já o usuario com role admin pode alterar o seu ou dos demais usuários

 ```json
{
	"titulo":"Duvida",
	"mensagem":"Testando um put"

}
```

## DELETE
 https://forum-api-alura.herokuapp.com/topicos/1

------------------------------------------------------

#Rodando no Docker

mvn clean install

docker build -t alura/forum .

docker run -p 8080:8080 alura/forum

