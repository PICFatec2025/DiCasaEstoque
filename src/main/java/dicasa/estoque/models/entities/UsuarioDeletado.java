package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_deletado", schema = "dicasa_estoque")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UsuarioDeletado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_deletado")
    private Long idUsuarioDeletado;

    @Column(name = "id_original")
    private Long idOriginal;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "data_exclusao", nullable = false)
    private LocalDateTime dataExclusao = LocalDateTime.now();

    @Column(name = "motivo_exclusao")
    private String motivoExclusao;
}
