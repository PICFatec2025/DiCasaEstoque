package dicasa.estoque.repository;

import dicasa.estoque.models.entities.PedidoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela pedido_produto
 */
@Repository
public interface PedidoProdutoRepository extends JpaRepository<PedidoProduto, Long> {
}
