package dicasa.estoque.models.dto;

/**
 * DTO que recebe os dados do usuário
 * @param nome
 * @param email
 * @param senha
 * @param isAdmin
 */
public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        Boolean isAdmin
) {
}
