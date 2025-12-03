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

/**
 * Controller responsável pela funcionalidade de recuperação de senha.
 * Permite que o usuário solicite uma nova senha via e-mail caso tenha esquecido a atual.
 * Inclui validações de formato de e-mail, existência de usuário e limite diário de solicitações.
 */
@Component
public class EsqueciSenhaController implements Initializable {

    /** Campo de texto FXML para a inserção do e-mail do usuário. */
    @FXML
    public TextField textFieldEmail;

    /** Botão FXML para iniciar o processo de recuperação de senha. */
    @FXML
    public Button buttonRecuperarSenha;

    /** Label FXML para exibir mensagens de erro ou validação do e-mail. */
    @FXML
    public Label labelErrorEmail;

    /** Botão FXML para retornar à tela de login. */
    @FXML
    public Button buttonVoltar;

    /** Serviço para manipulação das regras de negócio e dados de usuários. */
    private final UsuarioService usuarioService;

    /** Serviço responsável pelo envio de e-mails. */
    private final EmailService emailService;

    /** Template JDBC para interagir diretamente com o banco de dados (usado para log de reset). */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Construtor com injeção de dependências necessárias para recuperação de senha.
     *
     * @param usuarioService Serviço responsável por manipular dados do usuário.
     * @param emailService Serviço responsável pelo envio de e-mails.
     * @param jdbcTemplate Objeto JDBC para operações no banco de dados, usado especificamente
     * para registrar e consultar o log de tentativas de reset de senha.
     */
    public EsqueciSenhaController(
            UsuarioService usuarioService,
            EmailService emailService,
            JdbcTemplate jdbcTemplate
    ) {
        this.usuarioService = usuarioService;
        this.emailService = emailService;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inicializa a tela definindo as restrições dos campos, como o tamanho máximo do campo de e-mail.
     *
     * @param url Caminho do recurso.
     * @param resourceBundle Arquivo de recursos associado.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defineTamanhoMaximoTextField(textFieldEmail, 100);
    }

    /**
     * Acionado ao clicar no botão "Recuperar Senha".
     * Realiza validações de campo, valida e-mail, verifica se atingiu o limite de resets diários,
     * gera nova senha, atualiza no banco de dados e envia e-mail ao usuário.
     *
     * @param event Evento de clique do botão.
     */
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

        if (!usuarioService.buscarUsuario(email)) {
            showAlert("Usuário não encontrado", "Usuário com esse e-mail não foi encontrado no sistema", Alert.AlertType.ERROR);
            return;
        }

        if (!podeResetarSenhaHoje(email)) {
            showAlert("Limite atingido", "Você já solicitou recuperação de senha 2 vezes hoje. Tente novamente amanhã.", Alert.AlertType.WARNING);
            return;
        }

        String novaSenha = gerarSenhaAleatoria(8);

        boolean atualizada = usuarioService.atualizarSenha(email, novaSenha);
        if (!atualizada) {
            showAlert("Erro", "Não foi possível atualizar a senha no banco de dados.", Alert.AlertType.ERROR);
            return;
        }

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
     * Verifica se o usuário ainda pode solicitar redefinição de senha no dia atual.
     * A regra é de no máximo **2 solicitações por dia**, consultando a tabela `reset_senha_log`.
     *
     * @param email E-mail do usuário.
     * @return true se ainda pode resetar (tentativas < 2) ou se ocorrer um erro na consulta ao banco (evita bloquear o usuário);
     * false se atingiu o limite (tentativas >= 2).
     */
    private boolean podeResetarSenhaHoje(String email) {
        String sql = "SELECT tentativas FROM reset_senha_log WHERE user_email = ? AND data_reset = ?";
        try {
            Integer tentativas = jdbcTemplate.queryForObject(sql, Integer.class, email, LocalDate.now());
            // Se o resultado for nulo (primeira tentativa do dia), ou se for menor que 2
            return tentativas == null || tentativas < 2;
        } catch (Exception e) {
            // Se houver qualquer erro de banco (ex: tabela não existe, erro de conexão), 
            // assumimos que o reset é permitido para não bloquear o usuário.
            return true;
        }
    }

    /**
     * Registra ou atualiza no banco de dados uma tentativa de redefinição de senha
     * na tabela `reset_senha_log`.
     * <p>
     * Se não houver registro para o e-mail na data atual, insere uma nova entrada (tentativas = 1).
     * Caso contrário, incrementa o contador de tentativas.
     *
     * @param email E-mail do usuário que solicitou o reset.
     */
    private void registrarReset(String email) {
        String sqlSelect = "SELECT tentativas FROM reset_senha_log WHERE user_email = ? AND data_reset = ?";
        String sqlInsert = "INSERT INTO reset_senha_log (user_email, data_reset, tentativas) VALUES (?, ?, 1)";
        String sqlUpdate = "UPDATE reset_senha_log SET tentativas = tentativas + 1 WHERE user_email = ? AND data_reset = ?";

        try {
            Integer tentativas = jdbcTemplate.queryForObject(sqlSelect, Integer.class, email, LocalDate.now());
            if (tentativas == null) {
                // Primeira tentativa do dia
                jdbcTemplate.update(sqlInsert, email, LocalDate.now());
            } else {
                // Tentativas subsequentes
                jdbcTemplate.update(sqlUpdate, email, LocalDate.now());
            }
        } catch (Exception e) {
            // Em caso de erro na consulta, tenta garantir a inserção como primeira tentativa
            jdbcTemplate.update(sqlInsert, email, LocalDate.now());
        }
    }

    /**
     * Gera uma senha aleatória utilizando caracteres alfanuméricos.
     *
     * @param tamanho Quantidade de caracteres desejada para a senha.
     * @return Senha gerada.
     * @implNote Os caracteres utilizados são letras maiúsculas, minúsculas e números (A-Z, a-z, 0-9).
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

    /**
     * Retorna à tela de login principal através do ScreenNavigator.
     *
     * @param event Evento de clique do botão.
     */
    @FXML
    public void onClickVoltarButton(ActionEvent event) {
        ScreenNavigator.loadLoginView(LOGIN_VIEW, event);
    }

    /**
     * Exibe uma mensagem de alerta (Alert) para o usuário na interface.
     *
     * @param titulo Título da janela do alerta.
     * @param mensagem Conteúdo detalhado da mensagem.
     * @param tipo Tipo de alerta (ex: INFORMATION, WARNING, ERROR).
     */
    private void showAlert(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
