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
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        ScreenNavigator.initialScreen(stage,springContext);
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
