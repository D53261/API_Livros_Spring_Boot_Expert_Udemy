package com.example.cursoudemy.libraryapi.service;

import com.example.cursoudemy.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service // Anotação que indica que esta classe é um serviço, parte da camada de negócios da aplicação
@RequiredArgsConstructor // Lombok: gera um construtor com argumentos para todos os campos finais (final) da classe, facilitando a injeção de dependências
public class LivroService {
    private final LivroRepository livroRepository; // Injeção de dependência do repositório de livros, que é responsável por acessar os dados dos livros no banco de dados
}
