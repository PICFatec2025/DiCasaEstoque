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
        public static final String INICIAL_VIEW = rota("main/inicial-view.fxml");
        public static final String LOGIN_VIEW = rota("login/login-view.fxml");
        public static final String PRODUTOS_VIEW = rota("produtos/produtos-view.fxml");
        public static final String ESQUECI_SENHA = rota("login/esqueci-senha-view.fxml");
        public static final String PERFIL_VIEW = rota("perfil/perfil-view.fxml");
        public static final String NOVO_USUARIO = rota("perfil/novo-usuario-view.fxml");
        public static final String FORNECEDORES_VIEW = rota("fornecedores/fornecedores-view.fxml");
        public static final String RELATORIO_CONSUMO_VIEW = rota("estoque/historico-estoque-view.fxml");
        public static final String HISTORICO_ESTOQUE_VIEW = rota("estoque/relatorio_estoque-view.fxml");
        public static final String CADASTRO_PRATOS_VIEW = rota("pratos/cadastro-prato-view.fxml");
        public static final String RELATORIO_PRATOS_VIEW = rota("pratos/relatorio-pratos-view.fxml");
        public static final String PREVISAO_COMPRAS_VIEW = rota("produtos/previsao_compras-view.fxml");
        public static final String CONTROLE_QUALIDADE_VIEW = rota("produtos/controle-qualidade-view.fxml");
    
}
