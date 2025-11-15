package dicasa.estoque.models.entities;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Usuario {
    private Long id_usuario;
    private String nome;
    private String email;
    private String senha;
    private Boolean isAdmin;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
