package dicasa.estoque.models.dto;

import java.time.LocalDateTime;

/**
 * DTO que exibe os dados do usuário
 * @param id_usuario
 * @param nome
 * @param email
 * @param isAdmin
 * @param created_at
 */
public record UsarioResponseDTO (
        Long id_usuario,
        String nome,
        String email,
        Boolean isAdmin,
        String created_at
) {
}
