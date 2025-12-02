package dicasa.estoque.navigation;

import dicasa.estoque.EstoqueApplication;
import dicasa.estoque.controller.DataFormController;
import dicasa.estoque.controller.error.TelaErrorController;
import dicasa.estoque.util.SpringFXManager;
import javafx.application.Platform;
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
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

import static dicasa.estoque.navigation.Rotas.*;
import static dicasa.estoque.util.Alerts.showAlerts;


/**
 * Classe responsável por gerenciar a navegação entre telas e janelas do sistema.
 * Integra JavaFX com Spring Boot, mantendo o contexto de beans do Spring.
 */
public class ScreenNavigator {

    @Getter
    private static Scene scene;
    private static Stage primaryStage;

    // Método para configurar o contexto após inicialização
    // Contexto principal do Spring, inicializado no startup
    private static ConfigurableApplicationContext springContext =
            SpringFXManager.getContext();

    public static void initialScreen(Stage stage, ConfigurableApplicationContext springContext){
        try {
            primaryStage = stage;
            FXMLLoader fxmlLoader = loadFXML(LOGIN_VIEW);
            fxmlLoader.setControllerFactory(springContext::getBean);
            AnchorPane anchorPane = fxmlLoader.load();

            scene = new Scene(anchorPane, 800, 650);
            stage.setResizable(false);
            stage.setTitle("Di Casa - Estoque");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Método para configurar o contexto após inicialização
    public static void setSpringContext(ConfigurableApplicationContext context) {
        springContext = context;
    }

    public static void loadMainView(String nomeRota) {
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            ScrollPane scrollPane = fxmlLoader.load();

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
                primaryStage.setResizable(true);
                primaryStage.setMaximized(true);
            }

        } catch (IOException e) {
            messageError("Erro ao carregar a tela principal", e);
        }
    }

    public static void loadLoginView(String nomeRota, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = loadFXML(nomeRota);
            fxmlLoader.setControllerFactory(springContext::getBean);
            AnchorPane anchorPane = fxmlLoader.load();

            Scene loginScene = new Scene(anchorPane, 800, 650);
            Stage stage = getStageFromEvent(event);
            if (stage == null) stage = primaryStage;

            if (stage != null) {
                stage.setScene(loginScene);
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setWidth(800);
                stage.setHeight(650);
                stage.centerOnScreen();
                stage.show();

                scene = loginScene;
            }

        } catch (IOException e) {
            messageError("Erro ao carregar a tela de login", e);
        }
    }

    /**
     * Carrega uma nova tela dentro do layout principal.
     * Se não houver permissão ou erro, ignora silenciosamente e loga no console.
     */
    public static void loadView(AnchorPane anchorPane, String nomeRota) {
        try {
            System.out.println("🚀 ========== INICIANDO CARREGAMENTO DE TELA ==========");
            System.out.println("📁 Rota solicitada: " + nomeRota);
            System.out.println("🔧 AnchorPane: " + (anchorPane != null ? "OK" : "NULL"));

            // Verifica se a rota existe
            if (nomeRota == null || nomeRota.isEmpty()) {
                System.err.println("❌ ERRO: Rota está vazia ou nula");
                return;
            }

            System.out.println("🔄 Criando FXMLLoader...");
            FXMLLoader fxmlLoader = loadFXML(nomeRota);

            System.out.println("🔧 Configurando Controller Factory...");
            fxmlLoader.setControllerFactory(springContext::getBean);

            System.out.println("📦 Carregando FXML...");
            Parent newScreen = fxmlLoader.load();

            System.out.println("✅ FXML carregado com sucesso!");
            System.out.println("🎯 Controller: " + fxmlLoader.getController().getClass().getSimpleName());

            System.out.println("🖼️ Configurando layout na AnchorPane...");
            anchorPane.getChildren().setAll(newScreen);
            AnchorPane.setTopAnchor(newScreen, 0.0);
            AnchorPane.setBottomAnchor(newScreen, 0.0);
            AnchorPane.setLeftAnchor(newScreen, 0.0);
            AnchorPane.setRightAnchor(newScreen, 0.0);

            System.out.println("🎉 Tela carregada com SUCESSO: " + nomeRota);
            System.out.println("======================================================");

        } catch (Exception e) {
            messageError("Erro ao carregar a tela", e);
        }
    }

    public static void loadWindow(Stage stage, String nomeRota, String tituloWindow, Object object) {
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
            dialogStage.centerOnScreen();
            dialogStage.showAndWait();

        } catch (Exception e) {
            messageError("Erro ao carregar a janela modal", e);
        }
    }

    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private static Stage getStageFromEvent(ActionEvent event) {
        Object source = event.getSource();

        if (source instanceof Node) {
            return (Stage) ((Node) source).getScene().getWindow();
        } else if (source instanceof MenuItem menuItem) {
            Window ownerWindow = menuItem.getParentPopup().getOwnerWindow();
            if (ownerWindow instanceof Stage) {
                return (Stage) ownerWindow;
            }
        }

        for (Window window : Window.getWindows()) {
            if (window instanceof Stage && window.isShowing()) {
                return (Stage) window;
            }
        }

        return null;
    }

    public static void closeWindow(ActionEvent event) {
        currentStage(event).close();
    }

    private static FXMLLoader loadFXML(String nomeRota) {
        System.out.println("📂 Carregando FXML da rota: " + nomeRota);
        System.out.println("🔍 Resource URL: " + ScreenNavigator.class.getResource(nomeRota));
        return new FXMLLoader(ScreenNavigator.class.getResource(nomeRota));
    }

    private static void messageError(String message, Exception e) {
        showAlerts(
                "Erro de Navegação",
                message,
                e.getMessage(),
                Alert.AlertType.ERROR
        );
    }
}