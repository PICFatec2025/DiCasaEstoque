package dicasa.estoque.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Serviço responsável pelo envio de e-mails através do SMTP do Gmail.
 * Utiliza autenticação via senha de aplicativo configurada no arquivo
 * <code>application.yml</code> ou variáveis de ambiente.
 *
 * <p>Este serviço é usado para envio de mensagens como recuperação de senha,
 * notificações do sistema ou qualquer comunicação automatizada por e-mail.</p>
 */
@Service
public class EmailService {

    /**
     * Endereço de e-mail utilizado como remetente.
     * Obtido através do arquivo de configuração da aplicação.
     */
    private final String remetente;

    /**
     * Senha de aplicativo (App Password) gerada no provedor de e-mail.
     * Necessária para autenticação SMTP.
     */
    private final String senhaApp;

    /**
     * Construtor do serviço de e-mail com injeção dos valores configurados.
     *
     * @param remetente Endereço de e-mail que será utilizado como remetente.
     * @param senhaApp  Senha de aplicativo utilizada para autenticação no servidor SMTP.
     */
    public EmailService(
            @Value("${app.email.remetente}") String remetente,
            @Value("${app.email.senha}") String senhaApp) {
        this.remetente = remetente;
        this.senhaApp = senhaApp;
    }

    /**
     * Envia um e-mail simples em formato texto utilizando SMTP.
     *
     * @param destinatario Endereço de e-mail do destinatário.
     * @param assunto      Assunto da mensagem.
     * @param corpo        Corpo do e-mail em formato texto.
     * @return {@code true} se o e-mail foi enviado com sucesso,
     *         {@code false} se ocorreu alguma falha no envio.
     *
     * @throws IllegalArgumentException caso algum dos parâmetros obrigatórios seja nulo ou vazio.
     */
    public boolean enviarEmail(String destinatario, String assunto, String corpo) {

        if (destinatario == null || destinatario.isBlank()) {
            throw new IllegalArgumentException("O e-mail do destinatário não pode ser vazio.");
        }
        if (assunto == null) {
            throw new IllegalArgumentException("O assunto não pode ser nulo.");
        }
        if (corpo == null) {
            throw new IllegalArgumentException("O corpo do e-mail não pode ser nulo.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senhaApp);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)
            );
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);
            System.out.println("E-mail enviado com sucesso para: " + destinatario);
            return true;

        } catch (MessagingException e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
            return false;
        }
    }
}
