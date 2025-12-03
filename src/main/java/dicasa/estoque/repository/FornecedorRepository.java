package dicasa.estoque.repository;

import dicasa.estoque.models.entities.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    // Métodos de busca específica
    List<Fornecedor> findByRazaoSocialContainingIgnoreCase(String razaoSocial);

    List<Fornecedor> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia);

    List<Fornecedor> findByCnpjContaining(String cnpj);

    List<Fornecedor> findByEnderecoFornecedorCidadeContainingIgnoreCase(String cidade);

    List<Fornecedor> findByEnderecoFornecedorUfContainingIgnoreCase(String uf);

    // Busca por telefone (relacionamento indireto)
    @Query("SELECT f FROM Fornecedor f JOIN f.telefones t WHERE t.telefone LIKE %:telefone%")
    List<Fornecedor> findByTelefonesTelefoneContaining(@Param("telefone") String telefone);

    // Método original com @Query
    @Query("SELECT f FROM Fornecedor f WHERE " +
            "LOWER(f.cnpj) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(f.nomeFantasia) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Fornecedor> findByFiltro(@Param("filtro") String filtro);

    // Busca geral em múltiplos campos (incluindo endereço e telefones)
    @Query("SELECT DISTINCT f FROM Fornecedor f " +
            "LEFT JOIN f.enderecoFornecedor e " +
            "LEFT JOIN f.telefones t " +
            "WHERE LOWER(f.cnpj) LIKE LOWER(:termo) OR " +
            "LOWER(f.nomeFantasia) LIKE LOWER(:termo) OR " +
            "LOWER(f.razaoSocial) LIKE LOWER(:termo) OR " +
            "LOWER(e.cidade) LIKE LOWER(:termo) OR " +
            "LOWER(e.uf) LIKE LOWER(:termo) OR " +
            "LOWER(t.telefone) LIKE LOWER(:termo)")
    List<Fornecedor> findByFiltroGeral(@Param("termo") String termo);
}