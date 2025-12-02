package dicasa.estoque.controller.produtos;

import dicasa.estoque.controller.DataFormController;
import dicasa.estoque.models.entities.EstoqueProduto;
import dicasa.estoque.models.entities.Produto;
import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.service.ProdutoService;
import dicasa.estoque.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

import static dicasa.estoque.util.Constraints.textFieldRecebeApenasNumerosInteiros;
import static dicasa.estoque.util.NumberValidatorUtil.ehUmNumeroInteiroPositivo;

@Controller
public class AdicionarProdutoController implements DataFormController {

    private final ProdutoService produtoService;

    @FXML private Label lblTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtMarca;
    @FXML private TextField txtTipo;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtValorUnitario;
    @FXML private TextField txtEstoqueEmergencial;
    @FXML private TextField txtQuantidadeMinima;
    @FXML private TextField txtObservacao;
    @FXML private TextField txtUsuarioCriador;
    @FXML private Label lblMensagem;

    @FXML private Label lblQuantidade;
    @FXML private Label lblValorUnitario;
    @FXML private Label lblEstoqueEmergencial;
    @FXML private Label lblQuantidadeMinima;

    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnDeletar;

    private Produto produtoAtual;

    public AdicionarProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @FXML
    public void initialize() {
        configurarModoCriacao();
        configurarCamposNumericos();
    }

    /**
     * Cria um novo produto a partir dos dados preenchidos no formulário.
     */
    @FXML
    private void salvarProduto() {
        try {
            Produto produto = new Produto();
            EstoqueProduto estoqueProduto = new EstoqueProduto();
            produto.setNome(textoObrigatorio(txtNome, "o nome"));
            produto.setMarca(txtMarca.getText());
            produto.setTipo(textoObrigatorio(txtTipo, "o tipo"));
            estoqueProduto.setQuantidade(ehUmNumeroInteiroPositivo(textoObrigatorio(txtQuantidade, "a quantidade")));
            estoqueProduto.setQuantidadeMinima(ehUmNumeroInteiroPositivo(textoObrigatorio(txtQuantidadeMinima, "a quantidade mínima")));
            estoqueProduto.setEstoqueEmergencial(ehUmNumeroInteiroPositivo(textoObrigatorio(txtEstoqueEmergencial, "o estoque emergencial")));
            estoqueProduto.setData_criacao(LocalDateTime.now());
            produto.setObservacao(txtObservacao.getText());
            produto.setDataCriacao(LocalDateTime.now());

            Usuario usuario = SessionManager.getUsuarioLogado();
            if (usuario == null) {
                throw new IllegalStateException("Nenhum usuário logado.");
            }
            produto.setUsuario(usuario);
            estoqueProduto.setProduto(produto);
            produto.setEstoqueProduto(estoqueProduto);

            produtoService.salvarProduto(produto);

            lblMensagem.setText("Produto salvo com sucesso!");
            fecharJanela();

        } catch (Exception e) {
            lblMensagem.setText("Erro ao salvar produto: " + e.getMessage());
        }
    }

    /**
     * Atualiza as informações do produto atualmente carregado.
     */
    @FXML
    private void atualizarProduto() {
        if (produtoAtual == null) {
            return;
        }
        try {
            produtoAtual.setNome(textoObrigatorio(txtNome, "o nome"));
            produtoAtual.setMarca(txtMarca.getText());
            produtoAtual.setTipo(textoObrigatorio(txtTipo, "o tipo"));
            produtoAtual.setObservacao(txtObservacao.getText());
            produtoAtual.setDataAtualizacao(LocalDateTime.now());

            produtoService.salvarProduto(produtoAtual);
            lblMensagem.setText("Produto atualizado com sucesso!");
            fecharJanela();
        } catch (Exception e) {
            lblMensagem.setText("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    /**
     * Remove o produto selecionado, se houver, do cadastro.
     */
    @FXML
    private void deletarProduto() {
        if (produtoAtual == null || produtoAtual.getIdProduto() == null) {
            return;
        }
        try {
            produtoService.deletarProduto(produtoAtual.getIdProduto());
            lblMensagem.setText("Produto deletado com sucesso!");
            fecharJanela();
        } catch (Exception e) {
            lblMensagem.setText("Erro ao deletar produto: " + e.getMessage());
        }
    }

    /**
     * Recebe dados externos e ajusta o formulário para criação ou edição.
     */
    @Override
    public void setFormData(Object data) {
        if (data instanceof Produto produto) {
            Optional<Produto> produtoCompleto = produtoService.buscarPorId(produto.getIdProduto());
            produtoAtual = produtoCompleto.orElse(produto);
            configurarModoEdicao();
            preencherCamposProduto();
        } else {
            configurarModoCriacao();
        }
    }

    /**
     * Limpa os campos e retorna o formulário ao modo de criação.
     */
    @Override
    public void resetForm() {
        produtoAtual = null;
        limparCampos();
        configurarModoCriacao();
        lblMensagem.setText("");
    }

    /**
     * Preenche os campos com os dados do produto em edição.
     */
    private void preencherCamposProduto() {
        if (produtoAtual == null) {
            return;
        }
        txtNome.setText(produtoAtual.getNome());
        txtMarca.setText(produtoAtual.getMarca());
        txtTipo.setText(produtoAtual.getTipo());
        txtObservacao.setText(produtoAtual.getObservacao());
        Usuario usuario = produtoAtual.getUsuario();
        txtUsuarioCriador.setText(usuario != null ? usuario.getNome() : "");
    }

    /**
     * Apaga os valores informados em cada campo do formulário.
     */
    private void limparCampos() {
        txtNome.clear();
        txtMarca.clear();
        txtTipo.clear();
        txtQuantidade.clear();
        txtValorUnitario.clear();
        txtEstoqueEmergencial.clear();
        txtQuantidadeMinima.clear();
        txtObservacao.clear();
        txtUsuarioCriador.clear();
    }

    /**
     * Configura os elementos da interface para a criação de um novo produto.
     */
    private void configurarModoCriacao() {
        produtoAtual = null;
        lblTitulo.setText("Adicionar Produto");
        toggleCamposEstoque(true);
        alternarBotoes(true);

        Usuario usuario = SessionManager.getUsuarioLogado();
        if (usuario != null) {
            txtUsuarioCriador.setText(usuario.getNome());
        } else {
            txtUsuarioCriador.clear();
        }
        txtUsuarioCriador.setDisable(true);
    }

    /**
     * Ajusta a interface para exibir e editar um produto existente.
     */
    private void configurarModoEdicao() {
        lblTitulo.setText("Detalhes do Produto");
        toggleCamposEstoque(false);
        alternarBotoes(false);
        txtUsuarioCriador.setDisable(true);
    }

    /**
     * Controla a visibilidade e gerenciamento dos botões conforme o modo atual.
     */
    private void alternarBotoes(boolean modoCriacao) {
        btnSalvar.setVisible(modoCriacao);
        btnSalvar.setManaged(modoCriacao);

        btnAtualizar.setVisible(!modoCriacao);
        btnAtualizar.setManaged(!modoCriacao);

        btnDeletar.setVisible(!modoCriacao);
        btnDeletar.setManaged(!modoCriacao);
    }

    /**
     * Mostra ou oculta os campos relacionados ao estoque conforme o contexto.
     */
    private void toggleCamposEstoque(boolean visivel) {
        setVisibilidadeCampo(lblQuantidade, txtQuantidade, visivel);
        setVisibilidadeCampo(lblValorUnitario, txtValorUnitario, visivel);
        setVisibilidadeCampo(lblEstoqueEmergencial, txtEstoqueEmergencial, visivel);
        setVisibilidadeCampo(lblQuantidadeMinima, txtQuantidadeMinima, visivel);
    }

    /**
     * Aplica visibilidade e gerenciamento a um par rótulo/campo.
     */
    private void setVisibilidadeCampo(Label label, TextField campo, boolean visivel) {
        if (label != null) {
            label.setVisible(visivel);
            label.setManaged(visivel);
        }
        if (campo != null) {
            campo.setVisible(visivel);
            campo.setManaged(visivel);
            if (!visivel) {
                campo.clear();
            }
        }
    }

    /**
     * Fecha a janela atual do formulário.
     */
    private void fecharJanela() {
        Stage janela = (Stage) txtNome.getScene().getWindow();
        janela.close();
    }

    private void configurarCamposNumericos() {
        textFieldRecebeApenasNumerosInteiros(txtQuantidade, 9);
        textFieldRecebeApenasNumerosInteiros(txtQuantidadeMinima, 9);
        textFieldRecebeApenasNumerosInteiros(txtEstoqueEmergencial, 9);
    }

    /**
     * Garante que campos obrigatórios sejam preenchidos antes de salvar.
     */
    private String textoObrigatorio(TextField campo, String nomeCampo) {
        String valor = campo.getText() != null ? campo.getText().trim() : "";
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("Informe " + nomeCampo + ".");
        }
        return valor;
    }
}
