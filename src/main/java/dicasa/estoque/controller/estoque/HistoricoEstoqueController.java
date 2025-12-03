package dicasa.estoque.controller.estoque;

import dicasa.estoque.models.entities.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

/**
 * Classe que provavelmente não existirá mais
 */
@Component
public class HistoricoEstoqueController {
    @FXML
    private ComboBox<String> produtoFilter;

    @FXML
    private TableView<Produto> produtoTableView;

    @FXML
    public void initialize() {
        // Preencher os ComboBox programaticamente
        produtoFilter.getItems().addAll("Todos os produtos", "Produto A", "Produto B", "Produto C");
        produtoFilter.setValue("Todos os produtos");
    }
}
