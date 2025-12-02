package dicasa.estoque.models.dto;

import java.util.List;

/**
 * DTO para criar ou atualizar fornecedor
 */
public record FornecedorRequestDTO(
        String cnpj,
        String nomeFantasia,
        String razaoSocial,
        EnderecoRequestDTO endereco,
        List<String> telefones,
        String email,
        String contato
) {
}