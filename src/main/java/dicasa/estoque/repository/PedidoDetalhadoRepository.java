package dicasa.estoque.repository;

import dicasa.estoque.models.view.PedidoDetalhadoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Inteface que conecta uma view do Supabase com o programa
 * Busca a view do Pedido já com os dados tratados para exibição na tela
 */
@Repository
public interface PedidoDetalhadoRepository extends JpaRepository<PedidoDetalhadoView,Long> {
}
