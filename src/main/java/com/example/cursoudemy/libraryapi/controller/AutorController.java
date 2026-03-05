package com.example.cursoudemy.libraryapi.controller;

import com.example.cursoudemy.libraryapi.controller.dto.AutorDTO;
import com.example.cursoudemy.libraryapi.controller.dto.ErroResposta;
import com.example.cursoudemy.libraryapi.controller.mappers.AutorMapper;
import com.example.cursoudemy.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.example.cursoudemy.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.cursoudemy.libraryapi.models.Autor;
import com.example.cursoudemy.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Nodes.collect;

@RestController
@RequestMapping("/autores")
// Tera a URL base http://localhost:8080/autores
// Controlador REST para gerenciar recursos de Autor
@RequiredArgsConstructor // Gera um construtor com argumentos para todos os campos finais (final) com Lombok
public class AutorController {
    // Injeta o AutorService via construtor
    private final AutorService service;
    // Injeta o AutorMapper via construtor para converter entre Autor e AutorDTO
    private  final AutorMapper mapper;

    /*
    Não é necessario a presença de um construtor, pois o Lombok com a anotação @RequiredArgsConstructor
    gera automaticamente um construtor que recebe o AutorService como parâmetro e o atribui ao campo 'service'.
    public AutorController(AutorService service) {
        this.service = service;
    }
     */

    @PostMapping // Mapeia requisicoes HTTP POST para este metodo, pode ser posto uma URL mapeada dentro dos parenteses
    // Pode ser colocado tambem o @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto) { // Valid: Valida o DTO conforme as anotações de validação presentes na classe AutorDTO
        try {  // Tenta salvar o autor recebido no corpo da requisicao
            Autor autor = mapper.toEntity(dto); // Converte o DTO para a entidade Autor usando o mapper
            service.salvar(autor); // Chama o serviço para salvar o autor, que pode lançar uma exceção de registro duplicado

            // Construindo a URI do recurso criado para retornar no cabecalho da resposta
            // Isto serve para seguir o padrao REST de retornar a localizacao do recurso criado na resposta
            URI location = ServletUriComponentsBuilder // Classe utilitaria para construir URIs com base na requisicao atual
                    .fromCurrentRequest() // Pega a URI da requisicao atual (http://localhost:8080/autores)
                    .path("/{id}") // Adiciona o caminho /{id} a URI
                    .buildAndExpand(autor.getId()) // Substitui {id} pelo ID do autor criado
                    .toUri(); // Converte para o tipo URI

            return ResponseEntity.created(location).build(); // Retorna status 201 Created com o cabecalho Location
        } catch (RegistroDuplicadoException e) { // Captura excecao de registro duplicado
            var erroDTO = ErroResposta.conflito(e.getMessage()); // Cria o DTO de erro com status 409 Conflict
            return ResponseEntity.status(erroDTO.status()).body(erroDTO); // Retorna status 409 com o DTO de erro no corpo da resposta
        }

    }

    @GetMapping("/{id}") // Mapeia requisicoes HTTP GET para este metodo, com um parametro de caminho {id}
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id); // Converte a string do ID para UUID
//        Optional<Autor> autorOpcional = service.obterPorId(idAutor); // Busca o autor pelo ID usando o servico

        return service
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDto(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet( () -> ResponseEntity.notFound().build() );

        /**
         * Explicando o código acima:
         * Acima tem uma maneira muito mais enxuta e funcional de fazer a mesma coisa que o código comentado abaixo, usando as funcionalidades do Optional.
         * O metodo service.obterPorId(id) retorna um Optional<Autor>. O metodo map() é chamado no Optional, e recebe uma função lambda que recebe
         * o autor encontrado e retorna um ResponseEntity<AutorDTO> com status 200 OK e o DTO no corpo da resposta. Se o Optional estiver vazio (ou seja,
         * se o autor nao for encontrado), o metodo orElseGet() é chamado, e retorna um ResponseEntity com status 404 Not Found.
         */

//        if (autorOpcional.isPresent()) { // Verifica se o autor foi encontrado
//            Autor autor = autorOpcional.get(); // Pega o autor do Optional
//            AutorDTO dto = mapper.toDto(autor); // Converte a entidade Autor para AutorDTO usando o mapper
//            return  ResponseEntity.ok(dto); // Retorna status 200 OK com o DTO no corpo da resposta
//        }
//
//        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}") // Mapeia requisicoes HTTP DELETE para este metodo, com um parametro de caminho {id}
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        try {
            var idAutor = UUID.fromString(id); // Converte a string do ID para UUID
            Optional<Autor> autorOpcional = service.obterPorId(idAutor); // Busca o autor pelo ID usando o servico
            if (autorOpcional.isEmpty()) {
                return ResponseEntity.notFound().build(); // Retorna status 404 Not Found se o autor nao for encontrado
            }
            service.deletar(autorOpcional.get()); // Deleta o autor encontrado
            return ResponseEntity.noContent().build(); // Retorna status 204 No Content indicando que a exclusao foi bem sucedida
        } catch (OperacaoNaoPermitidaException e) { // Captura excecao de operacao nao permitida
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage()); // Cria o DTO de erro com status 400 Bad Request
            return ResponseEntity.status(erroResposta.status()).body(erroResposta); // Retorna status 400 com o DTO de erro no corpo da resposta
        }
    }

    @GetMapping // Mapeia requisições HTTP GET para este endpoint
    public ResponseEntity<List<AutorDTO>> pesquisar( // Define retorno com status HTTP e lista de DTOs
                  @RequestParam(value = "nome", required = false) String nome, // Parametro opcional 'nome' vindo da query string // filtro por nome (pode ser null)
                  @RequestParam(value = "nacionalidade", required = false) String nacionalidade) { // Parametro opcional 'nacionalidade' // filtro por nacionalidade (pode ser null)
        List<Autor> lista = service.pesquisaByExample(nome, nacionalidade); // Chama o service para obter lista de entidades conforme filtros
        List<AutorDTO> listaDTO = lista // Inicia conversão de entidades para DTOs
                .stream() // Cria stream para processar a coleção de forma funcional // permite mapear cada elemento
                .map(mapper::toDto) // Converte cada Autor para AutorDTO usando o mapper // referencia ao metodo toDto do mapper // os : sao usados para referenciar um metodo existente, evitando a necessidade de escrever uma lambda completa como autor -> mapper.toDto(autor)
                .collect(Collectors.toList()); // Coleta os DTOs em uma List<AutorDTO>
        return ResponseEntity.ok(listaDTO); // Retorna 200 OK com a lista de DTOs no corpo da resposta
    }

    @PutMapping("/{id}") // Mapeia requisicoes HTTP PUT para este metodo, com um parametro de caminho {id}
    public ResponseEntity<Object> atualizar(
            @PathVariable("id") String id,
            @RequestBody @Valid AutorDTO dto) { // Recebe o ID do autor a ser atualizado e os dados atualizados no corpo da requisicao, validando o DTO conforme as anotações de validação presentes na classe AutorDTO
        try {
            var idAutor = UUID.fromString(id); // Converte a string do ID para UUID
            Optional<Autor> autorOpcional = service.obterPorId(idAutor); // Busca o autor pelo ID usando o servico

            if (autorOpcional.isEmpty()) {  // Verifica se o autor foi encontrado
                return ResponseEntity.notFound().build(); // Retorna status 404 Not Found se o autor nao for encontrado
            }

            var autor = autorOpcional.get();  // Pega o autor do Optional
            autor.setNome(dto.nome());  // Atualiza o nome do autor
            autor.setDataNascimento(dto.dataNascimento());  // Atualiza a data de nascimento do autor
            autor.setNacionalidade(dto.nacionalidade());  // Atualiza a nacionalidade do autor
            service.atualizar(autor); // Atualiza o autor encontrado

            /**
             * Neste bloco de código acima não é necessário a utilização do MapStruct pois
             * esta sendo feita a atualização de apenas alguns campos específicos da entidade,
             * assim o mapper não é capaz de lidar com a atualização parcial, já que o metodo
             * toEntity do mapper espera um DTO completo para criar uma nova entidade, e não para
             * atualizar uma entidade existente. O MapStruct é mais adequado para conversões completas
             * entre DTOs e entidades, onde todos os campos são mapeados. Para atualizações parciais,
             * é mais simples e direto atualizar os campos manualmente, como mostrado no código acima.
             */

            return ResponseEntity.noContent().build(); // Retorna status 204 No Content indicando que a atualizacao foi bem sucedida
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
