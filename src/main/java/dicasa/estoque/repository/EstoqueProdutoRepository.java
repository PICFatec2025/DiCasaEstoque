package dicasa.estoque.repository;

import dicasa.estoque.models.entities.EstoqueProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela estoque_produto
 */

@Repository
public interface EstoqueProdutoRepository extends JpaRepository<EstoqueProduto, Long> {
}
