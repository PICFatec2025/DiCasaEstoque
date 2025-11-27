package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que relaciona as ligações entre Pedido, Produto e Fornecedor
 */

@Entity
@Table(name = "pedido_produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_produto")
    private Long idPedidoProduto;
    @Column(nullable = false)
    private int quantidade;
    @Column(name = "preco_total", nullable = false)
    private BigDecimal precoTotal;
    @Column(nullable = false)
    private BigDecimal desconto;
    @Column(name = "data_pedido",nullable = false)
    private LocalDateTime dataPedido;
    @Column(name = "data_atualizacao")
    private LocalDateTime data_atualizacao;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_fornecedor")
    private Fornecedor fornecedor;
    @JoinColumn(name = "id_produto")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Produto produto;
    @JoinColumn(name = "id_pedido")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Pedido pedido;
}
