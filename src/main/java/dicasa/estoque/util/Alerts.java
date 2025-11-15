package dicasa.estoque.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

/**
 * Classe que gera os alertas
 */
public class Alerts {
    /**
     * Função que permite carregar variados tipos de alerts
     * @param title
     * @param header
     * @param content
     * @param alertType
     */
    public static void showAlerts(
            String title,
            String header,
            String content,
            AlertType alertType
    ){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Função que cria um alert de confirmação, com 2 botões, 1 de cancelar e outro de confirmar
     * @param title
     * @param content
     * @return o boolean dizendo se o botão de confirmação foi pressionado
     */
    public static boolean showConfirmation(
            String title,
            String content,
            AlertType alertType
    ){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.isPresent() && buttonType.get() == ButtonType.OK;
    }

    /**
     * Função que carrega um alert epecífico de alert de erro
     * @param title
     * @param message
     */
    public static void messageError(String title, String message) {
        Alerts.showAlerts(
                title,
                null,
                message,
                AlertType.ERROR
        );
    }

    /**
     * Função que é usado quando há erro no preenchimento de formulário
     * permitindo um alert de erro + uma mensagem na Label do lado do TextField
     * @param label
     * @param title
     * @param textLabel
     * @param textAlert
     */
    public static void alertAndLabelErrorForm(
            Label label,
            String title,
            String textLabel,
            String textAlert
    ){
        label.setText(textLabel);
        messageError(title,textAlert);
    }

}
