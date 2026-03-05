package com.example.cursoudemy.libraryapi.controller;

import com.example.cursoudemy.libraryapi.controller.dto.CadastroLivroDTO;
import com.example.cursoudemy.libraryapi.controller.dto.ErroResposta;
import com.example.cursoudemy.libraryapi.exceptions.RegistroDuplicadoException;
import com.example.cursoudemy.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Anotação que indica que esta classe é um controlador REST, capaz de receber requisições HTTP e retornar respostas JSON
@RequestMapping("/livros") // Define o caminho base para as rotas deste controlador, ou seja, todas as rotas definidas aqui começarão com "/livros"
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService; // Injeção de dependência do serviço de livros, que contém a lógica de negócios para manipular os dados dos livros

    @PostMapping // Anotação que indica que este metodo deve ser invocado para requisições HTTP POST, geralmente usadas para criar novos recursos
    public ResponseEntity<Object> salvar (@RequestBody @Valid CadastroLivroDTO dto) { // O metodo recebe um objeto CadastroLivroDTO no corpo da requisição, que deve ser validado (@Valid) de acordo com as anotações de validação definidas no DTO. O ResponseEntity<Object> é usado para retornar uma resposta HTTP personalizada, permitindo controlar o status e o corpo da resposta.
        try {
            // O metodo salvar do serviço de livros é chamado para tentar salvar o novo livro usando os dados fornecidos no DTO. Se o processo for bem-sucedido, o DTO é retornado na resposta com um status HTTP 200 OK.
            return ResponseEntity.ok(dto);
        } catch (RegistroDuplicadoException e) { // Se ocorrer uma exceção do tipo RegistroDuplicadoException, que indica que já existe um registro com os mesmos dados (por exemplo, um livro com o mesmo ISBN), o código dentro do bloco catch será executado.
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }
}
