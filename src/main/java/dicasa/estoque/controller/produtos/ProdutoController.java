package dicasa.estoque.controller.produtos;

import dicasa.estoque.models.entities.Produto;
import dicasa.estoque.navigation.Rotas;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.ProdutoService;
import dicasa.estoque.util.TableViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProdutoController {

    private final ProdutoService produtoService;

    private final ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @FXML private TextField txtBusca;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Long> colunaId;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, String> colunaMarca;
    @FXML private TableColumn<Produto, String> colunaTipo;
    @FXML private Label lblMensagem;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Configura as colunas da tabela e carrega os produtos ao iniciar a tela.
     */
    @FXML
    public void initialize() {
        TableViewUtils.setupColumnLong(colunaId, Produto::getIdProduto);
        TableViewUtils.setupColumnString(colunaNome, Produto::getNome);
        TableViewUtils.setupColumnString(colunaMarca, Produto::getMarca);
        TableViewUtils.setupColumnString(colunaTipo, Produto::getTipo);
        TableViewUtils.tableViewFillHeight(tabelaProdutos);
        TableViewUtils.tableViewFillWidth(tabelaProdutos);

        carregarProdutos();
    }

    /**
     * Abre a janela de cadastro para criar um novo produto.
     */
    @FXML
    private void abrirCadastroProduto() {
        Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
        ScreenNavigator.loadWindow(stage, Rotas.ADICIONAR_PRODUTO_VIEW, "Adicionar Produto", null);
        carregarProdutos();
    }

    /**
     * Abre os detalhes do produto quando o usuário realiza um duplo clique.
     */
    @FXML
    private void handleTabelaProdutosClique(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY || event.getClickCount() < 2) {
            return;
        }
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            return;
        }
        Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
        ScreenNavigator.loadWindow(stage, Rotas.GERENCIAR_PRODUTO_VIEW, "Detalhes do Produto", produtoSelecionado);
        carregarProdutos();
    }

    /**
     * Executa uma busca pelo nome informado no campo de texto.
     */
    @FXML
    private void buscarPorNome() {
        String nomeBusca = txtBusca.getText().trim();
        if (nomeBusca.isEmpty()) {
            carregarTodosProdutos();
            return;
        }

        List<Produto> resultados = produtoService.buscarPorNomeParcial(nomeBusca);

        listaProdutos.setAll(resultados);
        tabelaProdutos.setItems(listaProdutos);

        if (resultados.isEmpty()) {
            lblMensagem.setText("Nenhum produto encontrado para '" + nomeBusca + "'.");
        } else {
            lblMensagem.setText(resultados.size() + " produto(s) encontrado(s) para '" + nomeBusca + "'.");
        }
    }

    /**
     * Limpa filtros e recupera a lista completa de produtos.
     */
    @FXML
    private void carregarTodosProdutos() {
        txtBusca.clear();
        carregarProdutos();
        lblMensagem.setText("Lista completa carregada!");
    }

    /**
     * Atualiza a tabela com todos os produtos disponíveis.
     */
    private void carregarProdutos() {
        listaProdutos.clear();
        List<Produto> todosProdutos = produtoService.buscarTodos();
        listaProdutos.addAll(todosProdutos);
        tabelaProdutos.setItems(listaProdutos);

        lblMensagem.setText("Total: " + todosProdutos.size() + " produto(s)");
    }
}
