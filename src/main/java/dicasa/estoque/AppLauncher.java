package dicasa.estoque;

import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.util.Alerts;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe de execução do JavaFX
 */
public class AppLauncher extends Application {
//    private ConfigurableApplicationContext springContext;
//
//    /**
//     * Carrega o Spring Boot antes de inicializar o JavaFX
//     */
//    @Override
//    public void init(){
//        springContext = SpringApplication.run(EstoqueApplication.class);
//    }
//
//    /**
//     * Carrega a tela inicial, a tela de Login
//     * @param primaryStage
//     * @throws Exception
//     */
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        try {
//            // Configura exception handler global
//            Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
//                System.err.println("Exception não tratada: " + throwable.getMessage());
//                throwable.printStackTrace();
//            });
//
//            // Chama o initialScreen passando o primaryStage
//            ScreenNavigator.initialScreen(primaryStage, springContext);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Ao fechar o projeto, finaliza o Spring Boot
//     */
//    @Override
//    public void stop(){
//        springContext.stop();
//    }
//
//    private void showErrorScreen(Stage stage, Exception e) {
//        // Cria uma tela de erro básica sem depender do Spring
//        VBox vbox = new VBox(20);
//        vbox.setAlignment(Pos.CENTER);
//        vbox.setPadding(new Insets(40));
//
//        Label title = new Label("Erro ao iniciar a aplicação");
//        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
//
//        TextArea errorText = new TextArea(e.toString());
//        errorText.setEditable(false);
//        errorText.setWrapText(true);
//        errorText.setMaxWidth(500);
//        errorText.setMaxHeight(200);
//
//        Button closeButton = new Button("Fechar");
//        closeButton.setOnAction(event -> Platform.exit());
//
//        vbox.getChildren().addAll(title, errorText, closeButton);
//
//        Scene scene = new Scene(vbox, 600, 400);
//        stage.setScene(scene);
//        stage.setTitle("Erro - Di Casa Estoque");
//        stage.show();
//    }
private ConfigurableApplicationContext springContext;
    /**
     //     * Carrega a tela inicial, a tela de Login
     //     * @param primaryStage
     //     * @throws Exception
     //     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Mostra uma tela de carregamento imediatamente
            showLoadingScreen(primaryStage);

            // 2. Inicializa o Spring em uma thread separada
            initializeSpringAsync(primaryStage);

        } catch (Exception e) {
            showFallbackError(primaryStage, e);
        }
    }

    /**
     * Carrega a tela de carregamento enquanto a tela não abre
     * @param stage
     */
    private void showLoadingScreen(Stage stage) {
        VBox loadingBox = new VBox(20);
        loadingBox.setAlignment(Pos.CENTER);

        ProgressIndicator progress = new ProgressIndicator();
        Label label = new Label("Inicializando sistema...");

        loadingBox.getChildren().addAll(progress, label);

        Scene scene = new Scene(loadingBox, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Di Casa - Estoque");
        stage.show();
    }

    /**
     * Função que faz a verificação da conexão com a internet
     * @param primaryStage
     */
    private void initializeSpringAsync(Stage primaryStage) {
        // Cria uma thread separada para inicializar o Spring
        new Thread(() -> {
            try {
                // Verifica conexão antes de inicializar Spring
                if (!checkNetworkConnection()) {
                    Platform.runLater(() -> showNetworkErrorScreen(primaryStage, true));
                    return;
                }

                // Inicializa o Spring
                this.springContext = SpringApplication.run(EstoqueApplication.class);

                // Se chegou aqui, Spring inicializou com sucesso
                Platform.runLater(() -> {
                    try {
                        ScreenNavigator.setSpringContext(springContext);
                        ScreenNavigator.initialScreen(primaryStage, springContext);
                    } catch (Exception e) {
                        showFallbackError(primaryStage, e);
                    }
                });

            } catch (Exception e) {
                // Spring falhou ao inicializar
                Platform.runLater(() -> {
                    if (e.getMessage().contains("Connection") ||
                            e.getMessage().contains("connect") ||
                            e.getMessage().contains("timeout")) {
                        showNetworkErrorScreen(primaryStage, false);
                    } else {
                        showSpringInitError(primaryStage, e);
                    }
                });
            }
        }).start();
    }

    /**
     * Função que verifica se a conexão com a internet está funcionando
     * @return true para certo e false para sem conexão
     */
    private boolean checkNetworkConnection() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * FUnção que carrega tela de erro de conexão
     * @param stage
     * @param noInternet
     */
    private void showNetworkErrorScreen(Stage stage, boolean noInternet) {
        VBox errorBox = new VBox(20);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(40));

        Label title = new Label(noInternet ? "🌐 Sem Conexão com a Internet" : "🔌 Erro de Conexão com o Servidor");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label message = new Label(
                noInternet
                        ? "Verifique sua conexão com a internet e tente novamente."
                        : "Não foi possível conectar ao servidor. Verifique sua rede ou tente mais tarde."
        );
        message.setWrapText(true);
        message.setMaxWidth(400);

        Button retryButton = new Button("Tentar Novamente");
        retryButton.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white;");
        retryButton.setOnAction(e -> start(stage));

        Button exitButton = new Button("Sair");
        exitButton.setOnAction(e -> Platform.exit());

        HBox buttonBox = new HBox(10, retryButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);

        errorBox.getChildren().addAll(title, message, buttonBox);

        Scene scene = new Scene(errorBox, 500, 350);
        stage.setScene(scene);
    }

    private void showSpringInitError(Stage stage, Exception e) {
        VBox errorBox = new VBox(20);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(40));

        Label title = new Label("⚠️ Erro na Inicialização");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label message = new Label("Não foi possível iniciar o sistema.");
        message.setWrapText(true);

        // Detalhes do erro (opcional, pode ser escondido inicialmente)
        TextArea errorDetails = new TextArea("Erro: " + e.getMessage());
        errorDetails.setEditable(false);
        errorDetails.setVisible(false);
        errorDetails.setPrefHeight(100);

        Button showDetailsButton = new Button("Mostrar Detalhes");
        showDetailsButton.setOnAction(ev -> {
            errorDetails.setVisible(!errorDetails.isVisible());
            showDetailsButton.setText(errorDetails.isVisible() ? "Ocultar Detalhes" : "Mostrar Detalhes");
        });

        Button retryButton = new Button("Tentar Novamente");
        retryButton.setOnAction(ev -> start(stage));

        VBox content = new VBox(10, title, message, showDetailsButton, errorDetails, retryButton);
        content.setAlignment(Pos.CENTER);

        Scene scene = new Scene(content, 500, 400);
        stage.setScene(scene);
    }

    private void showFallbackError(Stage stage, Exception e) {
        // Fallback mais básico se tudo falhar
        Alerts.messageError("Erro ao carregar","Erro ao carregar os dados "+e.getMessage());
        Platform.exit();
    }

    @Override
    public void stop() {
        // Fecha o contexto do Spring quando a aplicação fecha
        if (springContext != null) {
            springContext.close();
        }
    }
}
