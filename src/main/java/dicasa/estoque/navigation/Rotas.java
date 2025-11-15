package dicasa.estoque.navigation;

/**
 * Classe que armazena as rotas, e assim evita erro ao digitá-las
 */
public class Rotas {
        private static final String BASE_PATH = "/view/";
        private static String rota(String rota){
            return BASE_PATH+rota;
        }
        public static final String MAIN_VIEW = rota("main/main-view.fxml");
        public static final String LOGIN_VIEW = rota("login/login-view.fxml");
        public static final String SOBRE_VIEW = rota("sobre/sobre.fxml");
        public static final String PRODUTOS_VIEW = rota("produtos/produtos-view.fxml");
        public static final String ESQUECI_SENHA = rota("login/esqueci-senha-view.fxml");
        public static final String PERFIL_VIEW = rota("perfil/perfil-view.fxml");
        public static final String NOVO_USUARIO = rota("perfil/novo-usuario-view.fxml");
        public static final String FORNECEDORES_VIEW = rota("fornecedores/fornecedores-view.fxml");
}
