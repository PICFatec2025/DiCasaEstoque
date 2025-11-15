package dicasa.estoque.models.mapper;

import dicasa.estoque.models.dto.UsarioResponseDTO;
import dicasa.estoque.models.dto.UsuarioRequestDTO;
import dicasa.estoque.models.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(source = "created_at", target = "created_at", dateFormat = "dd/MM/yyyy")
    UsarioResponseDTO toDto(Usuario usuario);
    Usuario toUsuario(UsuarioRequestDTO dto);
}
