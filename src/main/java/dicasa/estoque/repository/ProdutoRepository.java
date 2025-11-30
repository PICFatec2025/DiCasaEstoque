package dicasa.estoque.repository;

import dicasa.estoque.models.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela produto
 */

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca exata por nome
    Optional<Produto> findByNome(String nome);

    // Busca por nome contendo o texto (case insensitive)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Produto> buscarPorNomeSimilar(@Param("nome") String nome);

    @Query("SELECT DISTINCT p FROM Produto p LEFT JOIN FETCH p.estoqueProduto LEFT JOIN FETCH p.usuario")
    List<Produto> findAllWithEstoqueAndUsuario();

    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.estoqueProduto LEFT JOIN FETCH p.usuario WHERE p.idProduto = :id")
    Optional<Produto> findByIdWithEstoqueAndUsuario(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Produto p LEFT JOIN FETCH p.estoqueProduto LEFT JOIN FETCH p.usuario WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Produto> findAllByNomeContainingIgnoreCaseWithEstoqueAndUsuario(@Param("nome") String nome);
}
