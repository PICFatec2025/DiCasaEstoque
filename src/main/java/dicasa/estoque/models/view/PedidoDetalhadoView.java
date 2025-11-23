package dicasa.estoque.models.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa a view consulta de pedidos
 * No banco de dados criamos uma view que já devolve o pedido com todas as informações de valores, quantidades,
 * sem realmente criar uma entidade nova
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Immutable// Marca como entidade somente leitura
@Table(name = "pedido_detalhado")
public class PedidoDetalhadoView {

    @Id
    @Column(name = "id_pedido")
    private Long idPedido;

    @Column(name = "data_compra")
    private LocalDateTime dataCompra;

    private String observacao;

    @Column(name = "desconto_pedido")
    private BigDecimal descontoPedido;

    private String usuario;

    @Column(name = "total_itens")
    private Integer totalItens;

    @Column(name = "total_quantidade")
    private Integer totalQuantidade;

    private BigDecimal subtotal;

    @Column(name = "total_descontos_itens")
    private BigDecimal totalDescontosItens;

    @Column(name = "total_liquido_itens")
    private BigDecimal totalLiquidoItens;

    @Column(name = "valor_final")
    private BigDecimal valorFinal;

}
