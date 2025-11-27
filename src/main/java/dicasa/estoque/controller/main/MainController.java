package dicasa.estoque.controller.main;

import dicasa.estoque.models.entities.EstoqueProduto;
import dicasa.estoque.models.entities.Produto;
import dicasa.estoque.models.view.AvisoEstoqueView;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.EstoqueService;
import dicasa.estoque.service.ProdutoService;
import dicasa.estoque.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static dicasa.estoque.util.Alerts.messageError;

import static dicasa.estoque.navigation.Rotas.*;

/**
 * Controller que gerencia a Tela Principal
 */

@Component
public class MainController implements Initializable {

    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    private final ObservableList<Produto> produtosDisponiveis = FXCollections.observableArrayList();
    private final ObservableList<AvisoEstoqueView> avisosEstoque = FXCollections.observableArrayList();
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
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox rootContainer;
    @FXML
    private HBox menuRow;

    @FXML
    private ComboBox<Produto> choiceProduto;
    @FXML
    private TextField txtQuantidadeSaida;
    @FXML
    private Label lblEstoqueDisponivel;
    @FXML
    private Label lblMensagemSaida;
    @FXML
    private TableView<AvisoEstoqueView> tabelaAvisos;
    @FXML
    private TableColumn<AvisoEstoqueView, String> colunaData;
    @FXML
    private TableColumn<AvisoEstoqueView, String> colunaHora;
    @FXML
    private TableColumn<AvisoEstoqueView, String> colunaAviso;
    @FXML
    private TableColumn<AvisoEstoqueView, String> colunaQtdMinimo;
    @FXML
    private TableColumn<AvisoEstoqueView, String> colunaPrioridade;

    public MainController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    /**
     * ao clicar no botão de produtos, vai para a tela de produtos
     */
    @FXML
    public void onMenuItemProdutosClick() {
        ScreenNavigator.loadView(contentContainer,PRODUTOS_VIEW);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarScrollPane();
        inicializarSecaoSaidaProdutos();
        configurarTabelaAvisos();
        carregarAvisosEstoque();
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
    public void onMenuItemInicioClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,INICIAL_VIEW);
    }
    @FXML
    public void onMenuItemFornecedoresClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,FORNECEDORES_VIEW);
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
    public void onButtonInicioClick(ActionEvent event) {
        ScreenNavigator.loadView(contentContainer,INICIAL_VIEW);
    }

    private void configurarScrollPane() {
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                if (rootContainer != null) {
                    rootContainer.setPrefWidth(newBounds.getWidth());
                    rootContainer.setPrefHeight(newBounds.getHeight());
                }
                if (contentContainer != null) {
                    contentContainer.setPrefWidth(newBounds.getWidth());
                    double menuHeight = menuRow != null ? menuRow.getHeight() : 0;
                    double spacing = rootContainer != null ? rootContainer.getSpacing() : 0;
                    double availableHeight = Math.max(0, newBounds.getHeight() - menuHeight - spacing);
                    contentContainer.setPrefHeight(availableHeight);
                }
            });
        }
    }

    private void configurarTabelaAvisos() {
        if (tabelaAvisos == null) {
            return;
        }

        tabelaAvisos.setPlaceholder(new Label("Nenhum produto abaixo da quantidade mínima."));
        colunaData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        colunaHora.setCellValueFactory(cellData -> cellData.getValue().horaProperty());
        colunaAviso.setCellValueFactory(cellData -> cellData.getValue().avisoProperty());
        colunaQtdMinimo.setCellValueFactory(cellData -> cellData.getValue().quantidadeAteMinimoProperty());
        colunaPrioridade.setCellValueFactory(cellData -> cellData.getValue().prioridadeProperty());
        tabelaAvisos.setItems(avisosEstoque);
    }

    private void inicializarSecaoSaidaProdutos() {
        if (choiceProduto == null) {
            return;
        }

        configurarComboBoxProduto();
        carregarProdutosSaida();
        if (lblMensagemSaida != null) {
            lblMensagemSaida.setText("");
        }
    }

    private void carregarAvisosEstoque() {
        if (tabelaAvisos == null) {
            return;
        }

        avisosEstoque.clear();

        produtoService.buscarTodos().stream()
                .filter(produto -> produto.getEstoqueProduto() != null)
                .filter(produto -> produto.getEstoqueProduto().getQuantidade() < produto.getEstoqueProduto().getQuantidadeMinima())
                .forEach(produto -> {
                    EstoqueProduto estoque = produto.getEstoqueProduto();
                    LocalDateTime referencia = estoque.getData_atualizacao() != null ? estoque.getData_atualizacao() : estoque.getData_criacao();
                    LocalDateTime dataHora = referencia != null ? referencia : LocalDateTime.now();

                    String prioridade = estoque.getQuantidade() < estoque.getEstoqueEmergencial() ? "Emergencial" : "Mínima";
                    int qtd =  estoque.getQuantidadeMinima() - estoque.getQuantidade() ;
                    String quantidadeAteMinimo = String.format("%d / %d", qtd, estoque.getQuantidadeMinima());
                    String mensagem = String.format(
                            "%s com %d unidade(s) abaixo do mínimo (%d)",
                            produto.getNome(),
                            qtd,
                            estoque.getQuantidadeMinima()
                    );

                    avisosEstoque.add(new AvisoEstoqueView(
                            dataHora.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            dataHora.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            mensagem,
                            quantidadeAteMinimo,
                            prioridade
                    ));
                });
    }

    private void configurarComboBoxProduto() {
        choiceProduto.setItems(produtosDisponiveis);
        choiceProduto.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Produto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        choiceProduto.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Produto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Selecione um produto" : item.getNome());
            }
        });
    }

    private void carregarProdutosSaida() {
        if (choiceProduto == null) {
            return;
        }

        Long produtoSelecionadoId = choiceProduto.getValue() != null ? choiceProduto.getValue().getIdProduto() : null;

        produtosDisponiveis.setAll(produtoService.buscarTodos());

        if (produtosDisponiveis.isEmpty()) {
            choiceProduto.setValue(null);
            atualizarEstoqueDisponivel(null);
            return;
        }

        Produto produtoParaSelecionar = produtosDisponiveis.stream()
                .filter(produto -> produtoSelecionadoId != null && produtoSelecionadoId.equals(produto.getIdProduto()))
                .findFirst()
                .orElse(produtosDisponiveis.get(0));

        choiceProduto.setValue(produtoParaSelecionar);
        atualizarEstoqueDisponivel(produtoParaSelecionar);
    }

    @FXML
    public void onProdutoSelecionado() {
        atualizarEstoqueDisponivel(choiceProduto != null ? choiceProduto.getValue() : null);
        if (lblMensagemSaida != null) {
            lblMensagemSaida.setText("");
        }
    }

    private void atualizarEstoqueDisponivel(Produto produto) {
        if (lblEstoqueDisponivel == null) {
            return;
        }

        if (produto == null || produto.getEstoqueProduto() == null) {
            lblEstoqueDisponivel.setText("Estoque não disponível");
            return;
        }

        lblEstoqueDisponivel.setText("Disponível: " + produto.getEstoqueProduto().getQuantidade());
    }

    @FXML
    public void onRetirarProduto(ActionEvent event) {
        if (choiceProduto == null) {
            return;
        }

        Produto produtoSelecionado = choiceProduto.getValue();
        if (produtoSelecionado == null) {
            messageError("Produto não selecionado", "Selecione um produto para registrar a saída.");
            return;
        }

        int quantidade;
        try {
            String quantidadeTexto = txtQuantidadeSaida != null ? txtQuantidadeSaida.getText().trim() : "";
            quantidade = Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException e) {
            messageError("Quantidade inválida", "Informe um número válido para a quantidade a ser retirada.");
            return;
        }

        if (quantidade <= 0) {
            messageError("Quantidade inválida", "A quantidade deve ser maior que zero.");
            return;
        }

        EstoqueProduto estoqueProduto = produtoSelecionado.getEstoqueProduto();
        if (estoqueProduto == null) {
            messageError("Estoque não encontrado", "O produto selecionado não possui estoque cadastrado.");
            return;
        }

        if (quantidade > estoqueProduto.getQuantidade()) {
            messageError("Quantidade excede estoque", "A quantidade informada é maior que o estoque disponível.");
            return;
        }

        boolean confirmada = Alerts.showConfirmation(
                "Confirmar retirada",
                "Deseja retirar " + quantidade + " unidade(s) de " + produtoSelecionado.getNome() + "?",
                javafx.scene.control.Alert.AlertType.CONFIRMATION
        );

        if (!confirmada) {
            return;
        }

        try {
            EstoqueProduto estoqueAtualizado = estoqueService.retirarDoEstoque(produtoSelecionado.getIdProduto(), quantidade);
            produtoSelecionado.setEstoqueProduto(estoqueAtualizado);
            atualizarEstoqueDisponivel(produtoSelecionado);
            if (txtQuantidadeSaida != null) {
                txtQuantidadeSaida.clear();
            }
            carregarProdutosSaida();
            carregarAvisosEstoque();
            if (lblMensagemSaida != null) {
                lblMensagemSaida.setText("Saída registrada com sucesso!");
            }
        } catch (Exception e) {
            messageError("Erro ao registrar saída", e.getMessage());
        }
    }
}