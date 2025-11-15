package dicasa.estoque.controller.perfil;

import dicasa.estoque.models.mapper.UsuarioMapper;
import dicasa.estoque.service.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class NovoUsuarioController implements Initializable {
    @FXML
    private Button buttonCadastrarUsuário;

    @FXML
    private CheckBox checkBoxEhAdm;

    @FXML
    private Label labelErrorConfirmarSenha;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorNome;

    @FXML
    private Label labelErrorSenhaNova;

    @FXML
    private PasswordField passwordFieldConfirmarSenha;

    @FXML
    private PasswordField passwordFieldSenhaNova;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldNome;

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    public NovoUsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void onClickCadastrarUsuario(ActionEvent event) {

    }

}
