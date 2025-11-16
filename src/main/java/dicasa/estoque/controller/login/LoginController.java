package dicasa.estoque.controller.login;

import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.UsuarioService;
import dicasa.estoque.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static dicasa.estoque.navigation.Rotas.ESQUECI_SENHA;
import static dicasa.estoque.navigation.Rotas.MAIN_VIEW;
import static dicasa.estoque.util.Alerts.messageError;
import static dicasa.estoque.util.Constraints.defineTamanhoMaximoTextField;
import static dicasa.estoque.util.Constraints.textFieldEstaEmBranco;

@Component
public class LoginController implements Initializable {

    @FXML
    private Hyperlink hyperlinkEsqueciSenha;

    @FXML
    private Button buttonLogin;

    @FXML
    private Label labelErrorSenha;

    @FXML
    private Label labelErrorUsuario;

    @FXML
    private PasswordField textFieldSenha;

    @FXML
    private TextField textFieldUsuario;

    @FXML
    private TextField textFieldSenhaVisible;

    @FXML
    private CheckBox checkboxExibirSenha;

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaNodes();
        sincronizaSenha();
    }

    private void iniciaNodes() {
        defineTamanhoMaximoTextField(textFieldUsuario, 50);
        defineTamanhoMaximoTextField(textFieldSenha, 20);
        defineTamanhoMaximoTextField(textFieldSenhaVisible, 20);
    }

    private void sincronizaSenha() {
        textFieldSenha.textProperty().addListener((obs, oldText, newText) -> textFieldSenhaVisible.setText(newText));
        textFieldSenhaVisible.textProperty().addListener((obs, oldText, newText) -> textFieldSenha.setText(newText));
    }

    @FXML
    private void toggleSenha() {
        boolean exibir = checkboxExibirSenha.isSelected();

        textFieldSenha.setVisible(!exibir);
        textFieldSenha.setManaged(!exibir);

        textFieldSenhaVisible.setVisible(exibir);
        textFieldSenhaVisible.setManaged(exibir);
    }

    @FXML
    public void onCLickButtonLogin(ActionEvent event) {
        if (!validateForm()) {
            messageError("Erro ao Logar", "Campo Usuário e senha são obrigatórios");
            return;
        }

        String senha = checkboxExibirSenha.isSelected() ? textFieldSenhaVisible.getText() : textFieldSenha.getText();

        // Faz login
        boolean login = usuarioService.login(textFieldUsuario.getText(), senha);

        if (login) {
            // Recupera usuário do banco (para popular SessionManager)
            Usuario usuario = usuarioService.buscarPorNomeOuEmail(textFieldUsuario.getText());
            if (usuario != null) {
                SessionManager.setUsuarioLogado(usuario); // ⚡ Aqui setamos o usuário logado
            }

            ScreenNavigator.loadMainView(MAIN_VIEW);
        } else {
            messageError("Erro ao Logar", "Usuário e/ou senha incorretas");
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (textFieldEstaEmBranco(textFieldUsuario, labelErrorUsuario)) isValid = false;
        if (textFieldEstaEmBranco(textFieldSenha, labelErrorSenha)) isValid = false;
        return isValid;
    }

    @FXML
    public void onClickEsqueciSenha(ActionEvent event) {
        ScreenNavigator.loadLoginView(ESQUECI_SENHA, event);
    }
}
