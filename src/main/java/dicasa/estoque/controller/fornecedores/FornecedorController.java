package dicasa.estoque.controller.fornecedores;

import dicasa.estoque.models.dto.FornecedorResponseDTO;
import dicasa.estoque.service.FornecedorService;
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
    @FXML private ComboBox<String> cbTipoBusca;
    @FXML private TextField txtBusca;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpar;

    @FXML private TableView<FornecedorResponseDTO> tabelaFornecedores;
    @FXML private TableColumn<FornecedorResponseDTO, String> colRazaoSocial;
    @FXML private TableColumn<FornecedorResponseDTO, String> colNomeFantasia;
    @FXML private TableColumn<FornecedorResponseDTO, String> colCnpj;
    @FXML private TableColumn<FornecedorResponseDTO, String> colEndereco;
    @FXML private TableColumn<FornecedorResponseDTO, String> colTelefone;
    @FXML private TableColumn<FornecedorResponseDTO, String> colCidade;

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

            // Configurar as colunas para corresponder ao DTO
            // IMPORTANTE: O nome no PropertyValueFactory deve corresponder EXATAMENTE ao nome do campo no DTO
            colRazaoSocial.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
            colNomeFantasia.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
            colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

            System.out.println("Colunas básicas configuradas");

            // Configurar coluna de endereço (logradouro)
            colEndereco.setCellValueFactory(cellData -> {
                FornecedorResponseDTO fornecedor = cellData.getValue();
                if (fornecedor == null) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }
                String endereco = fornecedor.logradouro() != null ? fornecedor.logradouro() : "";
                return new javafx.beans.property.SimpleStringProperty(endereco);
            });

            // Configurar coluna de telefone
            colTelefone.setCellValueFactory(cellData -> {
                FornecedorResponseDTO fornecedor = cellData.getValue();
                if (fornecedor == null || fornecedor.telefones() == null || fornecedor.telefones().isEmpty()) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }
                // Pegar o primeiro telefone da lista
                String primeiroTelefone = fornecedor.telefones().values().iterator().next();
                return new javafx.beans.property.SimpleStringProperty(primeiroTelefone);
            });

            // Configurar coluna de cidade/UF
            colCidade.setCellValueFactory(cellData -> {
                FornecedorResponseDTO fornecedor = cellData.getValue();
                if (fornecedor == null) {
                    return new javafx.beans.property.SimpleStringProperty("");
                }

                StringBuilder cidadeUf = new StringBuilder();
                if (fornecedor.cidade() != null && !fornecedor.cidade().isEmpty()) {
                    cidadeUf.append(fornecedor.cidade());
                }
                if (fornecedor.uf() != null && !fornecedor.uf().isEmpty()) {
                    if (cidadeUf.length() > 0) cidadeUf.append("/");
                    cidadeUf.append(fornecedor.uf());
                }

                return new javafx.beans.property.SimpleStringProperty(cidadeUf.toString());
            });

            // Configurar a tabela
            tabelaFornecedores.setItems(fornecedoresData);

            System.out.println("Tabela configurada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao configurar tabela: " + e.getMessage());
        }
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
            mostrarAlerta("Cadastro", "Funcionalidade de cadastro em desenvolvimento.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro inesperado: " + e.getMessage());
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
}