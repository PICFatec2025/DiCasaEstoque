package dicasa.estoque.controller.produtos;

import dicasa.estoque.models.Produto;
import dicasa.estoque.service.ProdutoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Componentes FXML (ajuste os IDs conforme seu arquivo FXML)
    @FXML private TextField txtNome;
    @FXML private TextField txtMarca;
    @FXML private TextField txtTipo;
    @FXML private TextField txtUsuarioCriador;
    @FXML private TextField txtBusca;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Long> colunaId;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, String> colunaMarca;
    @FXML private TableColumn<Produto, String> colunaTipo;
    @FXML private Label lblMensagem;


    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar colunas da tabela
        colunaId.setCellValueFactory(cellData -> cellData.getValue().idProdutoProperty().asObject());
        colunaNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colunaMarca.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
        colunaTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        carregarProdutos();
    }

    // Botão Salvar
    @FXML
    private void salvarProduto() {
        try {
            Produto produto = new Produto();
            produto.setNome(txtNome.getText());
            produto.setMarca(txtMarca.getText());
            produto.setTipo(txtTipo.getText());
            produto.setIdUsuarioCriador(Integer.parseInt(txtUsuarioCriador.getText()));
            produto.setDataCriacao(LocalDateTime.now());

            produtoService.salvarProduto(produto);
            limparCampos();
            carregarProdutos();
            lblMensagem.setText("Produto salvo com sucesso!");
        } catch (Exception e) {
            lblMensagem.setText("Erro ao salvar produto: " + e.getMessage());
        }
    }

    // Botão Atualizar
    @FXML
    private void atualizarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                produtoSelecionado.setNome(txtNome.getText());
                produtoSelecionado.setMarca(txtMarca.getText());
                produtoSelecionado.setTipo(txtTipo.getText());
                produtoSelecionado.setDataAtualizacao(LocalDateTime.now());

                produtoService.salvarProduto(produtoSelecionado);
                carregarProdutos();
                lblMensagem.setText("Produto atualizado com sucesso!");
            } catch (Exception e) {
                lblMensagem.setText("Erro ao atualizar produto: " + e.getMessage());
            }
        }
    }

    // Botão Deletar
    @FXML
    private void deletarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            produtoService.deletarProduto(produtoSelecionado.getIdProduto());
            carregarProdutos();
            limparCampos();
            lblMensagem.setText("Produto deletado com sucesso!");
        }
    }

    // Carregar produtos na tabela
    private void carregarProdutos() {
        try{
            listaProdutos.clear();
            List<Produto> todosProdutos = produtoService.buscarTodos();
            listaProdutos.addAll(todosProdutos);
            tabelaProdutos.setItems(listaProdutos);

            if(txtBusca.getText().isEmpty()) {
                lblMensagem.setText("Total: " + todosProdutos.size() + " produto(s)");
            }
        }catch (Exception e){
            lblMensagem.setText("Erro no1 carregamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Limpar campos
    private void limparCampos() {
        txtNome.clear();
        txtMarca.clear();
        txtTipo.clear();
        txtUsuarioCriador.clear();
    }
    @FXML
    private void buscarPorNome() {
        try {
            String nomeBusca = txtBusca.getText().trim();


            // Buscar produtos com nome similar
            List<Produto> resultados = produtoService.buscarPorNomeParcial(nomeBusca);

            if (resultados.isEmpty()) {
                lblMensagem.setText("Nenhum produto encontrado com: '" + nomeBusca + "'");
                lblMensagem.setText("Nenhum resultado encontrado!");
                // Limpa a tabela se não encontrar resultados
                listaProdutos.clear();
                tabelaProdutos.setItems(listaProdutos);
            } else {
                // Atualiza a tabela com os resultados da busca
                listaProdutos.clear();
                listaProdutos.addAll(resultados);
                tabelaProdutos.setItems(listaProdutos);

                lblMensagem.setText(resultados.size() + " produto(s) encontrado(s) com: '" + nomeBusca + "'");
                lblMensagem.setText("Busca realizada com sucesso!");

                // Seleciona o primeiro resultado automaticamente
                if (!resultados.isEmpty()) {
                    tabelaProdutos.getSelectionModel().selectFirst();
                    selecionarProduto(); // Preenche os campos com o primeiro resultado
                }
            }

        } catch (Exception e) {
            lblMensagem.setText("Erro na busca: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void carregarTodosProdutos() {
        carregarProdutos();
        txtBusca.clear();
        lblMensagem.setText("Mostrando todos os produtos");
        lblMensagem.setText("Lista completa carregada!");
    }
    // Quando selecionar um produto na tabela
    @FXML
    private void selecionarProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            txtNome.setText(produtoSelecionado.getNome());
            txtMarca.setText(produtoSelecionado.getMarca());
            txtTipo.setText(produtoSelecionado.getTipo());
            txtUsuarioCriador.setText(String.valueOf(produtoSelecionado.getIdUsuarioCriador()));
        }
    }

    // Botão Limpar
    @FXML
    private void limparCamposAction() {
        limparCampos();
        tabelaProdutos.getSelectionModel().clearSelection();
        lblMensagem.setText("Campos limpos!");
    }
}