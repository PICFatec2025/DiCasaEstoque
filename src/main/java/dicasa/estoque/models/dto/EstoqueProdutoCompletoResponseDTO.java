package dicasa.estoque.models.dto;

/**
 * DTO que junta tabela Produto e Estoque para exibição na tela
 * @param idProduto
 * @param nome
 * @param marca
 * @param tipo
 * @param dataCriacao
 * @param id_estoque_produto
 * @param quantidade
 * @param quantidadeMinima
 * @param estoqueEmergencial
 * @param status
 * @param statusTexto
 */
public record EstoqueProdutoCompletoResponseDTO(
        Long idProduto,
        String nome,
        String marca,
        String tipo,
        String dataCriacao,
        Long id_estoque_produto,
        int quantidade,
        int quantidadeMinima,
        int estoqueEmergencial,
        int status,
        String statusTexto
) {
}
