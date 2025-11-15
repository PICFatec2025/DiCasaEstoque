package dicasa.estoque.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class Alerts {
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
    public static boolean showConfirmation(
            String title,
            String content
    ){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.isPresent() && buttonType.get() == ButtonType.OK;
    }
    public static void messageError(String title, String message) {
        Alerts.showAlerts(
                title,
                null,
                message,
                AlertType.ERROR
        );
    }
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
