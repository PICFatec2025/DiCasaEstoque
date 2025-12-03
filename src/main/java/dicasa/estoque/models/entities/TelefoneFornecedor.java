package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que cuida dos dados de telefone do Fornecedor
 */

@Entity
@Table(name = "telefone_fornecedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneFornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefone_fornecedor")
    private Long idTelefone;
    @Column(nullable = false, length = 14)
    private String telefone;
    // MUITOS Telefones para UM Fornecedor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;
}
