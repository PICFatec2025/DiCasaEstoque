package dicasa.estoque.models.dto;

import dicasa.estoque.models.entities.EstoqueProduto;
import dicasa.estoque.models.entities.Produto;
import dicasa.estoque.models.entities.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record ProdutoEstoqueResponseDTO(
        Long idProduto,
        String nome,
        String marca,
        String tipo,
        String dataCriacao,
        Long id_estoque_produto,
        int quantidade,
        int quantidadeMinima,
        int estoqueEmergencial
) {
}
