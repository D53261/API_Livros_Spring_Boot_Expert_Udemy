package com.example.cursoudemy.libraryapi.controller.dto;

import com.example.cursoudemy.libraryapi.models.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResultadoPesquisaLivroDTO ( // Define um record para representar os dados retornados em uma pesquisa de livros, com os seguintes campos:
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        AutorDTO autor
    ) {

}
