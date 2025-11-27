package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que cuida do usuário logado no sistema
 */

@Entity
@Table(name = "usuario", schema = "dicasa_estoque",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "nome"),
                @UniqueConstraint(columnNames = "email")
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private Boolean isAdmin = false;

    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    private LocalDateTime updated_at;

    // Relação com Produto (um usuário cria muitos produtos)
    @OneToMany(mappedBy = "usuario")
    private List<Produto> produtos = new ArrayList<>();

    // Relação com Fornecedor (um usuário cria muitos fornecedores)
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Fornecedor> fornecedores = new ArrayList<>();

    // Relação com Pedido (um usuário faz muitos pedidos)
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
}
