package com.example.cursoudemy.libraryapi.service;

import com.example.cursoudemy.libraryapi.exceptions.OperacaoNaoPermitidaException;
import com.example.cursoudemy.libraryapi.models.Autor;
import com.example.cursoudemy.libraryapi.repository.AutorRepository;
import com.example.cursoudemy.libraryapi.repository.LivroRepository;
import com.example.cursoudemy.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Servico para gerenciar operacoes relacionadas a Autor
@Service
@RequiredArgsConstructor // Gera um construtor com argumentos para todos os campos finais (final) com Lombok
public class AutorService {
    // Injeta o AutorRepository via construtor
    private final AutorRepository repository;

    private final AutorValidator validator;

    private final LivroRepository livroRepository;

    // Salva um autor no banco de dados
    public Autor salvar(Autor autor) {
        validator.validar(autor);  // Valida o autor antes de salvar
        // Usa o repositório para salvar o autor e retorna a entidade salva
        return repository.save(autor);
    }

    // Atualizar um autor já existente no banco de dados
    public void atualizar(Autor autor) {
        if (autor.getId() == null) { // Verifica se o ID do autor é nulo
            throw new IllegalArgumentException("O ID do autor não pode ser nulo para atualização.");  // Lança uma exceção se for nulo
        }
        validator.validar(autor);  // Valida o autor antes de atualizar
        // Usa o repositório para atualizar o autor
        repository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id) {
        return repository.findById(id);
    }  // Obtém um autor pelo ID

    public void deletar(Autor autor) {
        if (possuiLivro(autor)) { // Verifica se o autor possui livros cadastrados
            throw new OperacaoNaoPermitidaException("Não é permitido exluir um Autor que possui livros cadastrados!");
        }
        repository.delete(autor);
    }  // Deleta um autor do banco de dados

    public List<Autor> pesquisa(String nome, String nacionalidade) {  // Pesquisa autores por nome e/ou nacionalidade
        if (nome != null && nacionalidade != null) {  // Se ambos os parâmetros forem fornecidos
            return repository.findByNomeAndNacionalidade(nome, nacionalidade);  // Pesquisa por ambos
        }

        if (nome != null) {  // Se apenas o nome for fornecido
            return repository.findByNome(nome);  // Pesquisa por nome
        }

        if (nacionalidade != null) {  // Se apenas a nacionalidade for fornecida
            return repository.findByNacionalidade(nacionalidade);  // Pesquisa por nacionalidade
        }

        return repository.findAll();  // Se nenhum parâmetro for fornecido, retorna todos os autores
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade) { // Pesquisa autores usando Example para busca dinâmica
        var autor = new Autor(); // Cria uma nova instância de Autor para servir como exemplo de busca
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher // Constrói o ExampleMatcher para personalizar a busca
                .matching() // Inicia a construção do matcher
                .withIgnoreNullValues() // Ignora valores nulos nos campos do exemplo, buscando apenas o desejado pelo cliente
                .withIgnoreCase() // Ignora diferenças entre maiúsculas e minúsculas, eliminando este fator da busca
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Define o tipo de correspondência para strings (padrão é 'CONTAINING', que busca por letras entre palavras, mas pode ser tambem 'STARTING', sendo a busca por itens que iniciem com a string digitada, 'ENDING', os que finalizam com a string digitada ou 'EXACT', buscando exatamente a string digitada)
                .withIgnorePaths("id", "dataNascimento"); // Ignora os campos 'id' e 'dataNascimento' na busca ou os campos que nao serao usados na busca
        Example<Autor> autorExample = Example.of(autor, matcher); // Cria o Example<Autor> com a entidade de exemplo e o matcher personalizado
        return repository.findAll(autorExample); // Realiza a busca dinâmica usando o Example

        /**
         * Neste código acima, criamos um ExampleMatcher personalizado que:
         * - Ignora valores nulos, permitindo buscas parciais.
         * - Ignora diferenças entre maiúsculas e minúsculas.
         * - Usa correspondência "CONTAINING" para strings, permitindo buscas por substrings.
         * Em seguida, criamos um Example<Autor> com a entidade de exemplo e o matcher, e usamos repository.findAll(autorExample) para realizar a busca dinâmica.
         * A necessidade do Example surge quando queremos permitir buscas flexíveis e dinâmicas, onde os critérios podem variar conforme a entrada do usuário.
         */
    }

    public boolean possuiLivro(Autor autor) { // Verifica se o autor possui livros cadastrados
        return livroRepository.existsByAutor(autor);
    }
}
