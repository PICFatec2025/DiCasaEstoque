package dicasa.estoque.repository;

import dicasa.estoque.models.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela pedido
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
