package dicasa.estoque.models.dto;

import java.time.LocalDateTime;

public record UsarioResponseDTO (
        Long id_usuario,
        String nome,
        String email,
        Boolean isAdmin,
        String created_at
) {
}
