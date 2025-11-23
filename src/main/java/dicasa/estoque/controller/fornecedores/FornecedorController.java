package dicasa.estoque.controller.fornecedores;

import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.FornecedorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@Component
public class FornecedorController implements Initializable {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cnpjField;
    @FXML
    private TextField enderecoField;
    @FXML
    private TextField telefoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField contatoField;
    @FXML
    private TableView<?> tabelaProdutos;
    @FXML
    private TableColumn<?, ?> produtoCol;
    @FXML
    private TableColumn<?, ?> precoCol;
    @FXML
    private TableColumn<?, ?> qtdCol;
    @FXML
    private TableColumn<?, ?> totalCol;
    @FXML
    private Button btnAddProduto;

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fornecedorService.listarFornecedoresCompleto();
    }

    @FXML
    private void adicionarProduto() {
        // Lógica para adicionar produto à tabela
        System.out.println("Produto adicionado!");
    }

    @FXML
    private void salvarFornecedor() {
        // Lógica para salvar fornecedor
        System.out.println("Fornecedor salvo:");
        System.out.println("Nome: " + nomeField.getText());
        System.out.println("CNPJ: " + cnpjField.getText());
        System.out.println("Endereço: " + enderecoField.getText());
        System.out.println("Telefone: " + telefoneField.getText());
        System.out.println("Email: " + emailField.getText());
        System.out.println("Contato: " + contatoField.getText());
    }

    public void onClickCadastrarFornecedor(ActionEvent event) {

    }

    public void onClickBuscar(ActionEvent event) {
    }

    public void onClickLimparFiltros(ActionEvent event) {

    }
    public void onClickAdicionarProduto(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/produtos/adicionar-produto.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adicionar Produto");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void onClickExportar(ActionEvent event) {

    }



}
