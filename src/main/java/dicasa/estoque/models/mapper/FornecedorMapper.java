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

@Mapper(componentModel = "spring")
public interface FornecedorMapper {

    @Mapping(source = "dataCriacao", target = "dataCriacao", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "dataAtualizacao", target = "dataAtualizacao", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "enderecoFornecedor.idEndereco", target = "idEndereco")
    @Mapping(source = "enderecoFornecedor.logradouro", target = "logradouro")
    @Mapping(source = "enderecoFornecedor.complemento", target = "complemento")
    @Mapping(source = "enderecoFornecedor.bairro", target = "bairro")
    @Mapping(source = "enderecoFornecedor.cidade", target = "cidade")
    @Mapping(source = "enderecoFornecedor.uf", target = "uf")
    @Mapping(source = "enderecoFornecedor.cep", target = "cep")
    @Mapping(source = "telefones", target = "telefones")
    FornecedorResponseDTO toDto(Fornecedor fornecedor); // Mudei o nome do método para toDto

    List<FornecedorResponseDTO> toDtoList(List<Fornecedor> fornecedores);

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

    default String mapLocalDateTime(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }

    default String mapEnderecoField(String fieldValue) {
        return fieldValue != null ? fieldValue : "";
    }
}