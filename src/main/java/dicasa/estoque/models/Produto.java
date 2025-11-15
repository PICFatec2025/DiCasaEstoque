package dicasa.estoque.models;

import jakarta.persistence.*;
import javafx.beans.property.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
@Data
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

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario_criador", nullable = false)
    private Integer idUsuarioCriador;

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