package dicasa.estoque.service;

import dicasa.estoque.models.dto.ProdutoResponseDTO;
import dicasa.estoque.models.entities.Produto;
import dicasa.estoque.models.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que vai fazer ligação entre o controller e o Banco de dados
 * atualmente ele simula, tendo uma lista interna (algo provisório
 */

@Service
public class ProdutoService {
    /**
     * Lista apenas  para testes
     */
    private List<Produto> produtos= new ArrayList<Produto>();
    private final ProductMapper productMapper;
    public ProdutoService(ProductMapper productMapper) {
        this.productMapper = productMapper;
        produtos.add(
                new Produto(
                        1L,
                        "Macarrão",
                        new BigDecimal("40.8"),
                        20,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
        produtos.add(
                new Produto(
                        2L,
                        "Arroz",
                        new BigDecimal("23.8"),
                        40,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
    }

    /**
     * Função que retorna uma lista de dto do produto
     * @return
     */
    public List<ProdutoResponseDTO> findAll(){
        return produtos.stream()
                .map(productMapper::toDto)
                .toList();
    }
}
