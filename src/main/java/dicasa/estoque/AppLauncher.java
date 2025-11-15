package dicasa.estoque;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class AppLauncher extends Application {
    private ConfigurableApplicationContext springContext;
    @Override
    public void init(){
        springContext = SpringApplication.run(EstoqueApplication.class);
    }
    @Override
    public void start(Stage stage) throws Exception {
        ScreenNavigator.initialScreen(stage,springContext);
    }
    @Override
    public void stop(){
        springContext.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
