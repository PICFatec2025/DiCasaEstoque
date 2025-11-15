package dicasa.estoque.controller;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static dicasa.estoque.navigation.Rotas.PRODUTOS_VIEW;
import static dicasa.estoque.navigation.Rotas.SOBRE_VIEW;

/**
 * Controller que gerencia a Tela Principal
 */

@Component
public class MainController implements Initializable {
    @FXML
    public MenuItem menuItemSaidaProdutos;
    @FXML
    public MenuItem menuItemEntradaProdutos;
    @FXML
    public MenuItem menuItemFornecedores;
    @FXML
    public MenuItem menuItemLotes;
    @FXML
    private MenuItem menuItemProdutos;
    @FXML
    private MenuItem menuItemInventario;
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
    public void onMenuItemInventarioClick() {
        System.out.println(menuItemInventario.getText());
    }

    @FXML
    public void onMenuItemSobreClick() {
        ScreenNavigator.loadView(contentContainer, SOBRE_VIEW);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onMenuItemSaidaProdutosClick(ActionEvent event) {
        System.out.println(menuItemSaidaProdutos.getText());
    }

    public void onMenuItemEntradaProdutosClick(ActionEvent event) {
        System.out.println(menuItemEntradaProdutos.getText());
    }

    public void onMenuItemFornecedoresClick(ActionEvent event) {
        System.out.println(menuItemFornecedores.getText());
    }

    public void onMenuItemLotesClick(ActionEvent event) {
        System.out.println(menuItemLotes.getText());
    }
}
