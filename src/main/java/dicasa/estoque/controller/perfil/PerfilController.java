package dicasa.estoque.controller.perfil;

import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.service.UsuarioService;
import dicasa.estoque.util.EmailValidator;
import dicasa.estoque.util.PasswordUtil;
import dicasa.estoque.util.SessionManager;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.navigation.Rotas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static dicasa.estoque.util.Constraints.textFieldEstaEmBranco;

/**
 * Controlador responsável pela tela de perfil do usuário.
 * <p>
 * Permite atualizar informações de perfil, alterar senha e deletar conta.
 * Todas as operações são validadas e executadas através do {@link UsuarioService}.
 */
@Component
public class PerfilController implements Initializable {

    // ==================== CAMPOS FXML ====================

    /** Campo de texto para o nome do usuário logado. */
    @FXML private TextField textFieldNome;

    /** Campo de texto para o e-mail do usuário logado. */
    @FXML private TextField textFieldEmail;

    /** Campo de senha para a senha atual. */
    @FXML private PasswordField passwordFieldSenhaAntiga;

    /** Campo de senha para a nova senha. */
    @FXML private PasswordField passwordFieldSenhaNova;

    /** Campo de senha para confirmar a nova senha. */
    @FXML private PasswordField passwordFieldConfirmarSenha;

    /** Label de erro associada ao campo de nome. */
    @FXML private Label labelErrorNome;

    /** Label de erro associada ao campo de e-mail. */
    @FXML private Label labelErrorEmail;

    /** Label de erro associada à senha atual. */
    @FXML private Label labelErrorSenhaAntiga;

    /** Label de erro associada à nova senha. */
    @FXML private Label labelErrorSenhaNova;

    /** Label de erro associada à confirmação da nova senha. */
    @FXML private Label labelErrorConfirmarSenha;

    /** Botão para salvar alterações de perfil. */
    @FXML private Button buttonAtualizarPerfil;

    /** Botão para alterar a senha do usuário. */
    @FXML private Button buttonAlterarSenha;

    /** Botão para excluir a conta do usuário. */
    @FXML private Button buttonDeletarPerfil;

    // ==================== DEPENDÊNCIAS ====================

    private final UsuarioService usuarioService;
    private Usuario usuarioLogado;

    /**
     * Construtor com injeção de dependência do {@link UsuarioService}.
     *
     * @param usuarioService serviço responsável por gerenciar usuários
     */
    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ==================== INICIALIZAÇÃO ====================

    /**
     * Inicializa a tela de perfil com os dados do usuário logado.
     * Caso não haja usuário na sessão, desabilita os campos.
     *
     * @param url não utilizado
     * @param resourceBundle não utilizado
     * @throws NullPointerException se ocorrer falha ao acessar dados do usuário logado
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioLogado = SessionManager.getUsuarioLogado();

        if (usuarioLogado != null) {
            textFieldNome.setText(usuarioLogado.getNome());
            textFieldEmail.setText(usuarioLogado.getEmail());
        } else {
            showAlert("Erro", "Nenhum usuário logado encontrado.", Alert.AlertType.ERROR);
            desabilitarCampos();
        }
    }

    // ==================== ATUALIZAR PERFIL ====================

    /**
     * Atualiza as informações básicas do usuário (nome e e-mail).
     *
     * @param event evento de clique do botão
     * @throws IllegalArgumentException se algum campo estiver inválido
     */
    @FXML
    void onClickAtualizarPerfil(ActionEvent event) {
        limparErros();

        if (usuarioLogado == null) {
            showAlert("Erro", "Usuário não está logado.", Alert.AlertType.ERROR);
            return;
        }

        String novoNome = textFieldNome.getText().trim();
        String novoEmail = textFieldEmail.getText().trim();
        boolean valid = true;

        if (textFieldEstaEmBranco(textFieldNome, labelErrorNome)) valid = false;
        if (textFieldEstaEmBranco(textFieldEmail, labelErrorEmail)) valid = false;

        if (!EmailValidator.isValidEmail(novoEmail)) {
            labelErrorEmail.setText("E-mail inválido");
            showAlert("E-mail inválido", "Informe um e-mail válido (ex: exemplo@mail.com)", Alert.AlertType.WARNING);
            valid = false;
        }

        if (!valid) return;

        boolean atualizado = usuarioService.atualizarPerfil(usuarioLogado.getId(), novoNome, novoEmail);

        if (atualizado) {
            showAlert("Sucesso", "Perfil atualizado com sucesso!", Alert.AlertType.INFORMATION);
            usuarioLogado.setNome(novoNome);
            usuarioLogado.setEmail(novoEmail);
            SessionManager.setUsuarioLogado(usuarioLogado);
        } else {
            showAlert("Erro", "Nome ou e-mail já estão em uso por outro usuário.", Alert.AlertType.ERROR);
        }
    }

    // ==================== ALTERAR SENHA ====================

    /**
     * Altera a senha do usuário logado, realizando validações de segurança.
     * <p>
     * Valida se a senha atual está correta e se a nova senha não é idêntica à anterior.
     *
     * @param event evento de clique do botão
     * @throws SecurityException se a senha atual estiver incorreta
     * @throws IllegalArgumentException se as senhas novas não coincidirem ou forem inválidas
     */
    @FXML
    void onClickAlterarSenha(ActionEvent event) {
        limparErros();

        if (usuarioLogado == null) {
            showAlert("Erro", "Usuário não está logado.", Alert.AlertType.ERROR);
            return;
        }

        String senhaAntiga = passwordFieldSenhaAntiga.getText();
        String novaSenha = passwordFieldSenhaNova.getText();
        String confirmarSenha = passwordFieldConfirmarSenha.getText();
        boolean valid = true;

        if (textFieldEstaEmBranco(passwordFieldSenhaAntiga, labelErrorSenhaAntiga)) valid = false;
        if (textFieldEstaEmBranco(passwordFieldSenhaNova, labelErrorSenhaNova)) valid = false;
        if (textFieldEstaEmBranco(passwordFieldConfirmarSenha, labelErrorConfirmarSenha)) valid = false;

        if (!novaSenha.equals(confirmarSenha)) {
            labelErrorConfirmarSenha.setText("As senhas não coincidem");
            valid = false;
        }

        if (!valid) return;

        String senhaAtualBanco = usuarioService.getSenhaPorEmail(usuarioLogado.getEmail());

        if (!PasswordUtil.verifyPassword(senhaAntiga, senhaAtualBanco)) {
            labelErrorSenhaAntiga.setText("Senha atual incorreta");
            showAlert("Erro", "A senha atual está incorreta.", Alert.AlertType.ERROR);
            return;
        }

        if (PasswordUtil.verifyPassword(novaSenha, senhaAtualBanco)) {
            labelErrorSenhaNova.setText("A nova senha não pode ser igual à atual");
            showAlert("Aviso", "A nova senha não pode ser igual à atual.", Alert.AlertType.WARNING);
            return;
        }

        boolean alterada = usuarioService.atualizarSenha(usuarioLogado.getEmail(), novaSenha);

        if (alterada) {
            showAlert("Sucesso", "Senha alterada com sucesso!", Alert.AlertType.INFORMATION);
            limparCamposSenha();
        } else {
            showAlert("Erro", "Não foi possível alterar a senha.", Alert.AlertType.ERROR);
        }
    }

    // ==================== DELETAR PERFIL ====================

    /**
     * Exclui permanentemente a conta do usuário logado, mediante confirmação.
     *
     * @param event evento de clique do botão
     * @throws SecurityException se o usuário não estiver logado
     */
    @FXML
    void onClickDeletarPerfil(ActionEvent event) {
        if (usuarioLogado == null) {
            showAlert("Erro", "Usuário não está logado.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmação");
        confirm.setHeaderText("Tem certeza que deseja deletar sua conta?");
        confirm.setContentText("Essa ação é irreversível e apagará todos os seus dados.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            boolean deletado = usuarioService.deletarUsuario(usuarioLogado.getId());

            if (deletado) {
                showAlert("Conta Deletada", "Sua conta foi removida permanentemente.", Alert.AlertType.INFORMATION);
                SessionManager.logout();
                ScreenNavigator.loadLoginView(Rotas.LOGIN_VIEW, event);
            } else {
                showAlert("Erro", "Não foi possível deletar a conta.", Alert.AlertType.ERROR);
            }
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /** Limpa os campos de senha após uma alteração bem-sucedida. */
    private void limparCamposSenha() {
        passwordFieldSenhaAntiga.clear();
        passwordFieldSenhaNova.clear();
        passwordFieldConfirmarSenha.clear();
    }

    /** Remove todas as mensagens de erro exibidas na tela. */
    private void limparErros() {
        labelErrorNome.setText("");
        labelErrorEmail.setText("");
        labelErrorSenhaAntiga.setText("");
        labelErrorSenhaNova.setText("");
        labelErrorConfirmarSenha.setText("");
    }

    /** Desabilita todos os campos e botões da tela. */
    private void desabilitarCampos() {
        textFieldNome.setDisable(true);
        textFieldEmail.setDisable(true);
        passwordFieldSenhaAntiga.setDisable(true);
        passwordFieldSenhaNova.setDisable(true);
        passwordFieldConfirmarSenha.setDisable(true);
        buttonAtualizarPerfil.setDisable(true);
        buttonAlterarSenha.setDisable(true);
        buttonDeletarPerfil.setDisable(true);
    }

    /**
     * Exibe uma janela de alerta na tela.
     *
     * @param titulo título da janela
     * @param mensagem mensagem exibida
     * @param tipo tipo de alerta (informação, erro, aviso, etc.)
     * @return o {@link Alert} exibido
     */
    private Alert showAlert(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
        return alert;
    }
}
