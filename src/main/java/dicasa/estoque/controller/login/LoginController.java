package dicasa.estoque.controller.login;

import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.UsuarioService;
import dicasa.estoque.util.LoginHistory;
import dicasa.estoque.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static dicasa.estoque.navigation.Rotas.ESQUECI_SENHA;
import static dicasa.estoque.navigation.Rotas.MAIN_VIEW;
import static dicasa.estoque.util.Alerts.messageError;
import static dicasa.estoque.util.Constraints.defineTamanhoMaximoTextField;
import static dicasa.estoque.util.Constraints.textFieldEstaEmBranco;

/**
 * Controller responsável pela tela de login.
 * Gerencia validação de credenciais, alternância de exibição da senha,
 * e navegação para a tela principal ou tela de recuperação de senha.
 */
@Component
public class LoginController implements Initializable {

    /** Link FXML para navegação à tela de recuperação de senha. */
    @FXML
    private Hyperlink hyperlinkEsqueciSenha;

    /** Botão FXML que inicia o processo de autenticação. */
    @FXML
    private Button buttonLogin;

    /** Label FXML para exibir mensagens de erro do campo senha. */
    @FXML
    private Label labelErrorSenha;

    /** Label FXML para exibir mensagens de erro do campo usuário. */
    @FXML
    private Label labelErrorUsuario;

    /** Campo de senha oculto (`PasswordField`) onde o usuário digita a credencial. */
    @FXML
    private PasswordField textFieldSenha;

    /** Campo de texto FXML para a inserção do nome de usuário ou e-mail. */
    @FXML
    private TextField textFieldUsuario;

    /** Campo de texto FXML visível (`TextField`) usado para exibir a senha quando o checkbox estiver marcado. */
    @FXML
    private TextField textFieldSenhaVisible;

    /** Checkbox FXML para alternar a visibilidade da senha entre oculto e visível. */
    @FXML
    private CheckBox checkboxExibirSenha;

    /** Menu de contexto para exibir sugestões de autocomplete */
    private ContextMenu autoCompleteMenu;

    /** Serviço responsável pela autenticação e manipulação de dados de usuários. */
    private final UsuarioService usuarioService;

    /**
     * Construtor com injeção do serviço de usuário.
     *
     * @param usuarioService Serviço responsável pela autenticação e busca de usuários.
     */
    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Inicializa a interface configurando as restrições dos campos e a sincronização
     * dos campos de senha (oculta e visível).
     *
     * @param url Caminho do recurso FXML.
     * @param resourceBundle Arquivo de recursos associado.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciaNodes();
        sincronizaSenha();
        configuraAutoComplete();
        configuraAcoesTecla();
    }

    /**
     * Define restrições e tamanhos máximos dos campos da interface:
     * <ul>
     * <li>Usuário: 50 caracteres</li>
     * <li>Senha (Oculta e Visível): 20 caracteres</li>
     * </ul>
     */
    private void iniciaNodes() {
        defineTamanhoMaximoTextField(textFieldUsuario, 50);
        defineTamanhoMaximoTextField(textFieldSenha, 20);
        defineTamanhoMaximoTextField(textFieldSenhaVisible, 20);

        // Inicializa o menu de autocomplete
        autoCompleteMenu = new ContextMenu();
        autoCompleteMenu.setAutoHide(true);
    }

    /**
     * Mantém sincronizados os campos de senha oculta (`textFieldSenha`) e senha visível (`textFieldSenhaVisible`)
     * utilizando Listeners na propriedade 'text' de ambos os campos.
     * Isso garante que a senha digitada seja a mesma, independente de qual campo está sendo exibido.
     */
    private void sincronizaSenha() {
        textFieldSenha.textProperty().addListener((obs, oldText, newText) -> textFieldSenhaVisible.setText(newText));
        textFieldSenhaVisible.textProperty().addListener((obs, oldText, newText) -> textFieldSenha.setText(newText));
    }

    /**
     * Configura o sistema de autocomplete para o campo de usuário
     */
    private void configuraAutoComplete() {
        textFieldUsuario.textProperty().addListener((obs, oldText, typed) -> {
            if (typed == null || typed.isBlank()) {
                autoCompleteMenu.hide();
                return;
            }

            List<String> sugestoes = LoginHistory.getFilteredHistory(typed);

            if (sugestoes.isEmpty()) {
                autoCompleteMenu.hide();
                return;
            }

            criarDropdown(sugestoes);

            if (!autoCompleteMenu.isShowing()) {
                autoCompleteMenu.show(textFieldUsuario, Side.BOTTOM, 0, 0);
            }
        });

        // Fecha o menu ao perder o foco
        textFieldUsuario.focusedProperty().addListener((obs, oldV, focused) -> {
            if (!focused) autoCompleteMenu.hide();
        });
    }

    /**
     * Cria o menu dropdown com as sugestões de autocomplete
     * @param sugestoes Lista de sugestões para exibir
     */
    private void criarDropdown(List<String> sugestoes) {
        autoCompleteMenu.getItems().clear();

        for (String sugestao : sugestoes) {
            MenuItem item = new MenuItem(sugestao);
            item.setStyle("-fx-padding: 8px 12px; -fx-font-size: 14px;");
            item.setOnAction(e -> {
                textFieldUsuario.setText(sugestao);
                autoCompleteMenu.hide();
                textFieldSenha.requestFocus();
            });
            autoCompleteMenu.getItems().add(item);
        }
    }

    /**
     * Acionado pelo checkbox "Exibir Senha".
     * Alterna a visibilidade e o gerenciamento de layout dos campos
     * de senha oculta (`PasswordField`) e senha visível (`TextField`).
     */
    @FXML
    private void toggleSenha() {
        boolean exibir = checkboxExibirSenha.isSelected();

        // Oculta/Exibe o campo de senha (PasswordField)
        textFieldSenha.setVisible(!exibir);
        textFieldSenha.setManaged(!exibir);

        // Oculta/Exibe o campo de texto visível (TextField)
        textFieldSenhaVisible.setVisible(exibir);
        textFieldSenhaVisible.setManaged(exibir);
    }

    /**
     * Acionado ao clicar no botão de login.
     * Valida o formulário, tenta autenticar o usuário e, em caso de sucesso,
     * salva o usuário na sessão e redireciona para a tela principal.
     * A senha é extraída do campo visível ou oculto, dependendo do estado do checkbox "Exibir Senha".
     *
     * @param event Evento disparado pelo clique do botão.
     */
    @FXML
    public void onCLickButtonLogin(ActionEvent event) {
        if (!validateForm()) {
            messageError("Erro ao Logar", "Campo Usuário e senha são obrigatórios");
            return;
        }

        // Determina qual campo de senha usar com base no estado do checkbox
        String senha = checkboxExibirSenha.isSelected() ? textFieldSenhaVisible.getText() : textFieldSenha.getText();

        boolean login = usuarioService.login(textFieldUsuario.getText(), senha);

        if (login) {
            // Adiciona ao histórico de logins
            LoginHistory.addToHistory(textFieldUsuario.getText());

            Usuario usuario = usuarioService.buscarPorNomeOuEmail(textFieldUsuario.getText());
            if (usuario != null) {
                SessionManager.setUsuarioLogado(usuario);
            }

            ScreenNavigator.loadMainView(MAIN_VIEW);
        } else {
            messageError("Erro ao Logar", "Usuário e/ou senha incorretas");
        }
    }

    /**
     * Valida se os campos de usuário e senha estão preenchidos,
     * utilizando a função `textFieldEstaEmBranco` para verificar ambos.
     *
     * @return true se ambos os campos forem válidos (não vazios), false caso contrário.
     */
    private boolean validateForm() {
        boolean isValid = true;
        if (textFieldEstaEmBranco(textFieldUsuario, labelErrorUsuario)) isValid = false;
        if (textFieldEstaEmBranco(textFieldSenha, labelErrorSenha)) isValid = false;
        return isValid;
    }

    /**
     * Navega para a tela de recuperação de senha, conforme a rota definida em {@code ESQUECI_SENHA}.
     *
     * @param event Evento disparado pelo clique no hyperlink.
     */
    @FXML
    public void onClickEsqueciSenha(ActionEvent event) {
        ScreenNavigator.loadLoginView(ESQUECI_SENHA, event);
    }

    /**
     * Configura ações de teclado para campos de login
     */
    private void configuraAcoesTecla() {
        // No campo de usuário, pressionar Enter move o foco para o campo de senha
        textFieldUsuario.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Fecha o autocomplete se estiver aberto
                if (autoCompleteMenu.isShowing()) {
                    autoCompleteMenu.hide();
                }

                // Mover o foco para o campo de senha
                if (checkboxExibirSenha.isSelected()) {
                    textFieldSenhaVisible.requestFocus();
                } else {
                    textFieldSenha.requestFocus();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                // Fecha o autocomplete com ESC
                autoCompleteMenu.hide();
            }
        });

        // No campo de senha (oculto), pressionar Enter executa o login
        textFieldSenha.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onCLickButtonLogin(new ActionEvent());
            }
        });

        // No campo de senha (visível), pressionar Enter executa o login
        textFieldSenhaVisible.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onCLickButtonLogin(new ActionEvent());
            }
        });
    }
}