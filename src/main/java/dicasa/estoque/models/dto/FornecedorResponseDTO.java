package dicasa.estoque.models.dto;

import java.util.Map;

public record FornecedorResponseDTO(
        Long idFornecedor,
        String cnpj,
        String nomeFantasia,
        String razaoSocial,
        String dataCriacao,
        String dataAtualizacao,
        Long idEndereco,
        String logradouro,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep,
        // REMOVA: String numero,
        Map<Long, String> telefones
) {
}