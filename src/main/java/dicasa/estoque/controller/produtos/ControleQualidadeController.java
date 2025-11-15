package dicasa.estoque.controller.produtos;

import javafx.fxml.Initializable;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ControleQualidadeController implements Initializable {
    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnRelatorio;

    @FXML
    private Button btnRemover;

    @FXML
    private ComboBox<?> cbFornecedor;

    @FXML
    private ComboBox<?> cbInsumo;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colFornecedor;

    @FXML
    private TableColumn<?, ?> colInsumo;

    @FXML
    private TableColumn<?, ?> colMarca;

    @FXML
    private TableColumn<?, ?> colNota;

    @FXML
    private TableColumn<?, ?> colObservacoes;

    @FXML
    private DatePicker dpData;

    @FXML
    private Spinner<?> spnNota;

    @FXML
    private TextArea taObservacoes;

    @FXML
    private TableView<?> tableHistorico;

    @FXML
    private TextField tfMarca;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
