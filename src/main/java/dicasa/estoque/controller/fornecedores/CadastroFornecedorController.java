package dicasa.estoque.controller.fornecedores;

import dicasa.estoque.controller.DataFormController;
import dicasa.estoque.models.dto.EnderecoRequestDTO;
import dicasa.estoque.models.dto.FornecedorRequestDTO;
import dicasa.estoque.service.FornecedorService;
import dicasa.estoque.util.EmailValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controller responsável pelo cadastro e edição de fornecedores.
 * Contém máscaras de entrada, validações completas e navegação por teclado.
 */
@Component
public class CadastroFornecedorController implements DataFormController {

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
    @FXML private ComboBox<String> comboUf;
    @FXML private TextField txtCep;
    @FXML private Label lblMensagem;
    @FXML private Button btnCancelar;

    private Runnable onSaveSuccess;

    private static final List<String> UFS = List.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    public CadastroFornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    /**
     * Inicializa os componentes da tela ao carregar o FXML.
     */
    @FXML
    public void initialize() {
        comboUf.getItems().setAll(UFS);
        comboUf.getSelectionModel().clearSelection();
        resetForm();

        configurarLimitadores();
        configurarMascaras();
        configurarNavegacaoPorEnter();
    }

    /**
     * Configura os limitadores de tamanho para campos de texto.
     */
    private void configurarLimitadores() {
        aplicarLimitador(txtRazaoSocial, 45);
        aplicarLimitador(txtNomeFantasia, 100);
        aplicarLimitador(txtContato, 255);
        aplicarLimitador(txtEmail, 255);
        aplicarLimitador(txtLogradouro, 100);
        aplicarLimitador(txtComplemento, 30);
        aplicarLimitador(txtBairro, 50);
        aplicarLimitador(txtCidade, 50);

        aplicarLimitadorPorDigitos(txtTelefonePrincipal, 11);
        aplicarLimitadorPorDigitos(txtTelefoneSecundario, 11);
        aplicarLimitadorPorDigitos(txtCep, 8);
        aplicarLimitadorPorDigitos(txtCnpj, 14);
    }

    /**
     * Aplica limitador de caracteres comum (sem máscara.
     */
    private void aplicarLimitador(TextField field, int maxLength) {
        if (field == null) return;
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > maxLength) {
                field.setText(newVal.substring(0, maxLength));
            }
        });
    }

    /**
     * Limita a quantidade de dígitos em campos com máscara (telefone, CEP, CNPJ).
     */
    private void aplicarLimitadorPorDigitos(TextField field, int maxDigitos) {
        if (field == null) return;
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String digitos = newVal.replaceAll("\\D", "");
            if (digitos.length() > maxDigitos) {
                String novosDigitos = digitos.substring(0, maxDigitos);
                String formatado = switch (field.getId()) {
                    case "txtCep" -> formatarCep(novosDigitos);
                    case "txtCnpj" -> formatarCnpj(novosDigitos);
                    default -> formatarTelefone(novosDigitos);
                };
                field.setText(formatado);
                field.positionCaret(formatado.length());
            }
        });
    }

    /**
     * Aplica as máscaras de entrada nos campos formatados.
     */
    private void configurarMascaras() {
        aplicarMascaraTelefone(txtTelefonePrincipal);
        aplicarMascaraTelefone(txtTelefoneSecundario);
        aplicarMascaraCep(txtCep);
        aplicarMascaraCnpj(txtCnpj);
    }
    

    /**
     * Aplica máscara de telefone brasileira: (99) 99999-9999 ou (99) 9999-9999.
     */
    private void aplicarMascaraTelefone(TextField field) {
        field.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.equals(oldValue)) return;

            String apenasDigitos = newValue.replaceAll("\\D", "");
            if (apenasDigitos.length() > 11) {
                apenasDigitos = apenasDigitos.substring(0, 11);
            }

            String formatado = formatarTelefone(apenasDigitos);
            if (!newValue.equals(formatado)) {
                field.setText(formatado);
                field.positionCaret(formatado.length());
            }
        });

        field.setOnMouseClicked(e -> field.selectAll());

        field.focusedProperty().addListener((o, old, focused) -> {
            if (!focused) {
                String digits = field.getText().replaceAll("\\D", "");
                field.setText(formatarTelefone(digits));
            }
        });
    }

    private String formatarTelefone(String digitos) {
        if (digitos.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        int len = digitos.length();

        sb.append('(');
        if (len > 0) sb.append(digitos.charAt(0));
        if (len > 1) sb.append(digitos.charAt(1));
        sb.append(") ");

        if (len <= 2) return sb.toString();

        String numero = digitos.substring(2);

        if (len == 11) { // celular com 9º dígito
            sb.append(numero, 0, 5).append('-').append(numero.substring(5));
        } else {
            int parte1 = Math.min(4, numero.length());
            sb.append(numero.substring(0, parte1));
            if (numero.length() > 4) {
                sb.append('-').append(numero.substring(4));
            }
        }
        return sb.toString();
    }

    /**
     * Aplica máscara de CEP: 00000-000
     */
    private void aplicarMascaraCep(TextField field) {
        field.textProperty().addListener((obs, old, newValue) -> {
            if (newValue == null) return;
            String digitos = newValue.replaceAll("\\D", "");
            if (digitos.length() > 8) digitos = digitos.substring(0, 8);
            String formatado = formatarCep(digitos);
            if (!newValue.equals(formatado)) {
                field.setText(formatado);
                field.positionCaret(formatado.length());
            }
        });
    }

    private String formatarCep(String digitos) {
        if (digitos.length() <= 5) return digitos;
        return digitos.substring(0, 5) + "-" + digitos.substring(5);
    }

    /**
     * Aplica máscara de CNPJ: 00.000.000/0000-00
     */
    private void aplicarMascaraCnpj(TextField field) {
        field.textProperty().addListener((obs, old, newValue) -> {
            if (newValue == null) return;
            String digitos = newValue.replaceAll("\\D", "");
            if (digitos.length() > 14) digitos = digitos.substring(0, 14);
            String formatado = formatarCnpj(digitos);
            if (!newValue.equals(formatado)) {
                field.setText(formatado);
                field.positionCaret(formatado.length());
            }
        });
    }

    private String formatarCnpj(String digitos) {
        return switch (digitos.length()) {
            case 0, 1, 2 -> digitos;
            case 3, 4, 5 -> digitos.substring(0, 2) + "." + digitos.substring(2);
            case 6, 7, 8 -> digitos.substring(0, 2) + "." + digitos.substring(2, 5) + "." + digitos.substring(5);
            case 9, 10, 11, 12 -> digitos.substring(0, 2) + "." + digitos.substring(2, 5) + "." + digitos.substring(5, 8) + "/" + digitos.substring(8);
            default -> digitos.substring(0, 2) + "." + digitos.substring(2, 5) + "." + digitos.substring(5, 8) + "/" + digitos.substring(8, 12) + "-" + digitos.substring(12);
        };
    }

    /**
     * Configura navegação entre campos usando a tecla Enter.
     */
    private void configurarNavegacaoPorEnter() {
        List<Control> ordem = List.of(
                txtRazaoSocial, txtNomeFantasia, txtCnpj, txtContato, txtEmail,
                txtTelefonePrincipal, txtTelefoneSecundario, txtLogradouro,
                txtComplemento, txtBairro, txtCidade, txtCep, comboUf
        );

        for (int i = 0; i < ordem.size(); i++) {
            Control atual = ordem.get(i);
            int proximo = i + 1;
            atual.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    if (proximo < ordem.size()) {
                        ordem.get(proximo).requestFocus();
                    } else {
                        onSalvarFornecedor();
                    }
                    event.consume();
                }
            });
        }
    }


    /**
     * Executa a validação e salva o fornecedor.
     */
    @FXML
    public void onSalvarFornecedor() {
        try {
            FornecedorRequestDTO dto = montarFornecedorRequest();
            fornecedorService.salvarFornecedor(dto);

            lblMensagem.setStyle("-fx-text-fill: #2e7d32;");
            lblMensagem.setText("Fornecedor salvo com sucesso!");

            Optional.ofNullable(onSaveSuccess).ifPresent(Runnable::run);
            fecharJanela();
        } catch (IllegalArgumentException e) {
            lblMensagem.setStyle("-fx-text-fill: #b00020;");
            lblMensagem.setText(e.getMessage());
        } catch (Exception e) {
            lblMensagem.setStyle("-fx-text-fill: #b00020;");
            lblMensagem.setText("Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    public void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }


    /**
     * Monta o DTO com todas as validações necessárias.
     */
    private FornecedorRequestDTO montarFornecedorRequest() {
        String razaoSocial = textoObrigatorio(txtRazaoSocial, "a razão social");
        String nomeFantasia = textoObrigatorio(txtNomeFantasia, "o nome fantasia");
        String cnpj = textoObrigatorio(txtCnpj, "o CNPJ");
        String logradouro = textoObrigatorio(txtLogradouro, "o logradouro");
        String cidade = textoObrigatorio(txtCidade, "a cidade");

        String uf = Optional.ofNullable(comboUf.getValue())
                .orElseThrow(() -> new IllegalArgumentException("Selecione a UF."));

        String cep = textoObrigatorio(txtCep, "o CEP");

        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
                logradouro,
                txtComplemento.getText().trim().isEmpty() ? null : txtComplemento.getText().trim(),
                txtBairro.getText().trim().isEmpty() ? null : txtBairro.getText().trim(),
                cidade,
                uf,
                cep.replaceAll("\\D", "")
        );

        List<String> telefones = Stream.of(txtTelefonePrincipal, txtTelefoneSecundario)
                .map(TextField::getText)
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(t -> t.replaceAll("\\D", ""))
                .toList();

        if (telefones.isEmpty()) {
            throw new IllegalArgumentException("Informe pelo menos um telefone.");
        }

        return new FornecedorRequestDTO(
                validarCnpj(cnpj),
                validarTamanhoMaximo(nomeFantasia, 100, "Nome Fantasia"),
                validarTamanhoMaximo(razaoSocial, 45, "Razão Social"),
                validarEndereco(endereco),
                telefones,
                validarEmail(txtEmail.getText()),
                validarTamanhoOpcional(txtContato.getText(), 255, "Contato")
        );
    }

    private String textoObrigatorio(TextField field, String nomeCampo) {
        String texto = field.getText();
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe " + nomeCampo + ".");
        }
        return texto.trim();
    }


    /**
     * Valida formato e tamanho do e-mail usando EmailValidator.
     * Campo opcional. Aceita e-mails gratuitos (pode bloquear se desejar).
     */
    private String validarEmail(String emailStr) {
        if (emailStr == null || emailStr.trim().isEmpty()) {
            return null;
        }

        String email = emailStr.trim();

        if (email.length() > 255) {
            throw new IllegalArgumentException("E-mail deve ter no máximo 255 caracteres.");
        }

        if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("E-mail inválido. Verifique o formato (ex: nome@empresa.com).");
        }


        return email.toLowerCase();
    }

    private String validarCnpj(String cnpj) {
        String digitos = cnpj.replaceAll("\\D", "");
        if (digitos.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve conter 14 dígitos.");
        }
        return digitos;
    }

    private EnderecoRequestDTO validarEndereco(EnderecoRequestDTO e) {
        validarTamanhoMaximo(e.logradouro(), 100, "Logradouro");
        validarTamanhoOpcional(e.complemento(), 30, "Complemento");
        validarTamanhoOpcional(e.bairro(), 50, "Bairro");
        validarTamanhoMaximo(e.cidade(), 50, "Cidade");
        validarUf(e.uf());
        validarCep(e.cep());
        return e;
    }

    private String validarCep(String cep) {
        String num = cep.replaceAll("\\D", "");
        if (num.length() != 8) {
            throw new IllegalArgumentException("CEP deve ter 8 dígitos.");
        }
        return num;
    }

    private String validarUf(String uf) {
        if (uf == null || !uf.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("UF inválida.");
        }
        return uf;
    }

    private String validarTamanhoMaximo(String valor, int max, String campo) {
        if (valor != null && valor.length() > max) {
            throw new IllegalArgumentException(campo + " deve ter no máximo " + max + " caracteres.");
        }
        return valor;
    }

    private String validarTamanhoOpcional(String valor, int max, String campo) {
        if (valor == null || valor.trim().isEmpty()) return null;
        String v = valor.trim();
        if (v.length() > max) {
            throw new IllegalArgumentException(campo + " deve ter no máximo " + max + " caracteres.");
        }
        return v;
    }



    @Override
    public void setFormData(Object data) {
        // Implementação futura para edição
    }

    @Override
    public void resetForm() {
        Stream.of(txtRazaoSocial, txtNomeFantasia, txtCnpj, txtContato, txtEmail,
                        txtTelefonePrincipal, txtTelefoneSecundario, txtLogradouro,
                        txtComplemento, txtBairro, txtCidade, txtCep)
                .forEach(f -> { if (f != null) f.clear(); });

        comboUf.getSelectionModel().clearSelection();
        lblMensagem.setText("");
    }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }
}