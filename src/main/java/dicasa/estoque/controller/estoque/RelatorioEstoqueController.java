package dicasa.estoque.controller.estoque;

import dicasa.estoque.models.dto.EstoqueProdutoCompletoResponseDTO;
import dicasa.estoque.navigation.Rotas;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.EstoqueService;
import dicasa.estoque.util.Alerts;
import dicasa.estoque.util.EstoqueProdutoSavedEvent;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static dicasa.estoque.util.TableViewUtils.*;


/**
 * Classe que gerencia o carregamento da tela de relatório de estoque do produto
 * Ele permite a visualização dos estoques e seus níveis
 * Permite exportar essa lista completa para um arquivo CSV
 * E ainda permite levar para uma janela que edita cada estoque individualmente
 */
@Component
public class RelatorioEstoqueController implements Initializable {
    @FXML
    public TextField textFieldBusca;
    @FXML
    private Button onClickButtonExportar;
    @FXML
    private TableView<EstoqueProdutoCompletoResponseDTO> estoqueTableView;
    @FXML
    private TableColumn<EstoqueProdutoCompletoResponseDTO,String> tableColumnNome;
    @FXML
    private TableColumn<EstoqueProdutoCompletoResponseDTO,String> tableColumnTipo;
    @FXML
    private TableColumn<EstoqueProdutoCompletoResponseDTO,Integer> tableColumnQuantidade;
    @FXML
    private TableColumn<EstoqueProdutoCompletoResponseDTO, Integer> tableColumnQuantidadeMinima;
    @FXML
    private TableColumn<EstoqueProdutoCompletoResponseDTO,String> tableColumnStatus;
    @FXML
    private TableColumn<EstoqueProdutoCompletoResponseDTO, EstoqueProdutoCompletoResponseDTO> tableColumnEditar;

    private final EstoqueService estoqueService;

    public RelatorioEstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    /**
     * Função que roda ao inicializar a tela
     * Ele prepara a tabela e carrega os itens do banco de dados
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
        Platform.runLater(this::updateEstoqueTableView);
        configurarBuscaComEnter();
    }

    /**
     * Função que configura a tabela para receber os dados do banco de dados
     */
    private void initializeNodes(){
        setupColumnString(tableColumnNome, EstoqueProdutoCompletoResponseDTO::nome);
        setupColumnString(tableColumnTipo, EstoqueProdutoCompletoResponseDTO::tipo);
        setupColumnInteger(tableColumnQuantidade, EstoqueProdutoCompletoResponseDTO::quantidade);
        setupColumnInteger(tableColumnQuantidadeMinima, EstoqueProdutoCompletoResponseDTO::quantidadeMinima);
        setupColumnString(tableColumnStatus, EstoqueProdutoCompletoResponseDTO::statusTexto);
        tableViewFillWidth(estoqueTableView);
    }

    /**
     * Função que busca a lista de produtos com estoque no service e atualiza a lista
     */
    public void updateEstoqueTableView(){
        List<EstoqueProdutoCompletoResponseDTO> produtos = estoqueService.listarEstoques();
        ObservableList<EstoqueProdutoCompletoResponseDTO> observableList =
                FXCollections.observableList(produtos);
        estoqueTableView.setItems(observableList);
        initEditButton();
    }

    /**
     * Função que gerencia o clique do botão de editar em cada Produto
     * Ele leva para uma janela que permite editar o estoque dos produtos
     */
    @FXML
    public void initEditButton(){
        tableColumnEditar.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        tableColumnEditar.setCellFactory(
                param ->
                        new TableCell<EstoqueProdutoCompletoResponseDTO, EstoqueProdutoCompletoResponseDTO>(){
                    private final Button button = new Button("Editar");
                    @Override
                    protected void updateItem(EstoqueProdutoCompletoResponseDTO produto, boolean b) {
                        super.updateItem(produto, b);
                        if (produto == null) {
                            setGraphic(null);
                            return;
                        }
                        setGraphic(button);
                        button.setOnAction(
                                event -> ScreenNavigator.loadWindow(
                                        ScreenNavigator.currentStage(event),
                                        Rotas.EDIT_ESTOQUE_FORM,
                                        "Edite os dados do estoque",
                                        produto
                                )
                        );
                    }
                }
        );
    }

    /**
     * Função que faz a ação ao clicar no botão de exportar a lista de produtos com estoque para o CSV
     * @param event event da tela
     */
    @FXML
    public void onClickButtonExportar(ActionEvent event) {
        String mensagem = estoqueService.exportarEstoquesEmCSV();
        AlertType tipoAlerta;
        if (!mensagem.contains("✅ CSV gerado em:")) {
            tipoAlerta = AlertType.ERROR;
        } else {
            tipoAlerta = AlertType.CONFIRMATION;
        }
        Alerts.showAlerts(
                "Exportar estoque para CSV",
                null,
                mensagem,
                tipoAlerta
        );
    }

    /**
     * Ao ser avisado sobre a alteração da tabela, ele vai atualizar a tabela com os valores novos
     * @param event evento de EstoqueProduto alterado
     */
    @EventListener
    public void handleEstoqueProdutoEditado(EstoqueProdutoSavedEvent event){
        Platform.runLater(this::updateEstoqueTableView);
    }

    /**
     * Função que faz a busca de produtos pelo nome, alterando a lista mostrado na tela
     * @param mouseEvent
     */
    @FXML
    public void onClickButtonBuscar(MouseEvent mouseEvent) {
        String nome = textFieldBusca.getText().trim();
        if (nome.isEmpty()) {
            updateEstoqueTableView(); // Se estiver vazio, recarrega todos
        } else {
            buscarProdutosPorNome(nome);
        }
    }

    /**
     * Função que carrega a lista de produtos pelo nome
     * @param nome do produto
     */
    private void buscarProdutosPorNome(String nome) {
        try {
            List<EstoqueProdutoCompletoResponseDTO> produtos = estoqueService.listarEstoquePorNome(nome);
            ObservableList<EstoqueProdutoCompletoResponseDTO> observableList = FXCollections.observableList(produtos);
            estoqueTableView.setItems(observableList);
            initEditButton(); // Re-inicializa os botões de edição para os novos itens
        } catch (Exception e) {
            Alerts.messageError("Erro na busca","Ocorreu um erro ao buscar os produtos.");
        }
    }

    /**
     * Função que permite que busca seja feita se for pressionado o botão enter
     */
    private void configurarBuscaComEnter() {
        textFieldBusca.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Dispara a busca quando Enter for pressionado
                String termoBusca = textFieldBusca.getText().trim();

                if (termoBusca.isEmpty()) {
                    updateEstoqueTableView();
                } else {
                    buscarProdutosPorNome(termoBusca);
                }
            }
        });
    }
}

