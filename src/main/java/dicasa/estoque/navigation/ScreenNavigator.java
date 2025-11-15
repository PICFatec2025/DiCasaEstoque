package dicasa.estoque.navigation;


import dicasa.estoque.EstoqueApplication;
import dicasa.estoque.controller.DataFormController;
import dicasa.estoque.util.SpringFXManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static dicasa.estoque.navigation.Rotas.*;
import static dicasa.estoque.util.Alerts.showAlerts;


public class ScreenNavigator {
    @Getter
    private static Scene scene;
    private static final ConfigurableApplicationContext springContext
            = SpringFXManager.getContext();
    public static void initialScreen(
            Stage stage,
            ConfigurableApplicationContext springContext
    ) throws IOException {
        FXMLLoader fxmlLoader = loadFXML(LOGIN_VIEW);
        fxmlLoader.setControllerFactory(springContext::getBean);
        AnchorPane anchorPane = fxmlLoader.load();
        scene = new Scene(anchorPane, 800, 600);
        stage.setTitle("Di Casa - Estoque");
        stage.setScene(scene);
        stage.show();
    }
    public static void loadMainView(
            String nomeRota
    ){
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            ScrollPane scrollPane = fxmlLoader.load();
            scene.setRoot(scrollPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
        } catch (IOException e) {
            messageError("Erro ao carregar a tela",e);
        }
    }
    public static void loadView(
            AnchorPane anchorPane,
            String nomeRota
    ){
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent newScreen = fxmlLoader.load();
            anchorPane.getChildren().setAll(newScreen);
            AnchorPane.setTopAnchor(newScreen, 0.0);
            AnchorPane.setBottomAnchor(newScreen, 0.0);
            AnchorPane.setLeftAnchor(newScreen, 0.0);
            AnchorPane.setRightAnchor(newScreen, 0.0);

        } catch (IOException e) {
            messageError("Erro ao carregar a tela",e);
        }
    }
    public static void loadWindow(
           Stage stage,
           String nomeRota,
           String tituloWindow,
           Object object
    ){
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            Parent pane = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if (controller instanceof DataFormController dataFormController) {
                dataFormController.resetForm();
                dataFormController.setFormData(object);
            }
            Stage dialogStage = new Stage();
            dialogStage.setTitle(tituloWindow);
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (Exception e) {
            messageError("Erro ao carregar a janela",e);
        }
    }

    public static Stage currentStage(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
    public static void closeWindow(ActionEvent event){
        currentStage(event).close();
    }

    private static FXMLLoader loadFXML(String nomeRota){
        return new FXMLLoader(EstoqueApplication.class.getResource(nomeRota));
    }
    private static void messageError(String message, Exception e){
        showAlerts(
                "IO Exception",
                message,
                e.getMessage(),
                Alert.AlertType.ERROR
        );
    }
}
