package com.example.cursoudemy.libraryapi.controller.dto;

import com.example.cursoudemy.libraryapi.models.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO ( // Define um record para representar os dados necessários para cadastrar um livro, com os seguintes campos:
        @ISBN
        @NotBlank(message = "campo obrigatório")
        String isbn,
        @NotBlank(message = "campo obrigatório")
        String titulo,
        @NotNull(message = "campo obrigatório")
        @Past(message = "a data de publicação deve ser no passado")
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        @NotNull(message = "campo obrigatório")
        UUID idAutor
    ) {

}
