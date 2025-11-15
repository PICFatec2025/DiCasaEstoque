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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static dicasa.estoque.navigation.Rotas.*;
import static dicasa.estoque.util.Alerts.showAlerts;

/**
 * Classe que faz o gerenciamento de abertura e troca de telas e janelas nesse projeto
 */
public class ScreenNavigator {
    @Getter
    private static Scene scene;
    private static Stage primaryStage;
    /**
     * Quem vai fazer o gerenciamento de Controllers é o spring, ao invés do JavaFX
     */
    private static final ConfigurableApplicationContext springContext
            = SpringFXManager.getContext();

    /**
     * Função que carrega a tela inicial, a tela de Login
     * @param stage
     * @param springContext
     * @throws IOException
     */
    public static void initialScreen(
            Stage stage,
            ConfigurableApplicationContext springContext
    ) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = loadFXML(LOGIN_VIEW);
        fxmlLoader.setControllerFactory(springContext::getBean);
        AnchorPane anchorPane = fxmlLoader.load();
        scene = new Scene(anchorPane, 800, 600);
        stage.setTitle("Di Casa - Estoque");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Função que leva para a tela principal após a tela de login
     * @param nomeRota
     */
    public static void loadMainView(
            String nomeRota
    ){
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            ScrollPane scrollPane = fxmlLoader.load();

            // Sempre usa o primaryStage como fallback
            if (primaryStage != null) {
                if (scene != null) {
                    scene.setRoot(scrollPane);
                } else {
                    Scene newScene = new Scene(scrollPane, 800, 600);
                    primaryStage.setScene(newScene);
                    scene = newScene;
                }
                scrollPane.setFitToWidth(true);
                scrollPane.setFitToHeight(true);
            }

        } catch (IOException e) {
            messageError("Erro ao carregar a tela", e);
        }
    }

    /**
     * Função para carregar as telas de login, ou outras telas que não possuem a barra superior da tela
     * @param nomeRota
     * @param event
     */
    public static void loadLoginView(String nomeRota, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            AnchorPane anchorPane = fxmlLoader.load();

            Scene loginScene = new Scene(anchorPane, 800, 600);

            // Tenta obter o stage do evento, se não conseguir usa o primaryStage
            Stage stage = getStageFromEvent(event);
            if (stage == null) {
                stage = primaryStage;
            }

            if (stage != null) {
                stage.setScene(loginScene);
                stage.show();
                scene = loginScene; // Atualiza a referência da scene
            }

        } catch (IOException e) {
            messageError("Erro ao carregar a tela de login", e);
        }
    }

    /**
     * Função que implementa o carregamento de outras telas
     * Mas ele carrega em cima da tela principal, mantendo o menu de troca de telas
     * @param anchorPane
     * @param nomeRota
     */
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

    /**
     * Função que carrega uma janela em cima da tela
     * @param stage
     * @param nomeRota
     * @param tituloWindow
     * @param object
     */
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

    /**
     * Função que busca o stage da tela atual
     * @param event
     * @return
     */
    public static Stage currentStage(ActionEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
    private static Stage getStageFromEvent(ActionEvent event) {
        Object source = event.getSource();

        if (source instanceof Node) {
            return (Stage) ((Node) source).getScene().getWindow();
        } else if (source instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) source;
            // Obtém a janela dona do popup do menu
            Window ownerWindow = menuItem.getParentPopup().getOwnerWindow();
            if (ownerWindow instanceof Stage) {
                return (Stage) ownerWindow;
            }
        }

        // Fallback: procura por stages visíveis
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage && window.isShowing()) {
                return (Stage) window;
            }
        }

        return null;
    }

    /**
     * Função que fecha a janela aberta
     * @param event
     */
    public static void closeWindow(ActionEvent event){
        currentStage(event).close();
    }

    /**
     * Função que busca o arquivo fxml pelo nome da rota
     * @param nomeRota
     * @return
     */
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
