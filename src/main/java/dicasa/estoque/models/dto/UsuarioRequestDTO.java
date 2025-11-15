package dicasa.estoque.models.dto;

public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        Boolean isAdmin
) {
}
