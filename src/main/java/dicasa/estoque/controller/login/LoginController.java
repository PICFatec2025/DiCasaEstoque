package dicasa.estoque.controller.login;

import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.UsuarioService;
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

/**
 * Classe que gerencia o código da tela de Login
 */

@Component
public class LoginController implements Initializable {
    @FXML
    public Hyperlink hyperlinkEsqueciSenha;
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

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Ao inicializar a tela, ele realiza essas funções
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaNodes();
    }

    /**
     * Definimos o tamanho máximo de caracteres que esses textfields podem receber
     */

    private void iniciaNodes() {
        defineTamanhoMaximoTextField(textFieldUsuario,10);
        defineTamanhoMaximoTextField(textFieldSenha,10);
    }

    /**
     * Função que é realizada ao clique do botão de Login.
     * Nele eve checa se os TextFields estão preenchidos
     * E depois chama o service para ver se está logado
     * Se tiver certo, vai para a tela principal
     * Se não, vai para a tela de erro
     */
    public void onCLickButtonLogin(ActionEvent event) {
        if(!validateForm()){
            messageError("Erro ao Logar","Campo Usuário e senha são obrigatórios");
            return;
        }
        boolean login = usuarioService.login(
                textFieldUsuario.getText(),
                textFieldSenha.getText()
        );
        if(login){
            ScreenNavigator.loadMainView(MAIN_VIEW);
        } else {
            messageError("Erro ao Logar","Usuário e/ou senha incorretas");
        }
    }

    /**
     * Função que valida se os TextFields estão vazios ou não
     * @return se está valido ou não
     */
    private boolean validateForm() {
        boolean isValid = true;

        if (textFieldEstaEmBranco(textFieldUsuario, labelErrorUsuario)) isValid = false;
        if (textFieldEstaEmBranco(textFieldSenha, labelErrorSenha)) isValid = false;

        return isValid;
    }

    public void onClickEsqueciSenha(ActionEvent event) {
        ScreenNavigator.loadLoginView(ESQUECI_SENHA,event);
    }
}
