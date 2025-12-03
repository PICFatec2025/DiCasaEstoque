package dicasa.estoque.models.mapper;

import dicasa.estoque.models.dto.EstoqueProdutoCompletoResponseDTO;
import dicasa.estoque.models.dto.EstoqueProdutoRequestDTO;
import dicasa.estoque.models.dto.EstoqueProdutoResponseDTO;
import dicasa.estoque.models.entities.EstoqueProduto;
import dicasa.estoque.models.entities.Produto;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper que gerencia o mapeamento de Produto e seu Estoque
 */
@Mapper(componentModel = "spring")
public interface ProdutoEstoqueMapper {
    /**
     * Função que mapeia o Produto em seu DTO
     * @param produto produto a ser convertido
     * @return ProdutoDTO, que inclui os dados de Estoque
     */
    @Mapping(source = "estoqueProduto.id_estoque_produto", target = "id_estoque_produto")
    @Mapping(source = "estoqueProduto.quantidade", target = "quantidade")
    @Mapping(source = "estoqueProduto.quantidadeMinima", target = "quantidadeMinima")
    @Mapping(source = "estoqueProduto.estoqueEmergencial", target = "estoqueEmergencial")
    @Mapping(target = "status", expression = "java(mapStatus(produto))")
    @Mapping(target = "statusTexto", expression = "java(mapStatusTexto(produto))")
    EstoqueProdutoCompletoResponseDTO toEstoqueProdutoDto(Produto produto);

    /**
     * Converte lista de Produto na lista de DTO
     * @param produtos lista de produtos
     * @return lista de DTO
     */
    default List<EstoqueProdutoCompletoResponseDTO> toDtoList(List<Produto> produtos) {
        return produtos.stream()
                .map(this::toEstoqueProdutoDto)
                .collect(Collectors.toList());
    }

    /**
     * São funções que previnem o programa de ter bug caso não haja um estoque vinculado ao produto
     * @param produto que tem ou não um estoque vinculado
     * @return o status, se não tem estoque, status como zero
     */
    default int calcularStatus(Produto produto) {
        if (produto.getEstoqueProduto() == null) {
            return 0;
        }
        return produto.getEstoqueProduto().status();
    }
    /**
     * São funções que previnem o programa de ter bug caso não haja um estoque vinculado ao produto
     * @param produto que tem ou não um estoque vinculado
     * @return o texto do status, se não tem estoque, retorna como "zerado"
     */
    default String obterStatusTexto(Produto produto) {
        if (produto.getEstoqueProduto() == null) {
            return "Zerado";
        }
        return produto.getEstoqueProduto().statusTexto();
    }

    /**
     * Converte o ResponseDTO em sua entidade
     * @param estoqueProdutoDTO o estoque com os dados do formulário
     * @return na entidade Estoque já convertida
     */
    EstoqueProduto toEstoqueProduto(EstoqueProdutoResponseDTO estoqueProdutoDTO);

    /**
     * Função que converte entidade Estoque em seu DTO
     * @param estoqueProduto estoque a ser convertido
     * @return seu DTO a ser exibido
     */
    EstoqueProdutoResponseDTO toEstoqueProdutoResponseDTO(EstoqueProduto estoqueProduto);
    default int parseInt(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " não pode estar vazio");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " deve ser um número válido: " + value);
        }
    }

    /**
     * Funçao que recebe o DTO, e compara com a entidade
     * caso haja alguma atualização do DTO, ele atualiza a entidade
     * @param dto
     * @param estoqueProduto
     */
    @Mapping(target = "id_estoque_produto")
    void atualizaDoDTO(EstoqueProdutoRequestDTO dto,@MappingTarget EstoqueProduto estoqueProduto);

    @Named("estoqueToId")
    default Long estoqueToId(EstoqueProduto estoque) {
        return estoque != null ? estoque.getId_estoque_produto() : null;
    }
    /**
     * Converte o produto em um status numérico considerando a ausência de estoque.
     *
     * @param produto produto a ser avaliado
     * @return status calculado ou 0 quando o estoque não existe
     */
    default int mapStatus(Produto produto) {
        EstoqueProduto estoque = produto != null ? produto.getEstoqueProduto() : null;
        return estoque != null ? estoque.status() : 0;
    }

    /**
     * Converte o produto em um texto de status tratando estoque ausente.
     *
     * @param produto produto a ser avaliado
     * @return texto do status ou mensagem padrão quando o estoque não existe
     */
    default String mapStatusTexto(Produto produto) {
        EstoqueProduto estoque = produto != null ? produto.getEstoqueProduto() : null;
        return estoque != null ? estoque.statusTexto() : "Sem estoque";
    }

    /**
     * Função que trata se o atributo quantidade estiver nulo
     * @param estoque a ser tratado
     * @return o valor de quantidade ou 0 se estiver nulo
     */
    @Named("estoqueToQuantidade")
    default int estoqueToQuantidade(EstoqueProduto estoque) {
        return estoque != null ? estoque.getQuantidade() : 0;
    }

    /**
     * Função que trata se o atributo quantidade estiver nulo
     * @param estoque a ser tratado
     * @return o valor de quantidade mínima ou 0 se estiver nulo
     */
    @Named("estoqueToMinimo")
    default int estoqueToMinimo(EstoqueProduto estoque) {
        return estoque != null ? estoque.getQuantidadeMinima() : 0;
    }

    /**
     * Função que trata se o atributo quantidade estiver nulo
     * @param estoque a ser tratado
     * @return o valor de estoque emergencial ou 0 se estiver nulo
     */
    @Named("estoqueToEmergencial")
    default int estoqueToEmergencial(EstoqueProduto estoque) {
        return estoque != null ? estoque.getEstoqueEmergencial() : 0;
    }
}
