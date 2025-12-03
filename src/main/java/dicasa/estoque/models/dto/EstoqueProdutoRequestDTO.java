package dicasa.estoque.models.dto;

import java.time.LocalDateTime;

/**
 * DTO que envia os dados do estoque para ser criado/atualizado no Banco de dados
 * @param id_estoque_produto
 * @param quantidade
 * @param quantidadeMinima
 * @param estoqueEmergencial
 * @param data_atualizacao
 */
public record EstoqueProdutoRequestDTO (
        Long id_estoque_produto,
        int quantidade,
        int quantidadeMinima,
        int estoqueEmergencial,
        LocalDateTime data_atualizacao
){
}
