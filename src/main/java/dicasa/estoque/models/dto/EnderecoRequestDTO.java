package dicasa.estoque.models.dto;

/**
 * DTO para endereço
 */
public record EnderecoRequestDTO(
        String logradouro,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
}