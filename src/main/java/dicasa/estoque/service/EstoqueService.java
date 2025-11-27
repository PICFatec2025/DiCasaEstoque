package dicasa.estoque.service;

import dicasa.estoque.csv.CSVExporter;
import dicasa.estoque.exception.EstoqueNaoEncotradoException;
import dicasa.estoque.models.dto.EstoqueProdutoCompletoResponseDTO;
import dicasa.estoque.models.dto.EstoqueProdutoRequestDTO;
import dicasa.estoque.models.dto.EstoqueProdutoResponseDTO;
import dicasa.estoque.models.entities.EstoqueProduto;
import dicasa.estoque.models.entities.Produto;
import dicasa.estoque.models.mapper.ProdutoEstoqueMapper;
import dicasa.estoque.repository.EstoqueProdutoRepository;
import dicasa.estoque.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de service para o estoque do produto
 * Conecta os Repositories relacionados a estoque do Produto com os controllers de telas de estoque
 */

@Slf4j
@Service
public class EstoqueService {
    private final ProdutoRepository produtoRepository;
    private final EstoqueProdutoRepository estoqueProdutoRepository;
    private final ProdutoEstoqueMapper produtoEstoqueMapper;
    private final CSVExporter csvExporter;
    private final ApplicationEventPublisher eventPublisher;

    public EstoqueService(
            ProdutoRepository produtoRepository,
            EstoqueProdutoRepository estoqueProdutoRepository,
            ProdutoEstoqueMapper produtoEstoqueMapper,
            CSVExporter csvExporter,
            ApplicationEventPublisher eventPublisher) {
        this.produtoRepository = produtoRepository;
        this.estoqueProdutoRepository = estoqueProdutoRepository;
        this.produtoEstoqueMapper = produtoEstoqueMapper;
        this.csvExporter = csvExporter;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Função que lista todos o Estoque incluindo o Produto vinculado a ele
     * @return lista de estoque com produto
     */
    public List<EstoqueProdutoCompletoResponseDTO> listarEstoques(){
        List<Produto> produtos = produtoRepository.findAllWithEstoqueAndUsuario();
        return produtoEstoqueMapper.toDtoList(produtos);
    }

    /**
     * Exporta a lista de estoque para CSV
     * @return a mensagem de êxito ou de erro
     */
    public String exportarEstoquesEmCSV(){
        List<EstoqueProdutoCompletoResponseDTO> produtos = listarEstoques();
        return csvExporter.exportarEstoqueEmCSV(produtos);
    }

    /**
     * busca o Estoque pelo id
     * @param id o id desse estoque
     * @return o DTO do estoque
     */
    public EstoqueProdutoResponseDTO acharEstoquePorId(Long id){
        EstoqueProduto estoqueProduto =
                acharEstoqueProdutoPorId(id);
        return produtoEstoqueMapper.toEstoqueProdutoResponseDTO(estoqueProduto);
    }

    /**
     * Função interna que busca o estoque pelo id, que vai ser utilizado por outras funções
     * Joga exception de Estoque não encontrado casa haja falha
     * @param id id a ser buscado
     * @return o estoque
     */
    private EstoqueProduto acharEstoqueProdutoPorId(Long id){
        return estoqueProdutoRepository
                .findById(id)
                .orElseThrow(EstoqueNaoEncotradoException::new);
    }

    /**
     * Função que atualiza o Estoque
     * @param estoqueProdutoRequestDTO o estoque a ser atualizado
     * @return o estoque atualizado, para gerar um alert de atualização no sistema
     */
    public EstoqueProduto editarEstoque(EstoqueProdutoRequestDTO estoqueProdutoRequestDTO){
        EstoqueProduto estoqueProduto =
                acharEstoqueProdutoPorId(
                        estoqueProdutoRequestDTO.id_estoque_produto()
                );
        produtoEstoqueMapper.atualizaDoDTO(estoqueProdutoRequestDTO, estoqueProduto);
        EstoqueProduto saved = estoqueProdutoRepository.save(estoqueProduto);
        return saved;

    }

    /**
     * Realiza a saída de itens do estoque de um produto específico.
     *
     * @param idProduto  identificador do produto
     * @param quantidade quantidade a ser retirada
     * @return estoque atualizado após a retirada
     */
    public EstoqueProduto retirarDoEstoque(Long idProduto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Produto produto = produtoRepository.findByIdWithEstoqueAndUsuario(idProduto)
                .orElseThrow(EstoqueNaoEncotradoException::new);

        EstoqueProduto estoqueProduto = produto.getEstoqueProduto();
        if (estoqueProduto == null) {
            throw new EstoqueNaoEncotradoException();
        }

        if (quantidade > estoqueProduto.getQuantidade()) {
            throw new IllegalArgumentException("Quantidade solicitada maior que o estoque disponível.");
        }

        estoqueProduto.setQuantidade(estoqueProduto.getQuantidade() - quantidade);
        estoqueProduto.setData_atualizacao(LocalDateTime.now());

        return estoqueProdutoRepository.save(estoqueProduto);
    }
}