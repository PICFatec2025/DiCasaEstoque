package dicasa.estoque.controller.fornecedores;

import dicasa.estoque.controller.DataFormController;
import dicasa.estoque.models.dto.EnderecoRequestDTO;
import dicasa.estoque.models.dto.FornecedorRequestDTO;
import dicasa.estoque.models.dto.FornecedorResponseDTO;
import dicasa.estoque.service.FornecedorService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EditarFornecedorController implements DataFormController {

    private final FornecedorService fornecedorService;

    @FXML private TextField txtRazaoSocial;
    @FXML private TextField txtNomeFantasia;
    @FXML private TextField txtCnpj;
    @FXML private TextField txtContato;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefonePrincipal;
    @FXML private TextField txtTelefoneSecundario;
    @FXML private TextField txtLogradouro;
    @FXML private TextField txtComplemento;
    @FXML private TextField txtBairro;
    @FXML private TextField txtCidade;
    @FXML private TextField txtUf;
    @FXML private TextField txtCep;
    @FXML private Label lblMensagem;
    @FXML private Button btnCancelar;

    private Runnable onSaveSuccess;
    private Long fornecedorId;

    public EditarFornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @FXML
    public void initialize() {
        configurarFormatadores();
        resetForm();
    }

    @FXML
    public void onSalvarAlteracoes() {
        try {
            if (fornecedorId == null) {
                throw new IllegalStateException("Fornecedor não carregado para edição.");
            }

            FornecedorRequestDTO fornecedorDto = montarFornecedorRequest();
            fornecedorService.atualizarFornecedor(fornecedorId, fornecedorDto);
            lblMensagem.setStyle("-fx-text-fill: #2e7d32;");
            lblMensagem.setText("Fornecedor atualizado com sucesso!");

            Optional.ofNullable(onSaveSuccess).ifPresent(Runnable::run);
            fecharJanela();
        } catch (IllegalArgumentException e) {
            lblMensagem.setStyle("-fx-text-fill: #b00020;");
            lblMensagem.setText(e.getMessage());
        } catch (Exception e) {
            lblMensagem.setStyle("-fx-text-fill: #b00020;");
            lblMensagem.setText(e.getMessage());
        }
    }

    @FXML
    public void onCancelar() {
        fecharJanela();
    }

    @Override
    public void setFormData(Object data) {
        if (data instanceof FornecedorResponseDTO fornecedor) {
            fornecedorId = fornecedor.idFornecedor();
            txtRazaoSocial.setText(valorSeguro(fornecedor.razaoSocial()));
            txtNomeFantasia.setText(valorSeguro(fornecedor.nomeFantasia()));
            txtCnpj.setText(valorSeguro(fornecedor.cnpj()));
            txtLogradouro.setText(valorSeguro(fornecedor.logradouro()));
            txtComplemento.setText(valorSeguro(fornecedor.complemento()));
            txtBairro.setText(valorSeguro(fornecedor.bairro()));
            txtCidade.setText(valorSeguro(fornecedor.cidade()));
            txtUf.setText(valorSeguro(fornecedor.uf()));
            txtCep.setText(valorSeguro(fornecedor.cep()));

            List<String> telefones = new ArrayList<>();
            if (fornecedor.telefones() != null) {
                telefones.addAll(fornecedor.telefones().values());
            }

            if (!telefones.isEmpty()) {
                txtTelefonePrincipal.setText(telefones.get(0));
            }
            if (telefones.size() > 1) {
                txtTelefoneSecundario.setText(telefones.get(1));
            }
        }
    }

    @Override
    public void resetForm() {
        Stream.of(
                txtRazaoSocial, txtNomeFantasia, txtCnpj, txtContato, txtEmail,
                txtTelefonePrincipal, txtTelefoneSecundario, txtLogradouro,
                txtComplemento, txtBairro, txtCidade, txtUf, txtCep
        ).forEach(field -> {
            if (field != null) {
                field.clear();
            }
        });
        lblMensagem.setText("");
        fornecedorId = null;
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }

    private FornecedorRequestDTO montarFornecedorRequest() {
        String razaoSocial = textoObrigatorio(txtRazaoSocial, "a razão social");
        String nomeFantasia = textoObrigatorio(txtNomeFantasia, "o nome fantasia");
        String cnpj = textoObrigatorio(txtCnpj, "o CNPJ");
        String logradouro = textoObrigatorio(txtLogradouro, "o logradouro");
        String cidade = textoObrigatorio(txtCidade, "a cidade");
        String uf = textoObrigatorio(txtUf, "a UF");
        String cep = textoObrigatorio(txtCep, "o CEP");

        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
                logradouro,
                txtComplemento.getText(),
                txtBairro.getText(),
                cidade,
                uf,
                cep
        );

        List<String> telefones = Stream.of(txtTelefonePrincipal.getText(), txtTelefoneSecundario.getText())
                .filter(telefone -> telefone != null && !telefone.trim().isEmpty())
                .map(String::trim)
                .collect(Collectors.toList());

        if (telefones.isEmpty()) {
            throw new IllegalArgumentException("Informe pelo menos um telefone.");
        }

        return new FornecedorRequestDTO(
                validarCnpj(cnpj),
                validarTamanhoMaximo(nomeFantasia, 100, "Nome Fantasia"),
                validarTamanhoMaximo(razaoSocial, 45, "Razão Social"),
                validarEndereco(endereco),
                validarTelefones(telefones),
                validarTamanhoOpcional(txtEmail.getText(), 255, "E-mail"),
                validarTamanhoOpcional(txtContato.getText(), 255, "Contato")
        );
    }

    private String textoObrigatorio(TextField field, String nomeCampo) {
        if (field == null || field.getText() == null || field.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe " + nomeCampo + ".");
        }
        return field.getText().trim();
    }

    private void configurarFormatadores() {
        aplicarLimitador(txtRazaoSocial, 45);
        aplicarLimitador(txtNomeFantasia, 100);
        aplicarLimitador(txtCnpj, 18);
        aplicarLimitador(txtContato, 255);
        aplicarLimitador(txtEmail, 255);
        aplicarLimitador(txtTelefonePrincipal, 14);
        aplicarLimitador(txtTelefoneSecundario, 14);
        aplicarLimitador(txtLogradouro, 100);
        aplicarLimitador(txtComplemento, 30);
        aplicarLimitador(txtBairro, 50);
        aplicarLimitador(txtCidade, 50);
        aplicarLimitador(txtCep, 9);

        if (txtUf != null) {
            txtUf.textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    String upper = newVal.toUpperCase();
                    txtUf.setText(upper.length() > 2 ? upper.substring(0, 2) : upper);
                }
            });
        }

        if (txtCnpj != null) {
            txtCnpj.textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.length() > 18) {
                    txtCnpj.setText(newVal.substring(0, 18));
                }
            });
        }
    }

    private void aplicarLimitador(TextField field, int maxLength) {
        if (field == null) return;
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > maxLength) {
                field.setText(newVal.substring(0, maxLength));
            }
        });
    }

    private String validarCnpj(String cnpj) {
        String somenteDigitos = cnpj.replaceAll("\\D", "");
        if (somenteDigitos.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve conter 14 dígitos numéricos.");
        }
        return somenteDigitos;
    }

    private EnderecoRequestDTO validarEndereco(EnderecoRequestDTO endereco) {
        String logradouro = validarTamanhoMaximo(endereco.logradouro(), 100, "Logradouro");
        String complemento = validarTamanhoOpcional(endereco.complemento(), 30, "Complemento");
        String bairro = validarTamanhoOpcional(endereco.bairro(), 50, "Bairro");
        String cidade = validarTamanhoMaximo(endereco.cidade(), 50, "Cidade");
        String uf = validarUf(endereco.uf());
        String cep = validarCep(endereco.cep());

        return new EnderecoRequestDTO(logradouro, complemento, bairro, cidade, uf, cep);
    }

    private List<String> validarTelefones(List<String> telefones) {
        return telefones.stream()
                .map(telefone -> telefone.replaceAll("\\D", ""))
                .peek(telefoneNumerico -> {
                    if (telefoneNumerico.isEmpty()) {
                        throw new IllegalArgumentException("Telefone não pode ser vazio.");
                    }
                    if (telefoneNumerico.length() > 14) {
                        throw new IllegalArgumentException("Telefone deve ter até 14 dígitos.");
                    }
                })
                .collect(Collectors.toList());
    }

    private String validarCep(String cep) {
        String numerico = cep.replaceAll("\\D", "");
        if (numerico.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos numéricos.");
        }
        return numerico;
    }

    private String validarUf(String uf) {
        if (uf == null || uf.trim().length() != 2) {
            throw new IllegalArgumentException("UF deve conter exatamente 2 letras.");
        }
        String upper = uf.trim().toUpperCase();
        if (!upper.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("UF deve conter apenas letras.");
        }
        return upper;
    }

    private String validarTamanhoMaximo(String valor, int max, String campo) {
        if (valor != null && valor.length() > max) {
            throw new IllegalArgumentException(campo + " deve ter no máximo " + max + " caracteres.");
        }
        return valor;
    }

    private String validarTamanhoOpcional(String valor, int max, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        String ajustado = valor.trim();
        if (ajustado.length() > max) {
            throw new IllegalArgumentException(campo + " deve ter no máximo " + max + " caracteres.");
        }
        return ajustado;
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private String valorSeguro(String valor) {
        return valor != null ? valor : "";
    }
}
