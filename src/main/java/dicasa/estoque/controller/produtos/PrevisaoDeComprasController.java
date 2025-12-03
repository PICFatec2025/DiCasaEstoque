package dicasa.estoque.controller.produtos;

import dicasa.estoque.csv.CSVPrevisaoComprasExporter;
import dicasa.estoque.models.dto.PrevisaoCompraDTO;
import dicasa.estoque.service.EstoqueService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller responsável pela tela de Previsão de Compras.
 *
 * Aqui acontece toda a lógica de exibição dos produtos que precisam ser comprados,
 * junto da aplicação dos filtros, geração de lista e exportação para CSV.
 *
 * PRINCIPAIS CONCEITOS DE POO UTILIZADOS:
 * - INJEÇÃO DE DEPENDÊNCIA (via construtor - Spring)
 * - ENCAPSULAMENTO (componentes @FXML privados)
 * - RESPONSABILIDADE ÚNICA (cada método executa uma tarefa específica)
 * - POLIMORFISMO (uso de ObservableList e FilteredList)
 * - OBSERVER PATTERN (listeners nos campos de filtro)
 * - COMPOSIÇÃO (uso de DTOs, Services, Exporter)
 */
@Component
public class PrevisaoDeComprasController implements Initializable {

    private final EstoqueService estoqueService;
    private final CSVPrevisaoComprasExporter csvExporter;

    // Componentes FXML
    @FXML private ComboBox<String> cbUrgencia;
    @FXML private TableView<PrevisaoCompraDTO> tabelaProdutos;
    @FXML private TextField txtFiltro;
    @FXML private Label lblContador;

    // Dados exibidos na tabela
    private ObservableList<PrevisaoCompraDTO> produtosData = FXCollections.observableArrayList();
    private ObservableList<String> niveisUrgencia = FXCollections.observableArrayList("TODOS", "CRÍTICO", "ALTO", "MÉDIO", "BAIXO");

    /**
     * Construtor com injeção de dependências do Spring.
     *
     * @param estoqueService  serviço responsável pelos dados de estoque
     * @param csvExporter     serviço para exportar dados em CSV
     */
    public PrevisaoDeComprasController(EstoqueService estoqueService,
                                       CSVPrevisaoComprasExporter csvExporter) {
        this.estoqueService = estoqueService;
        this.csvExporter = csvExporter;
    }

    /**
     * Método executado automaticamente ao carregar a tela (JavaFX).
     *
     * Responsável por:
     * - inicializar filtros
     * - carregar dados
     * - configurar os ComboBox
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarCombobox();
        configurarFiltros();
        carregarDados();
    }

    /**
     * Configura os ComboBox de categoria e urgência.
     * Define valores padrão.
     */
    private void configurarCombobox() {
        cbUrgencia.setItems(niveisUrgencia);
        cbUrgencia.setValue("TODOS"); // valor inicial

        // Listeners para filtros automáticos
        cbUrgencia.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        txtFiltro.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
    }

    /**
     * Configura filtros em tempo real usando FilteredList.
     *
     * Os listeners observam mudanças nos campos de texto e combobox,
     * e chamam o método aplicarFiltros().
     */
    private void configurarFiltros() {
        produtosFiltrados = new FilteredList<>(produtosData, p -> true);
        tabelaProdutos.setItems(produtosFiltrados);

        txtFiltro.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        cbCategoria.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
        cbUrgencia.valueProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros());
    }

    /**
     * Carrega os dados do estoque a partir do serviço.
     *
     * Aqui ocorre:
     * - cálculo da quantidade a comprar
     * - determinação do nível de urgência
     * - exclusão de itens que não precisam ser comprados
     * - conversão para DTO de exibição
     */
    private void carregarDados() {
        try {
            var estoques = estoqueService.listarEstoques();

            List<PrevisaoCompraDTO> previsoes = estoques.stream()
                    .map(estoque -> {
                        // Calcula quanto precisa comprar
                        Integer quantidadeComprar = PrevisaoCompraDTO.calcularQuantidadeComprar(
                                estoque.quantidade(), estoque.quantidadeMinima()
                        );

                        if (quantidadeComprar <= 0) {
                            return null; // ignora produtos que não precisam compra
                        }

                        // Determina urgência
                        String urgencia = PrevisaoCompraDTO.determinarUrgencia(
                                estoque.quantidade(), estoque.quantidadeMinima()
                        );

                        // Cria DTO para a tabela
                        return new PrevisaoCompraDTO(
                                estoque.idProduto(),
                                estoque.nome(),
                                estoque.tipo(),
                                estoque.quantidade(),
                                estoque.quantidadeMinima(),
                                quantidadeComprar,
                                urgencia
                        );
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            produtosData.clear();
            produtosData.addAll(previsoes);

            atualizarCategorias();
            atualizarContagemProdutos();

        } catch (Exception e) {
            exibirAlertaErro("Erro ao carregar dados",
                    "Não foi possível carregar os dados: " + e.getMessage());
        }
    }

    /**
     * Atualiza dinamicamente o ComboBox de categorias com base nos produtos carregados.
     * Inclui a opção "TODAS".
     */
    private void atualizarCategorias() {
        Set<String> categoriasUnicas = new HashSet<>();
        categoriasUnicas.add("TODAS");

        for (PrevisaoCompraDTO produto : produtosData) {
            if (produto.getCategoria() != null) {
                categoriasUnicas.add(produto.getCategoria().trim());
            }
        }

        String selecaoAtual = cbCategoria.getValue();

        categorias.clear();
        categorias.addAll(categoriasUnicas.stream().sorted().collect(Collectors.toList()));

        // Mantém categoria selecionada se ainda existir
        if (categorias.contains(selecaoAtual)) {
            cbCategoria.setValue(selecaoAtual);
        } else {
            cbCategoria.setValue("TODAS");
        }
    }

    /**
     * Aplica os filtros:
     * - Texto (nome do produto)
     * - Categoria
     * - Urgência
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
     * Atualiza a quantidade de produtos mostrados na tela.
     * Exemplo: "Produtos encontrados: 5 de 12"
     */
    private void atualizarContagemProdutos() {
        if (lblContador != null) {
            lblContador.setText(
                    String.format("Produtos encontrados: %d de %d",
                            produtosFiltrados.size(),
                            produtosData.size())
            );
        }
    }

    /**
     * Gera e exibe uma lista de compras em texto.
     * Mostra um Alert contendo todos os produtos que precisam ser comprados.
     */
    @FXML
    void gerarListaCompras(ActionEvent event) {

        List<PrevisaoCompraDTO> itens = tabelaProdutos.getItems();

        if (itens.isEmpty()) {
            exibirAlertaInformacao("Lista Vazia", "Não há produtos para comprar.");
            return;
        }

        StringBuilder lista = new StringBuilder("LISTA DE COMPRAS:\n\n");

        int totalUnidades = 0;

        for (PrevisaoCompraDTO produto : itens) {
            lista.append(String.format("- %s: %d unidades (%s)\n",
                    produto.getNomeProduto(),
                    produto.getQuantidadeComprar(),
                    produto.getNivelUrgencia()));

            totalUnidades += produto.getQuantidadeComprar();
        }

        lista.append(String.format(
                "\nTOTAL: %d produtos / %d unidades",
                itens.size(), totalUnidades
        ));

        TextArea area = new TextArea(lista.toString());
        area.setEditable(false);
        area.setWrapText(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lista de Compras");
        alert.setHeaderText("Itens necessários para reposição:");
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    /**
     * Exporta os dados mostrados na tabela para um arquivo CSV.
     */
    @FXML
    void exportarParaCSV(ActionEvent event) {
        List<PrevisaoCompraDTO> dados = tabelaProdutos.getItems();

        if (dados.isEmpty()) {
            exibirAlertaInformacao("Nenhum dado", "Não há dados para exportar.");
            return;
        }

        String resultado = csvExporter.exportarPrevisaoComprasCSV(dados);
        exibirAlertaInformacao("Exportação CSV", resultado);
    }

    /**
     * Reseta todos os filtros para os valores padrão.
     */
    @FXML
    void limparFiltros(ActionEvent event) {
        txtFiltro.clear();
        cbUrgencia.setValue("TODOS");
    }

    //====================================================
    // MÉTODOS AUXILIARES DE ALERTA
    //====================================================

    /**
     * Exibe um alerta de erro padrão.
     */
    private void exibirAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta informativo padrão.
     */
    private void exibirAlertaInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
