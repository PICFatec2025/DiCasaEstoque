package dicasa.estoque.repository;

import dicasa.estoque.models.entities.EnderecoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela endereco_fornecedor
 */

@Repository
public interface EnderecoFornecedorRepository extends JpaRepository<EnderecoFornecedor, Long> {
}
