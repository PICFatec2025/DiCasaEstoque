package dicasa.estoque.models.dto;

import java.util.Map;

/**
 * DTO que mostra os dados completos do Fornecedor, exibindo telefone e endereço juntos, para ser exibidos em tela
 * @param idFornecedor
 * @param cnpj
 * @param nomeFantasia
 * @param razaoSocial
 * @param dataCriacao
 * @param dataAtualizacao
 * @param idEndereco
 * @param logradouro
 * @param complemento
 * @param bairro
 * @param cidade
 * @param uf
 * @param cep
 * @param telefones
 */
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
        Map<Long, String> telefones
) {
}

