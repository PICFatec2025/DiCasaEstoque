package dicasa.estoque.controller.perfil;

import dicasa.estoque.service.UsuarioService;
import dicasa.estoque.util.EmailValidator;
import dicasa.estoque.util.SessionManager;
import dicasa.estoque.navigation.ScreenNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static dicasa.estoque.util.Constraints.*;

/**
 * Controlador responsável pelo cadastro de novos usuários.
 * <p>
 * Esta classe gerencia a tela de criação de usuários, aplicando
 * validações de campos, verificando permissões e interagindo com
 * o {@link UsuarioService} para persistência dos dados.
 */
@Component
public class NovoUsuarioController implements Initializable {

    /** Campo de texto para inserir o nome do novo usuário. */
    @FXML private TextField textFieldNome;

    /** Campo de texto para inserir o e-mail do novo usuário. */
    @FXML private TextField textFieldEmail;

    /** Campo de senha para a nova senha do usuário. */
    @FXML private PasswordField passwordFieldSenhaNova;

    /** Campo de senha para confirmação da senha digitada. */
    @FXML private PasswordField passwordFieldConfirmarSenha;

    /** Botão responsável por acionar o cadastro de novo usuário. */
    @FXML private Button buttonCadastrarUsuario;

    /** Label de erro exibida caso o nome seja inválido ou vazio. */
    @FXML private Label labelErrorNome;

    /** Label de erro exibida caso o e-mail seja inválido ou vazio. */
    @FXML private Label labelErrorEmail;

    /** Label de erro exibida caso a senha nova seja inválida. */
    @FXML private Label labelErrorSenhaNova;

    /** Label de erro exibida caso a confirmação da senha não coincida. */
    @FXML private Label labelErrorConfirmarSenha;

    private final UsuarioService usuarioService;

    /**
     * Construtor com injeção de dependência do serviço de usuários.
     *
     * @param usuarioService serviço responsável por operações de usuário
     */
    public NovoUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Inicializa a tela de cadastro, aplicando validações iniciais e
     * verificando se o usuário logado possui permissão de administrador.
     *
     * @param url não utilizado
     * @param resourceBundle não utilizado
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!SessionManager.isAdmin()) {
            showAlert("Acesso negado", "Apenas administradores podem criar novos usuários", Alert.AlertType.ERROR);
            ScreenNavigator.loadMainView("/views/main_view.fxml");
            return;
        }

        defineTamanhoMaximoTextField(textFieldNome, 50);
        defineTamanhoMaximoTextField(textFieldEmail, 100);
        defineTamanhoMaximoTextField(passwordFieldSenhaNova, 20);
        defineTamanhoMaximoTextField(passwordFieldConfirmarSenha, 20);
    }

    /**
     * Executado ao clicar no botão de cadastro.
     * <p>
     * Realiza validações dos campos, verifica duplicidade de nome/e-mail,
     * e solicita ao {@link UsuarioService} a criação do novo usuário.
     *
     * @param event evento de clique do botão
     * @throws IllegalArgumentException se os campos estiverem inválidos
     */
    @FXML
    public void onClickCadastrarUsuario(ActionEvent event) {
        String nome = textFieldNome.getText().trim();
        String email = textFieldEmail.getText().trim();
        String senha = passwordFieldSenhaNova.getText();
        String confirmarSenha = passwordFieldConfirmarSenha.getText();

        boolean valid = true;

        if (textFieldEstaEmBranco(textFieldNome, labelErrorNome)) valid = false;
        if (textFieldEstaEmBranco(textFieldEmail, labelErrorEmail)) valid = false;
        if (textFieldEstaEmBranco(passwordFieldSenhaNova, labelErrorSenhaNova)) valid = false;
        if (textFieldEstaEmBranco(passwordFieldConfirmarSenha, labelErrorConfirmarSenha)) valid = false;

        if (!senha.equals(confirmarSenha)) {
            labelErrorConfirmarSenha.setText("As senhas não coincidem");
            valid = false;
        }

        if (!valid) return;

        if (!EmailValidator.isValidEmail(email)) {
            labelErrorEmail.setText("E-mail inválido");
            showAlert("E-mail inválido", "Insira um e-mail válido, como: exemplo@mail.com", Alert.AlertType.WARNING);
            return;
        }

        if (usuarioService.existsByNome(nome)) {
            labelErrorNome.setText("Nome já cadastrado");
            showAlert("Erro", "Já existe um usuário com este nome", Alert.AlertType.ERROR);
            return;
        }

        if (usuarioService.existsByEmail(email)) {
            labelErrorEmail.setText("E-mail já cadastrado");
            showAlert("Erro", "Já existe um usuário com este e-mail", Alert.AlertType.ERROR);
            return;
        }

        boolean criado = usuarioService.criarUsuario(nome, email, senha);

        if (criado) {
            showAlert("Sucesso", "Usuário criado com sucesso!", Alert.AlertType.INFORMATION);
            limparCampos();
        } else {
            showAlert("Erro", "Não foi possível criar o usuário", Alert.AlertType.ERROR);
        }
    }

    /** Limpa os campos de texto e remove mensagens de erro da tela. */
    private void limparCampos() {
        textFieldNome.clear();
        textFieldEmail.clear();
        passwordFieldSenhaNova.clear();
        passwordFieldConfirmarSenha.clear();

        labelErrorNome.setText("");
        labelErrorEmail.setText("");
        labelErrorSenhaNova.setText("");
        labelErrorConfirmarSenha.setText("");
    }

    /**
     * Exibe uma caixa de diálogo na tela.
     *
     * @param titulo título da janela
     * @param mensagem conteúdo da mensagem
     * @param tipo tipo de alerta (informação, erro, aviso)
     */
    private void showAlert(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
