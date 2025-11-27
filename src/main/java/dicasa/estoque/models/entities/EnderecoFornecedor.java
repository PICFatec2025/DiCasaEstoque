package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que cuida dos dados de endereço do Fornecedor
 */

@Entity
@Table(name = "endereco_fornecedor",schema = "dicasa_estoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoFornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco_fornecedor")
    private Long idEndereco;
    @Column(length = 100, nullable = false)
    private String logradouro;
    @Column(length = 30)
    private String complemento;
    @Column(length = 50)
    private String bairro;
    @Column(length = 50)
    private String cidade;
    @Column(length = 2)
    private String uf;
    @Column(length = 9)
    private String cep;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;
}
