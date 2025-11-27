package dicasa.estoque.repository;

import dicasa.estoque.models.entities.TelefoneFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela telefone_fornecedor
 */
@Repository
public interface TelefoneFornecedorRepository extends JpaRepository<TelefoneFornecedor, Long> {
}
