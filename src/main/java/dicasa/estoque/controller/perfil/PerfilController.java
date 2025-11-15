package dicasa.estoque.controller.perfil;

import dicasa.estoque.models.dto.UsarioResponseDTO;
import dicasa.estoque.models.mapper.UsuarioMapper;
import dicasa.estoque.service.UsuarioService;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class PerfilController implements Initializable {
    @FXML
    public Label labelErrorNome;
    @FXML
    public Label labelErrorEmail;
    @FXML
    public Label labelErrorSenhaAntiga;
    @FXML
    public Label labelErrorSenhaNova;
    @FXML
    public Label labelErrorConfirmarSenha;
    @FXML
    private Button buttonAlterarSenha;

    @FXML
    private Button buttonAtualizarPerfil;

    @FXML
    private Button buttonDeletarPerfil;

    @FXML
    private PasswordField passwordFieldConfirmarSenha;

    @FXML
    private PasswordField passwordFieldSenhaNova;

    @FXML
    private PasswordField passwordFieldSenhaAntiga;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldNome;

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    public PerfilController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void onClickAlterarSenha(ActionEvent event) {

    }

    @FXML
    void onClickAtualizarPerfil(ActionEvent event) {

    }
    @FXML
    void onClickDeletarPerfil(ActionEvent event) {

    }
}
