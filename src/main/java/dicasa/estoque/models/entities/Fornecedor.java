package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidede que armazena os dados dos Fornecedores
 */

@Entity
@Table(name = "fornecedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Long idFornecedor;
    private String cnpj;
    @Column(name = "nome_fantasia", nullable = false, length = 100)
    private String nomeFantasia;
    @Column(name = "razao_social", nullable = false, length = 45)
    private String razaoSocial;
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_criador")
    private Usuario usuario;
    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
    private List<TelefoneFornecedor> telefones;

    @OneToOne(mappedBy = "fornecedor")
    private EnderecoFornecedor enderecoFornecedor;
}
