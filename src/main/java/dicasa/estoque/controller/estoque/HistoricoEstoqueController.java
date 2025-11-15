package dicasa.estoque.controller.estoque;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.springframework.stereotype.Component;

@Component
public class HistoricoEstoqueController {
    @FXML
    public DatePicker dataInicioFilter;
    @FXML
    public DatePicker dataFimFilter;
    @FXML
    private ComboBox<String> categoriaFilter;

    @FXML
    private ComboBox<String> produtoFilter;

    @FXML
    public void initialize() {
        // Preencher os ComboBox programaticamente
        categoriaFilter.getItems().addAll("Todas Categorias", "Categoria A", "Categoria B");
        produtoFilter.getItems().addAll("Todos os produtos", "Produto A", "Produto B", "Produto C");
        categoriaFilter.setValue("Todas Categorias");
        produtoFilter.setValue("Todos os produtos");
    }
}
