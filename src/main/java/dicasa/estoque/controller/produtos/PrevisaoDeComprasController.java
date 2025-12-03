package dicasa.estoque.controller.produtos;

import dicasa.estoque.csv.CSVPrevisaoComprasExporter;
import dicasa.estoque.models.dto.PrevisaoCompraDTO;
import dicasa.estoque.service.EstoqueService;
import dicasa.estoque.service.FornecedorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller para a tela de Previsão de Compras
 * Exibe produtos com estoque baixo e sugere compras
 */
@Component
public class PrevisaoDeComprasController implements Initializable {

    private final EstoqueService estoqueService;
    private final FornecedorService fornecedorService;
    private final CSVPrevisaoComprasExporter csvExporter;

    // Componentes FXML
    @FXML private ComboBox<String> cbUrgencia;
    @FXML private TableView<PrevisaoCompraDTO> tabelaProdutos;
    @FXML private TableView<?> tabelaFornecedores;
    @FXML private TextField txtFiltro;

    // Lista de dados
    private ObservableList<PrevisaoCompraDTO> produtosData = FXCollections.observableArrayList();
    private ObservableList<String> niveisUrgencia = FXCollections.observableArrayList("TODOS", "CRÍTICO", "ALTO", "MÉDIO", "BAIXO");

    public PrevisaoDeComprasController(EstoqueService estoqueService,
                                       FornecedorService fornecedorService,
                                       CSVPrevisaoComprasExporter csvExporter) {
        this.estoqueService = estoqueService;
        this.fornecedorService = fornecedorService;
        this.csvExporter = csvExporter;
    }

    /**
     * Inicializa a tela com dados dos produtos e configurações
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarCombobox();
        carregarDados();
        configurarTabelas();
    }

    /**
     * Configura os ComboBox com opções de filtro
     */
    private void configurarCombobox() {
        // Configura ComboBox de Urgência
        cbUrgencia.setItems(niveisUrgencia);
        cbUrgencia.setValue("TODOS");

        // Listeners para filtros automáticos
        cbUrgencia.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        txtFiltro.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
    }

    /**
     * Configura as colunas das tabelas
     */
    private void configurarTabelas() {
        // A configuração das colunas será feita via FXML
        tabelaProdutos.setItems(produtosData);
    }

    /**
     * Carrega os dados dos produtos do serviço de estoque
     */
    private void carregarDados() {
        try {
            var estoques = estoqueService.listarEstoques();

            List<PrevisaoCompraDTO> previsoes = estoques.stream()
                    .map(estoque -> {
                        Integer quantidadeComprar = PrevisaoCompraDTO.calcularQuantidadeComprar(
                                estoque.quantidade(),
                                estoque.quantidadeMinima()
                        );

                        String urgencia = PrevisaoCompraDTO.determinarUrgencia(
                                estoque.quantidade(),
                                estoque.quantidadeMinima()
                        );

                        return new PrevisaoCompraDTO(
                                estoque.idProduto(),
                                estoque.nome(),
                                estoque.tipo(),
                                estoque.quantidade(),
                                estoque.quantidadeMinima(),
                                quantidadeComprar,
                                urgencia,
                                "Fornecedores disponíveis" // Placeholder - pode ser expandido
                        );
                    })
                    .filter(previsao -> previsao.getQuantidadeComprar() > 0)
                    .collect(Collectors.toList());

            produtosData.setAll(previsoes);

        } catch (Exception e) {
            exibirAlertaErro("Erro ao carregar dados", "Não foi possível carregar os dados de previsão de compras.");
        }
    }

    /**
     * Aplica os filtros selecionados na tabela
     */
    private void aplicarFiltros() {
        String filtroTexto = txtFiltro.getText().toLowerCase();
        String urgenciaSelecionada = cbUrgencia.getValue();

        List<PrevisaoCompraDTO> filtrados = produtosData.stream()
                .filter(produto ->
                        (filtroTexto.isEmpty() || produto.getNomeProduto().toLowerCase().contains(filtroTexto)) &&
                                ("TODOS".equals(urgenciaSelecionada) || produto.getNivelUrgencia().equals(urgenciaSelecionada))
                )
                .collect(Collectors.toList());

        tabelaProdutos.setItems(FXCollections.observableArrayList(filtrados));
    }

    /**
     * Atualiza os dados da tela (chamado pelo botão Atualizar)
     */
    @FXML
    void atualizarDados(ActionEvent event) {
        carregarDados();
        exibirMensagemSucesso("Dados atualizados com sucesso!");
    }

    /**
     * Gera lista de compras (funcionalidade básica)
     */
    @FXML
    void gerarListaCompras(ActionEvent event) {
        List<PrevisaoCompraDTO> produtosSelecionados = tabelaProdutos.getItems();

        if (produtosSelecionados.isEmpty()) {
            exibirAlertaInformacao("Lista Vazia", "Não há produtos para comprar no momento.");
            return;
        }

        StringBuilder listaCompras = new StringBuilder("LISTA DE COMPRAS SUGERIDA:\n\n");
        for (PrevisaoCompraDTO produto : produtosSelecionados) {
            listaCompras.append(String.format("- %s: %d unidades\n",
                    produto.getNomeProduto(), produto.getQuantidadeComprar()));
        }

        exibirAlertaInformacao("Lista de Compras Gerada", listaCompras.toString());
    }

    /**
     * Exporta os dados da tabela para CSV
     */
    @FXML
    void exportarParaCSV(ActionEvent event) {
        List<PrevisaoCompraDTO> produtosParaExportar = tabelaProdutos.getItems();

        if (produtosParaExportar.isEmpty()) {
            exibirAlertaInformacao("Nenhum dado", "Não há dados para exportar.");
            return;
        }

        String resultado = csvExporter.exportarPrevisaoComprasCSV(produtosParaExportar);
        exibirAlertaInformacao("Exportação CSV", resultado);
    }

    /**
     * Limpa todos os filtros aplicados
     */
    @FXML
    void limparFiltros(ActionEvent event) {
        txtFiltro.clear();
        cbUrgencia.setValue("TODOS");
        // Os listeners irão automaticamente aplicar os filtros "limpos"
    }

    // Métodos auxiliares para exibição de alertas
    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirAlertaInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirMensagemSucesso(String mensagem) {
        // Pode ser implementado com uma Label na interface se preferir
        System.out.println(mensagem);
    }
}