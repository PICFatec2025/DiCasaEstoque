package dicasa.estoque.navigation;

public class Rotas {
        private static final String BASE_PATH = "/view/";
        private static String rota(String rota){
            return BASE_PATH+rota;
        }
        public static final String MAIN_VIEW = rota("main-view.fxml");
        public static final String LOGIN_VIEW = rota("login-view.fxml");
        public static final String SOBRE_VIEW = rota("sobre.fxml");
}
