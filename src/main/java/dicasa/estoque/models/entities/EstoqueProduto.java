package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que vai armazenar os valores de estoque dos produtos
 *
 */
@Entity
@Table(name = "estoque_produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_estoque_produto;
    @Column(nullable = false)
    private int quantidade;
    @Column(name = "quantidade_minima")
    private int quantidadeMinima;
    @Column(name = "estoque_emergencial")
    private int estoqueEmergencial;
    @Column(nullable = false)
    private LocalDateTime data_criacao;
    private LocalDateTime data_atualizacao;
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produto")
    private Produto produto;

    public int status(){
        if(this.quantidade==0){
            return 0;
        } else if (quantidade>=(quantidadeMinima*2)) {
            return 4;
        } else if (quantidade>=(quantidadeMinima)) {
            return 3;
        } else if (quantidade<quantidadeMinima&&quantidade>=estoqueEmergencial) {
            return 2;
        } else if (quantidade<quantidadeMinima&&quantidade<estoqueEmergencial) {
            return 1;
        } else  {
            return 5;
        }
    }
    public String statusTexto(){
        return switch (status()) {
            case 0 -> "Zerado";
            case 1 -> "Emergencial";
            case 2 -> "Minima";
            case 3 -> "Bom";
            case 4 -> "Ideal";
            default -> "Erro de dados";
        };
    }
}
