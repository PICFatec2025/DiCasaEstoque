package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que armazena o Pedido
 */

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;
    @Column(nullable = false)
    private BigDecimal desconto;
    @Column(nullable = true)
    private String observacao;
    @Column(name = "data_compra",nullable = false)
    private LocalDateTime dataCompra;
    @Column(name = "data_atualizacao")
    private LocalDateTime data_atualizacao;
    @OneToMany(mappedBy = "pedido", fetch = FetchType.EAGER)
    private List<PedidoProduto> pedidoProdutos;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_criador")
    private Usuario usuario;
}
