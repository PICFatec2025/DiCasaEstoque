package dicasa.estoque.controller.main;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static dicasa.estoque.navigation.Rotas.*;

/**
 * Controller que gerencia a Tela Principal
 */

@Component
public class MainController implements Initializable {
    @FXML
    public MenuItem menuItemPrevisaoCompras;
    @FXML
    public MenuItem menuItemHistoricoEstoque;
    @FXML
    public MenuItem menuItemFornecedores;
    @FXML
    public MenuItem menuItemControleQualidade;
    @FXML
    public MenuItem menuItemPefil;
    @FXML
    public MenuItem menuItemNovoUsuario;
    @FXML
    public MenuItem menuItemSair;
    @FXML
    public MenuItem menuItemRelatorioConsumo;
    @FXML
    public MenuItem menuItemPratos;
    @FXML
    public MenuItem menuItemRelatorioPratos;
    @FXML
    public MenuItem menuItemRelatorioEstoque;
    @FXML
    private MenuItem menuItemProdutos;

    @FXML
    private MenuItem menuItemSobre;
    @FXML
    private AnchorPane contentContainer;

    /**
     * ao clicar no botão de produtos, vai para a tela de produtos
     */
    @FXML
    public void onMenuItemProdutosClick() {
        ScreenNavigator.loadView(contentContainer,PRODUTOS_VIEW);
    }


    @FXML
    public void onMenuItemSobreClick() {
        ScreenNavigator.loadView(contentContainer, SOBRE_VIEW);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onMenuItemPrevisaoComprasClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,PREVISAO_COMPRAS_VIEW);
    }

    @FXML
    public void onMenuItemHistoricoEstoqueClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer, HISTORICO_ESTOQUE_VIEW);
    }

    @FXML
    public void onMenuItemFornecedoresClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,FORNECEDORES_VIEW);
    }

    @FXML
    public void onMenuItemControleQualidadeClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,CONTROLE_QUALIDADE_VIEW);
    }

    @FXML
    public void onMenuItemPerfilClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,PERFIL_VIEW);
    }

    @FXML
    public void onMenuItemNovoUsuarioClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,NOVO_USUARIO);
    }
    @FXML
    public void onMenuItemSairClick(ActionEvent event) {
        ScreenNavigator.loadLoginView(LOGIN_VIEW,event);
    }
    @FXML
    public void onMenuItemRelatorioConsumoClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,RELATORIO_CONSUMO_VIEW);
    }
    @FXML
    public void onMenuItemPratosClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,CADASTRO_PRATOS_VIEW);
    }
    @FXML
    public void onMenuItemRelatorioPratosClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,RELATORIO_PRATOS_VIEW);
    }

}
