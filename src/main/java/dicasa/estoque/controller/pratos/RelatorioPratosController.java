package dicasa.estoque.controller.pratos;

import javafx.fxml.Initializable;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class RelatorioPratosController implements Initializable {

    @FXML
    private Button btnFechar;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<?, ?> colAcoes;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colIngredientes;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableView<?> tblPratos;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
