package dicasa.estoque.models.dto;

/**
 * DTO que exibe os dados de estoque na tela
 * @param id_estoque_produto
 * @param quantidade
 * @param quantidadeMinima
 * @param estoqueEmergencial
 */
public record EstoqueProdutoResponseDTO(
        Long id_estoque_produto,
        String quantidade,
        String quantidadeMinima,
        String estoqueEmergencial
) {
}
