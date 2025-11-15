package dicasa.estoque.models.entities;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String nome;
    private BigDecimal preco;
    private int quantidade;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
