package dicasa.estoque.controller;

import dicasa.estoque.models.mapper.UsuarioMapper;
import dicasa.estoque.navigation.Rotas;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.UsuarioService;
import dicasa.estoque.util.EmailValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static dicasa.estoque.navigation.Rotas.LOGIN_VIEW;
import static dicasa.estoque.util.Alerts.*;
import static dicasa.estoque.util.Constraints.*;

/**
 * Tela para realizar a recuperação de senha do usuário
 */
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
    private final UsuarioMapper usuarioMapper;

    public EsqueciSenhaController(
            UsuarioService usuarioService,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        defineTamanhoMaximoTextField(textFieldEmail,100);
    }

    /**
     * Função ao clicar no botão de recuperar senha
     * Ao clicar, verifica se o TextField está vazio, se o e-mail é válido
     * Se sim, chama o service para buscar o email no sistema
     * Se não tiver, aparece mensagem que não encontrou usuário
     * Se sim, avisa que mandou e-mail para recuperar a senha
     * @param event
     */
    @FXML
    public void onCLickButtonRecuperarSenha(ActionEvent event) {
        String email = textFieldEmail.getText();
        if(textFieldEstaEmBranco(textFieldEmail,labelErrorEmail)){
            messageError(
                    "Campo vazio",
                    "Preencha o campo de e-mail para recuperar a senha");
            return;
        }
        if (!EmailValidator.isValidEmail(email)) {
            messageError(
                    "E-mail válido",
                    "Insira um e-mail válido, como: \"exemplo@mail.com\""
                    );
            return;
        }
        if(usuarioService.buscarUsuario(email)){
            if(
                    showConfirmation(
                    "E-mail enviado",
                    "Um e-mail foi enviado para "+email+" para alterar a senha",
                            Alert.AlertType.INFORMATION
                    )
            ){
                ScreenNavigator.loadLoginView(LOGIN_VIEW,event);
            }
        } else {
            messageError(
                    "Usuário não encontrado",
                    "Usuário com esse e-mail não foi encontrado no sistema"
                    );
        }

    }

    public void onClickVoltarButton(ActionEvent event) {
        ScreenNavigator.loadLoginView(LOGIN_VIEW,event);
    }
}
