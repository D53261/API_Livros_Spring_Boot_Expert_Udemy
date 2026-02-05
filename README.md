## Parte do Curso: Spring Boot Expert: JPA, REST, JWT, OAuth2 com Docker e AWS
Aqui estão apresentados os códigos usados durante a realização do curso da plataforma Udemy com nome mencionado acerca de uma sessão: Introdução ao Bean Validation e Melhorias para API.

<hr></hr>

## Bean Validation e finalização da API de Autor

No projeto que é a continuação das sessões anteriores, foi implementado algumas melhorias de validação usando o Starter Validation do Spring Boot, além de finalizar e melhorar alguns conceitos e funções já antes utilizadas.

## 1. Validação de campos
Utilizou-se Anotations especificas para verificar a integridade dos dados que eram postos pelo cliente na DTO do autor, com verificações como:
* @NotBlank: para impedir que campos de String estejam vazios
* @NotNull: para impedir que qualquer outro tipo de campo esteja vazio
* @Size: limita o tamanho mínimo e máximo que um campo pode ter
* @Past: impede que campos de Data tenham valores futuros da data atual (possuindo sua contraparte tambem, o @Future)

## 2. Exception Handlers
Cada uma destas validações citadas podem ser implementadas nos endpoints do Controller com a anotação @Valid em campos de POST e PUT neste caso, mas também foi implementado um Handler, que é uma exceção lançada globalmente para todos os controladores REST seguindo os critérios das verificações comentadas.

Por isso todas as Anotations acima que foram postas no DTO possuem mensagens que serão impressas caso tal validação falhe graças ao Handler Exception. 

## 3. Aprofundando em DTO's
As DTO's se apresentaram como um ponto mais importante ainda, sendo a barreira que só é aberta ao banco de dados quando os dados que lhe foram colocados forem validados com sucesso, caso contrario a Entity principal nunca verá os dados e a operação não ocorrerá.

## 4. Query By Examples
Foi mostrada uma maneira extremamente prática e completa de fazer pesquisas com a API, usando os Examples e suas pesquisas dinâmicas, podendo fazer pesquisas pelos campos que quiser, e encontrando a palavra mesmo sem a necessidade de digitar inteiramente, graças a funções internas como o matcher que limita inserções e estabelece os padrões ditos.

<hr></hr> 
Nesta parte final da API de Autores, foi posta a vista a questão final de verificações simples mas muito importantes, adaptando muito código com funções bem elaboradas e preparando dados para chegarem ao banco e ao cliente sem complicações.

## Como Executar
- **Pela IDE (IntelliJ)**: Execute a classe principal `Application.java` anotada com `@SpringBootApplication`.
- **Pela Linha de Comando (Windows)**:
    - Construir: `mvn clean package`
    - Rodar: `mvn spring-boot:run`
- **Executar Testes**: `mvn test`

## Configuração com Docker
- **Criar Rede Docker**: `docker network create library-network`
- **Rodar PostgreSQL**: `docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library --network library-network postgres:16.3`
- **Rodar pgAdmin**: `docker run --name pgadmin4 -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin dpage/pgadmin4:8.9`

## Visão Geral dos Conceitos e Termos Importantes de Bean Validation e Querys
- **Examples**: tipo de classe muito usado em pesquisas para ser uma alternativa flexível e sem dados definidos
- **@RestControllerAdvice e Handler**: Uma classe que verifica e cuida em caso de exceções especificas de todas as outras classes REST é a com a Anotation @RestControllerAdvice, e esta usa os Handlers, funções que desempenham o papel de exceções gerais por todo controlador (onde possui a Anotation @Valid)

## Caso encontre códigos confusos neste projeto
- Revise os outros projetos postados em meu GitHub relacionados ao curso Spring Boot Expert, provavelmente a explicação mais detalhada estará lá.
- Consulte a documentação oficial do Spring Boot e Spring Data JPA.

## Dicas para Estudo e Experimentação
- Ative `spring.jpa.show-sql=true` e observe SQLs gerados.
- Faça testes com diferentes exceções ou crie suas prórpias verificações com Validator.

## Recursos Adicionais
- [Documentação do Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação do Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL](https://www.postgresql.org/)

## Feito por:
### Danilo Ribeiro
### Linkedin: https://www.linkedin.com/in/danilo-ribeiro-catroli-da-silva/
### Email: danilo051007@gmail.com
