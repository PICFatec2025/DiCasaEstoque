package dicasa.estoque.models.dto;

/**
 * Apenas um DTO utilizado para receber os dados do Produto e converter para um String e facilitar a leitura na tela
 * @param id
 * @param nome
 * @param preco
 * @param quantidade
 * @param created_at
 */
public record ProdutoResponseDTO(
        Long id,
        String nome,
        String preco,
        String quantidade,
        String created_at
) {
}
