package com.example.cursoudemy.libraryapi.controller.mappers;

import com.example.cursoudemy.libraryapi.controller.dto.AutorDTO;
import com.example.cursoudemy.libraryapi.models.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // Anotação do MapStruct que indica que esta interface é um mapper e deve ser gerenciada pelo Spring
public interface AutorMapper {

    // @Mapping(source = "idAutor", target = "id") // Exemplo de mapeamento personalizado, caso os nomes dos campos sejam diferentes entre o DTO e a entidade. Neste caso, não é necessário porque os campos têm o mesmo nome.
    // É possivel mapear qualquer campo, mesmo que os nomes sejam diferentes, usando a anotação @Mapping. O source é o nome do campo no DTO e o target é o nome do campo na entidade. Se os campos tiverem o mesmo nome, o MapStruct faz o mapeamento automaticamente, sem necessidade de usar @Mapping.
    Autor toEntity(AutorDTO dto); // Metodo para converter um AutorDTO em um Autor. O MapStruct gera a implementação deste método automaticamente, mapeando os campos correspondentes entre o DTO e a entidade.

    AutorDTO toDto(Autor autor);
}

/**
 * Os mappers são usados para converter entre as entidades do
 * domínio e os DTOs (Data Transfer Objects). Eles ajudam a
 * separar as preocupações entre as camadas da aplicação,
 * permitindo que a camada de apresentação (controladores)
 * trabalhe com DTOs, enquanto a camada de serviço e repositório
 * trabalham com as entidades do domínio. Isso melhora a organização
 * do código, facilita a manutenção e aumenta a segurança, evitando
 * expor diretamente as entidades do domínio para a camada de apresentação.
 * O uso de mappers também torna o código mais limpo e legível, já que a
 * lógica de conversão é centralizada em um único lugar.
 */
