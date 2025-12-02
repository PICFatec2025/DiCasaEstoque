package dicasa.estoque.controller.fornecedores;

import dicasa.estoque.models.dto.FornecedorResponseDTO;
import dicasa.estoque.navigation.Rotas;
import dicasa.estoque.service.FornecedorService;
import dicasa.estoque.util.SpringFXManager;
import dicasa.estoque.controller.fornecedores.CadastroFornecedorController;
import dicasa.estoque.controller.fornecedores.EditarFornecedorController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class FornecedorController implements Initializable {

    // Elementos do FXML
    @FXML private Label lblTitulo;
    @FXML private Button btnCadastrar;
    @FXML private Button btnExcluir;
    @FXML private ComboBox<String> cbTipoBusca;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpar;

    @FXML private TableView<FornecedorResponseDTO> tabelaFornecedores;
    @FXML private TableColumn<FornecedorResponseDTO, String> colRazaoSocial;
    @FXML private TableColumn<FornecedorResponseDTO, String> colNomeFantasia;
    @FXML private TableColumn<FornecedorResponseDTO, String> colCnpj;
    @FXML private TableColumn<FornecedorResponseDTO, String> colLogradouro;
    @FXML private TableColumn<FornecedorResponseDTO, String> colComplemento;
    @FXML private TableColumn<FornecedorResponseDTO, String> colBairro;
    @FXML private TableColumn<FornecedorResponseDTO, String> colCidade;
    @FXML private TableColumn<FornecedorResponseDTO, String> colUf;
    @FXML private TableColumn<FornecedorResponseDTO, String> colCep;
    @FXML private TableColumn<FornecedorResponseDTO, String> colTelefones;

    @FXML private Button btnAddProduto;
    @FXML private Button btnExportar;

    private final FornecedorService fornecedorService;
    private ObservableList<FornecedorResponseDTO> fornecedoresData = FXCollections.observableArrayList();

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            configurarComboBoxBusca();
            configurarTabela();
            carregarFornecedores();
            configurarEventosDeLinha();
            debugDados(); // Adicionado para debug
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao inicializar tela: " + e.getMessage());
        }
    }

    private void debugDados() {
        System.out.println("=== DEBUG DOS DADOS ===");
        System.out.println("Total de fornecedores na tabela: " + fornecedoresData.size());

        for (int i = 0; i < fornecedoresData.size(); i++) {
            FornecedorResponseDTO fornecedor = fornecedoresData.get(i);
            System.out.println("Fornecedor " + (i+1) + ":");
            System.out.println("  - Razão Social: " + fornecedor.razaoSocial());
            System.out.println("  - Nome Fantasia: " + fornecedor.nomeFantasia());
            System.out.println("  - CNPJ: " + fornecedor.cnpj());
            System.out.println("  - Cidade: " + fornecedor.cidade());
            System.out.println("  - UF: " + fornecedor.uf());
            System.out.println("  - Telefones: " + (fornecedor.telefones() != null ? fornecedor.telefones().size() : 0));
        }
    }

    private void configurarComboBoxBusca() {
        try {
            // Opções de busca
            ObservableList<String> opcoesBusca = FXCollections.observableArrayList(
                    "Razão Social",
                    "Nome Fantasia",
                    "CNPJ",
                    "Cidade",
                    "Estado (UF)",
                    "Telefone"
            );
            cbTipoBusca.setItems(opcoesBusca);
            cbTipoBusca.getSelectionModel().selectFirst();

            // Permitir busca ao pressionar Enter
            txtBusca.setOnAction(event -> onClickBuscar(null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTabela() {
        try {
            System.out.println("Configurando tabela...");

            colRazaoSocial.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().razaoSocial() : "")
            ));
            colNomeFantasia.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().nomeFantasia() : "")
            ));
            colCnpj.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().cnpj() : "")
            ));

            colLogradouro.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().logradouro() : "")
            ));
            colComplemento.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().complemento() : "")
            ));
            colBairro.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().bairro() : "")
            ));
            colCidade.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().cidade() : "")
            ));
            colUf.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().uf() : "")
            ));
            colCep.setCellValueFactory(cellData -> new SimpleStringProperty(
                    valorSeguro(cellData.getValue() != null ? cellData.getValue().cep() : "")
            ));
            colTelefones.setCellValueFactory(cellData -> {
                FornecedorResponseDTO fornecedor = cellData.getValue();
                if (fornecedor == null || fornecedor.telefones() == null || fornecedor.telefones().isEmpty()) {
                    return new SimpleStringProperty("");
                }
                String telefonesConcatenados = String.join(", ", fornecedor.telefones().values());
                return new SimpleStringProperty(telefonesConcatenados);
            });

            tabelaFornecedores.setItems(fornecedoresData);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao configurar tabela: " + e.getMessage());
        }
    }

    private void configurarEventosDeLinha() {
        tabelaFornecedores.setRowFactory(tv -> {
            TableRow<FornecedorResponseDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    abrirEdicaoFornecedor(row.getItem());
                }
            });
            return row;
        });
    }

    private void carregarFornecedores() {
        try {
            System.out.println("Carregando fornecedores do service...");
            List<FornecedorResponseDTO> fornecedores = fornecedorService.listarFornecedoresCompleto();
            System.out.println("Total de fornecedores carregados: " + fornecedores.size());

            fornecedoresData.setAll(fornecedores);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    @FXML
    public void onClickBuscar(ActionEvent event) {
        try {
            String tipoBusca = cbTipoBusca.getValue();
            String termoBusca = txtBusca.getText().trim();

            if (termoBusca.isEmpty()) {
                // Se o campo de busca estiver vazio, mostrar todos
                carregarFornecedores();
                return;
            }

            // Usando o método do service para busca no banco
            List<FornecedorResponseDTO> resultados = fornecedorService.buscarFornecedoresPorTermo(tipoBusca, termoBusca);
            fornecedoresData.setAll(resultados);

            System.out.println("Resultados da busca: " + resultados.size() + " fornecedores encontrados");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao buscar fornecedores: " + e.getMessage());
        }
    }

    @FXML
    public void onClickLimparBusca(ActionEvent event) {
        try {
            txtBusca.clear();
            cbTipoBusca.getSelectionModel().selectFirst();
            carregarFornecedores();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao limpar busca: " + e.getMessage());
        }
    }

    @FXML
    public void onClickCadastrarFornecedor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Rotas.CADASTRO_FORNECEDOR_VIEW));
            loader.setControllerFactory(SpringFXManager.getContext()::getBean);
            Parent root = loader.load();

            CadastroFornecedorController controller = loader.getController();
            controller.setOnSaveSuccess(this::carregarFornecedores);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(btnCadastrar.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Cadastro de Fornecedor");
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro inesperado: " + e.getMessage());
        }
    }

    private void abrirEdicaoFornecedor(FornecedorResponseDTO fornecedor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Rotas.EDITAR_FORNECEDOR_VIEW));
            loader.setControllerFactory(SpringFXManager.getContext()::getBean);
            Parent root = loader.load();

            EditarFornecedorController controller = loader.getController();
            controller.setFormData(fornecedor);
            controller.setOnSaveSuccess(this::carregarFornecedores);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(tabelaFornecedores.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Fornecedor");
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir edição: " + e.getMessage());
        }
    }

    @FXML
    public void onClickAdicionarProduto(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/produtos/adicionar-produto.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adicionar Produto");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de produtos: " + e.getMessage());
        }
    }

    @FXML
    public void onClickExportar(ActionEvent event) {
        try {
            if (fornecedoresData.isEmpty()) {
                mostrarAlerta("Exportar", "Não há dados para exportar.");
                return;
            }

            mostrarAlerta("Exportar",
                    String.format("Pronto para exportar %d fornecedor(es).",
                            fornecedoresData.size()));

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao exportar dados: " + e.getMessage());
        }
    }

    @FXML
    public void onClickExcluirFornecedor(ActionEvent event) {
        try {
            FornecedorResponseDTO fornecedorSelecionado = tabelaFornecedores.getSelectionModel().getSelectedItem();
            if (fornecedorSelecionado == null) {
                mostrarAlerta("Excluir", "Selecione um fornecedor para excluir.");
                return;
            }

            Alert alertaConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            alertaConfirmacao.setTitle("Confirmar exclusão");
            alertaConfirmacao.setHeaderText("Deseja realmente excluir o fornecedor selecionado?");
            alertaConfirmacao.setContentText(String.format("Fornecedor: %s (%s)",
                    valorSeguro(fornecedorSelecionado.nomeFantasia()),
                    valorSeguro(fornecedorSelecionado.cnpj())));

            if (alertaConfirmacao.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }

            fornecedorService.excluirFornecedor(fornecedorSelecionado.idFornecedor());
            carregarFornecedores();
            mostrarAlerta("Excluir", "Fornecedor excluído com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao excluir fornecedor: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao mostrar alerta: " + e.getMessage());
        }
    }

    private String valorSeguro(String valor) {
        return valor != null ? valor : "";
    }
}