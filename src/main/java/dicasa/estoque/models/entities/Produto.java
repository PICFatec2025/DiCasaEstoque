package dicasa.estoque.models.entities;

import jakarta.persistence.*;
import javafx.beans.property.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que armazena Produto
 */

@Entity
@Table(name = "produto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "tipo", length = 30, nullable = false)
    private String tipo;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_criador")
    private Usuario usuario;

    @OneToOne(mappedBy = "produto", fetch = FetchType.EAGER)
    private EstoqueProduto estoqueProduto;

    // JavaFX Properties (para a TableView)
    public LongProperty idProdutoProperty() {
        return new SimpleLongProperty(idProduto);
    }

    public StringProperty nomeProperty() {
        return new SimpleStringProperty(nome);
    }

    public StringProperty marcaProperty() {
        return new SimpleStringProperty(marca);
    }

    public StringProperty tipoProperty() {
        return new SimpleStringProperty(tipo);
    }
}