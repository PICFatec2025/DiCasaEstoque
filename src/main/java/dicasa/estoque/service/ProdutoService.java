package dicasa.estoque.service;

import dicasa.estoque.repository.EstoqueProdutoRepository;
import dicasa.estoque.repository.FornecedorRepository;
import dicasa.estoque.repository.PedidoProdutoRepository;
import dicasa.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;


import dicasa.estoque.models.entities.Produto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * Classe de service para Produto
 * Conecta os Repositories relacionados a Produto com os controllers de telas de fornecedores
 */

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoProdutoRepository pedidoProdutoRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            PedidoProdutoRepository pedidoProdutoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoProdutoRepository = pedidoProdutoRepository;
    }

    // CREATE - Salvar produto
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    // READ - Buscar todos os produtos
    public List<Produto> buscarTodos() {
        return produtoRepository.findAllWithEstoqueAndUsuario();
    }

    // READ - Buscar por ID
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findByIdWithEstoqueAndUsuario(id);
    }

    public Optional<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNome(nome);
    }
    // Busca por nome (parcial, case insensitive)
    //Pesquisa mapstruck
    public List<Produto> buscarPorNomeParcial(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Busca personalizada
    public List<Produto> buscarPorNomeSimilar(String nome) {
        return produtoRepository.buscarPorNomeSimilar(nome);
    }

    // UPDATE - Atualizar produto
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoAtualizado.getNome());
                    produto.setMarca(produtoAtualizado.getMarca());
                    produto.setTipo(produtoAtualizado.getTipo());
                    produto.setDataAtualizacao(java.time.LocalDateTime.now());
                    produto.setUsuario(produtoAtualizado.getUsuario());
                    return produtoRepository.save(produto);
                })
                .orElse(null);
    }

    // DELETE - Deletar produto
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}