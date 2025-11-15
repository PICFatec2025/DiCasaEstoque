package dicasa.estoque.controller.produtos;

import dicasa.estoque.models.dto.ProdutoResponseDTO;
import dicasa.estoque.service.ProdutoService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static dicasa.estoque.util.TableViewUtils.setupColumnString;
import static dicasa.estoque.util.TableViewUtils.tableViewFillHeight;

/**
 * Controller que gerencia a tela de produtos
 */

@Component
public class ProdutoController implements Initializable {
    @FXML
    public Button btNovo;
    @FXML
    public TableView<ProdutoResponseDTO> produtosTableView;
    @FXML
    public TableColumn<ProdutoResponseDTO, String> tableColumnNome;
    @FXML
    public TableColumn<ProdutoResponseDTO, String> tableColumnQuantidade;
    @FXML
    public TableColumn<ProdutoResponseDTO, String> tableColumnPreco;
    @FXML
    public TableColumn<ProdutoResponseDTO, String> tableColumnCriadoEm;
    @FXML
    public TableColumn<ProdutoResponseDTO, ProdutoResponseDTO> tableColumnEdit;
    @FXML
    public TableColumn<ProdutoResponseDTO, ProdutoResponseDTO> tableColumnDelete;
    private final ProdutoService produtoService;

    /**
     * Ele recebe o service pelo Constructor pelo Spring
     * @param produtoService
     */
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void onBtNovoAction(ActionEvent event) {
        System.out.println(btNovo.getText());
    }

    /**
     * Função que executa ao inicializar a tela
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initilizeNodes();
        atualizaTabela();
    }

    /**
     * função que busca os dados no service e insere na tabela da tela
     */

    private void atualizaTabela() {
        List<ProdutoResponseDTO> produtos = produtoService.findAll();
        ObservableList<ProdutoResponseDTO> obervableList = FXCollections.observableList(produtos);
        produtosTableView.setItems(obervableList);
        initEditButton();
        initDeleteButton();
    }

    /**
     * Função que carrega os campos da tabela
     * Além de fazer com que a tabela preencha todo a altura da tela
     */
    private void initilizeNodes() {
        setupColumnString(tableColumnNome, ProdutoResponseDTO::nome);
        setupColumnString(tableColumnQuantidade, ProdutoResponseDTO::quantidade);
        setupColumnString(tableColumnPreco, ProdutoResponseDTO::preco);
        setupColumnString(tableColumnCriadoEm, ProdutoResponseDTO::created_at);
        tableViewFillHeight(produtosTableView);
    }

    /**
     * Função que gera o botão de delete em cada produto
     */

    @FXML
    public void initDeleteButton() {
        tableColumnDelete.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tableColumnDelete.setCellFactory(
                param -> new TableCell<ProdutoResponseDTO, ProdutoResponseDTO>() {
                    private final Button button = new Button("Deletar");

                    @Override
                    protected void updateItem(ProdutoResponseDTO dto, boolean b) {
                        super.updateItem(dto, b);
                        if (dto == null) {
                            setGraphic(null);
                            return;
                        }
                        setGraphic(button);
                        button.setOnAction(
                                event -> System.out.println("Deletar")
                        );
                    }
                }
        );
    }
    /**
     * Função que gera o botão de edit em cada produto
     */

    @FXML
    public void initEditButton() {
        tableColumnEdit.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tableColumnEdit.setCellFactory(
                param -> new TableCell<ProdutoResponseDTO, ProdutoResponseDTO>() {
                    private final Button button = new Button("Editar");

                    @Override
                    protected void updateItem(ProdutoResponseDTO dto, boolean b) {
                        super.updateItem(dto, b);
                        if (dto == null) {
                            setGraphic(null);
                            return;
                        }
                        setGraphic(button);
                        button.setOnAction(
                                event -> System.out.println("Editar")
                        );
                    }
                }
        );
    }
}
