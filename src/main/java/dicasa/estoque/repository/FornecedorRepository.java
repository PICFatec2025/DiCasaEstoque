package dicasa.estoque.repository;

import dicasa.estoque.models.entities.Fornecedor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela fornecedor
 */

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

}
