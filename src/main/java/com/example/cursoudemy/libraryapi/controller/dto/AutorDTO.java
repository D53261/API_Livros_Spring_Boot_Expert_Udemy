package com.example.cursoudemy.libraryapi.controller.dto;

import com.example.cursoudemy.libraryapi.models.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

// DTO - Data Transfer Object
// Objeto de transferencia de dados
// Usado para transferir dados entre camadas da aplicacao, evitando expor diretamente as entidades do dominio
public record AutorDTO(
        UUID id,
        @NotBlank( // Validacao: o campo nao pode ser nulo ou vazio, sendo especifico para Strings
                message = "O nome do autor nao pode ser vazio" // Mensagem de erro caso a validacao falhe
        )
        @Size(min = 1, max = 255, // Validacao: o campo deve ter entre 1 e 255 caracteres)
                message = "O nome do autor deve conter entre 1 e 255 caracteres" // Mensagem de erro caso a validacao falhe
        )
        String nome,
        @NotNull ( // Validacao: o campo nao pode ser nulo
                message = "A data de nascimento do autor nao pode ser nula" // Mensagem de erro caso a validacao falhe
        )
        @Past( // Validacao: a data deve ser no passado)
                message = "A data de nascimento do autor deve ser uma data passada" // Mensagem de erro caso a validacao falhe
        )
        LocalDate dataNascimento,
        @NotBlank( // Validacao: o campo nao pode ser nulo ou vazio, sendo especifico para Strings
                message = "A nacionalidade do autor nao pode ser vazia" // Mensagem de erro caso a validacao falhe
        )
        @Size(min = 2, max = 50, // Validacao: o campo deve ter entre 2 e 50 caracteres)
                message = "A nacionalidade do autor deve conter entre 2 e 50 caracteres" // Mensagem de erro caso a validacao falhe
        )
        String nacionalidade) {
    // Mapeia o DTO para a entidade Autor
    public Autor mapearParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}

/**
 * Uma outra vantagem do uso das DTO's é justamente a preparação e
 * Limpeza dos dados antes de enviá-los para a camada de serviço ou para o banco de dados.
 * Isso ajuda a garantir que apenas os dados necessários e válidos sejam processados,
 * reduzindo o risco de erros e melhorando a eficiência do sistema.
 */
