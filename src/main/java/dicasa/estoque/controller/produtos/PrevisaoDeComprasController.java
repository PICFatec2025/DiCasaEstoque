package dicasa.estoque.controller.produtos;

import javafx.fxml.Initializable;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PrevisaoDeComprasController implements Initializable {
    @FXML
    private ComboBox<?> cbCategoria;

    @FXML
    private ComboBox<?> cbUrgencia;

    @FXML
    private TableView<?> tabelaFornecedores;

    @FXML
    private TableView<?> tabelaProdutos;

    @FXML
    private TextField txtFiltro;

    @FXML
    void atualizarDados(ActionEvent event) {

    }

    @FXML
    void gerarListaCompras(ActionEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
