package dicasa.estoque.models.mapper;

import dicasa.estoque.models.dto.FornecedorResponseDTO;
import dicasa.estoque.models.entities.Fornecedor;
import dicasa.estoque.models.entities.TelefoneFornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper que mapeia as entidades relacionadas ao Fornecedor e seu endereço e telefone
 */
@Mapper(componentModel = "spring")
public interface FornecedorMapper {
    /**
     * Converte Fornecedor em seu DTO com dados de endereço e telefone
     * @param fornecedor
     * @return
     */
    @Mapping(source = "dataCriacao", target = "dataCriacao", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "dataAtualizacao", target = "dataAtualizacao", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "enderecoFornecedor.idEndereco",target = "idEndereco")
    @Mapping(source = "enderecoFornecedor.logradouro", target = "logradouro")
    @Mapping(source = "enderecoFornecedor.complemento", target = "complemento")
    @Mapping(source = "enderecoFornecedor.bairro", target = "bairro")
    @Mapping(source = "enderecoFornecedor.cidade", target = "cidade")
    @Mapping(source = "enderecoFornecedor.uf", target = "uf")
    @Mapping(source = "enderecoFornecedor.cep", target = "cep")
    @Mapping(source = "telefones", target = "telefones")
    FornecedorResponseDTO toDtoList(Fornecedor fornecedor);

    /**
     * Converte a lista de fornecedores em sua lista de DTO
     * @param fornecedores
     * @return
     */
    List<FornecedorResponseDTO> toDtoList(List<Fornecedor> fornecedores);

    /**
     * Método personalizado para mapear a lista de telefones para Map<Long, String>
     */
    default Map<Long, String> mapTelefones(List<TelefoneFornecedor> telefones) {
        if (telefones == null || telefones.isEmpty()) {
            return new HashMap<>();
        }

        return telefones.stream()
                .collect(Collectors.toMap(
                        TelefoneFornecedor::getIdTelefone,
                        TelefoneFornecedor::getTelefone
                ));
    }

    /**
     * Método para tratar datas nulas
     */
    default String mapLocalDateTime(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        // O format será aplicado automaticamente pela anotação @Mapping
        return date.toString(); // Fallback, mas o MapStruct usará o format especificado
    }

    /**
     * Método para tratar valores nulos do endereço
     */
    default String mapEnderecoField(String fieldValue) {
        return fieldValue != null ? fieldValue : "";
    }
}
