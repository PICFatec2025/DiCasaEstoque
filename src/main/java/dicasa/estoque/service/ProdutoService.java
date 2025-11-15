package dicasa.estoque.service;

import dicasa.estoque.repository.ProdutoRepository;
import org.springframework.stereotype.Service;


import dicasa.estoque.models.Produto;
import dicasa.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    // CREATE - Salvar produto
    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    // READ - Buscar todos os produtos
    public List<Produto> buscarTodos() {
        return produtoRepository.findAll();
    }

    // READ - Buscar por ID
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
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
                    produto.setIdUsuarioCriador(produtoAtualizado.getIdUsuarioCriador());
                    return produtoRepository.save(produto);
                })
                .orElse(null);
    }

    // DELETE - Deletar produto
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}