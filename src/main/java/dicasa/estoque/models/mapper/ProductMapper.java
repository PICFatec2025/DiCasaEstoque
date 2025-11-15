package dicasa.estoque.models.mapper;

import dicasa.estoque.models.dto.ProdutoResponseDTO;
import dicasa.estoque.models.entities.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Uma classe de exemplo de como o Mapstruck (biblioteca do Java) funciona,
 * fazendo a conversão da entidade para o DTO
 * assim facilitando a leitura de dados na tela
 */

@Mapper(componentModel = "spring")
public interface ProductMapper {
    /**
     * Faz a conversão do produto para o seu DTO
     * ignora o Id e Update At
     * Deixa o preço e Created At formatado
     * @param produto
     * @return dto da classe Produto
     */
    @Mapping(source = "preco", target = "preco", numberFormat = "R$ #,##0.00")
    @Mapping(source = "created_at", target = "created_at", dateFormat = "dd/MM/yyyy")
    ProdutoResponseDTO toDto(Produto produto);

    /**
     * Converte a lista de Produto para a lista do seu DTO
     * @param produtos
     * @return
     */
    List<ProdutoResponseDTO> toDtoList(List<Produto> produtos);
}
