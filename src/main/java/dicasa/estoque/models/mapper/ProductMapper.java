package dicasa.estoque.models.mapper;

import dicasa.estoque.models.dto.ProdutoResponseDTO;
import dicasa.estoque.models.entities.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(source = "preco", target = "preco", numberFormat = "R$ #,##0.00")
    @Mapping(source = "created_at", target = "created_at", dateFormat = "dd/MM/yyyy")
    ProdutoResponseDTO toDto(Produto produto);

    List<ProdutoResponseDTO> toDtoList(List<Produto> produtos);
}
