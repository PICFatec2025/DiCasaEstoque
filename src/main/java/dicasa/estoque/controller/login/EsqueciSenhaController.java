package dicasa.estoque.controller.login;

import dicasa.estoque.util.EmailValidator;
import dicasa.estoque.service.UsuarioService;
import dicasa.estoque.service.EmailService;
import dicasa.estoque.navigation.ScreenNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static dicasa.estoque.navigation.Rotas.LOGIN_VIEW;
import static dicasa.estoque.util.Constraints.defineTamanhoMaximoTextField;
import static dicasa.estoque.util.Constraints.textFieldEstaEmBranco;

@Component
public class EsqueciSenhaController implements Initializable {

    @FXML
    public TextField textFieldEmail;
    @FXML
    public Button buttonRecuperarSenha;
    @FXML
    public Label labelErrorEmail;
    @FXML
    public Button buttonVoltar;

    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final JdbcTemplate jdbcTemplate;

    public EsqueciSenhaController(
            UsuarioService usuarioService,
            EmailService emailService,
            JdbcTemplate jdbcTemplate
    ) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defineTamanhoMaximoTextField(textFieldEmail, 100);
    }

    @FXML
    public void onCLickButtonRecuperarSenha(ActionEvent event) {
        String email = textFieldEmail.getText();

        if (textFieldEstaEmBranco(textFieldEmail, labelErrorEmail)) {
            showAlert("Campo vazio", "Preencha o campo de e-mail para recuperar a senha", Alert.AlertType.WARNING);
            return;
        }

        if (!EmailValidator.isValidEmail(email)) {
            showAlert("E-mail inválido", "Insira um e-mail válido, como: \"exemplo@mail.com\"", Alert.AlertType.WARNING);
            return;
        }

        // Verifica se o e-mail existe usando o método boolean
        if (!usuarioService.buscarUsuario(email)) {
            showAlert("Usuário não encontrado", "Usuário com esse e-mail não foi encontrado no sistema", Alert.AlertType.ERROR);
            return;
        }

        // Limite de 2 resets por dia
        if (!podeResetarSenhaHoje(email)) {
            showAlert("Limite atingido", "Você já solicitou recuperação de senha 2 vezes hoje. Tente novamente amanhã.", Alert.AlertType.WARNING);
            return;
        }

        // Gera nova senha
        String novaSenha = gerarSenhaAleatoria(8);

        // Atualiza no banco
        boolean atualizada = usuarioService.atualizarSenha(email, novaSenha);
        if (!atualizada) {
            showAlert("Erro", "Não foi possível atualizar a senha no banco de dados.", Alert.AlertType.ERROR);
            return;
        }

        // Envia e-mail
        boolean enviado = emailService.enviarEmail(
                email,
                "Recuperação de senha",
                "Uma nova senha foi gerada para sua conta:\n\n" +
                        novaSenha + "\n\n" +
                        "Use essa senha para acessar o sistema e altere-a em seguida."
        );

        if (enviado) {
            registrarReset(email);
            showAlert("E-mail enviado", "Uma nova senha foi enviada para " + email, Alert.AlertType.INFORMATION);
            ScreenNavigator.loadLoginView(LOGIN_VIEW, event);
        } else {
            showAlert("Erro ao enviar e-mail", "Não foi possível enviar a senha para o seu e-mail", Alert.AlertType.ERROR);
        }
    }

    /**
     * Verifica se o usuário ainda pode resetar a senha hoje (máximo 2 vezes).
     */
    private boolean podeResetarSenhaHoje(String email) {
        String sql = "SELECT tentativas FROM reset_senha_log WHERE user_email = ? AND data_reset = ?";
        try {
            Integer tentativas = jdbcTemplate.queryForObject(sql, Integer.class, email, LocalDate.now());
            return tentativas == null || tentativas < 2;
        } catch (Exception e) {
            // Nenhum registro encontrado
            return true;
        }
    }

    /**
     * Registra a tentativa de reset no banco.
     */
    private void registrarReset(String email) {
        String sqlSelect = "SELECT tentativas FROM reset_senha_log WHERE user_email = ? AND data_reset = ?";
        String sqlInsert = "INSERT INTO reset_senha_log (user_email, data_reset, tentativas) VALUES (?, ?, 1)";
        String sqlUpdate = "UPDATE reset_senha_log SET tentativas = tentativas + 1 WHERE user_email = ? AND data_reset = ?";

        try {
            Integer tentativas = jdbcTemplate.queryForObject(sqlSelect, Integer.class, email, LocalDate.now());
            if (tentativas == null) {
                jdbcTemplate.update(sqlInsert, email, LocalDate.now());
            } else {
                jdbcTemplate.update(sqlUpdate, email, LocalDate.now());
            }
        } catch (Exception e) {
            // Se não existir ainda, insere
            jdbcTemplate.update(sqlInsert, email, LocalDate.now());
        }
    }

    /**
     * Gera uma senha aleatória segura.
     */
    private String gerarSenhaAleatoria(int tamanho) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return sb.toString();
    }

    @FXML
    public void onClickVoltarButton(ActionEvent event) {
        ScreenNavigator.loadLoginView(LOGIN_VIEW, event);
    }

    private void showAlert(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
