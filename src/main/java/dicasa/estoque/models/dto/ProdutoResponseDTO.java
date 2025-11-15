package dicasa.estoque.models.dto;

public record ProdutoResponseDTO(
        String nome,
        String preco,
        int quantidade,
        String created_at
) {
}
