package dicasa.estoque;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Classe de execução do JavaFX
 */
public class AppLauncher extends Application {

    private ConfigurableApplicationContext springContext;

    /**
     * Carrega o Spring Boot antes de inicializar o JavaFX
     */
    @Override
    public void init(){
        springContext = SpringApplication.run(EstoqueApplication.class);
    }

    /**
     * Carrega a tela inicial, a tela de Login
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();

        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        // 🔒 Impede redimensionamento (sem tela cheia)
        primaryStage.setResizable(false);

        try {

            // Configura exception handler global
            Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
                System.err.println("Exception não tratada: " + throwable.getMessage());
                throwable.printStackTrace();
            });

            // Chama o initialScreen passando o primaryStage
            ScreenNavigator.initialScreen(primaryStage, springContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ao fechar o projeto, finaliza o Spring Boot
     */
    @Override
    public void stop(){
        springContext.stop();
    }

    public static void main(String[] args) {
        launch(args);


    }
}
