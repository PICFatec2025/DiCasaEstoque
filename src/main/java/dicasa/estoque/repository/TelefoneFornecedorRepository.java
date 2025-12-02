package dicasa.estoque.repository;

import dicasa.estoque.models.entities.TelefoneFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TelefoneFornecedorRepository extends JpaRepository<TelefoneFornecedor, Long> {

    @Query("SELECT t FROM TelefoneFornecedor t WHERE t.fornecedor.idFornecedor = :fornecedorId")
    List<TelefoneFornecedor> findByFornecedorId(@Param("fornecedorId") Long fornecedorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TelefoneFornecedor t WHERE t.fornecedor.idFornecedor = :fornecedorId")
    void deleteByFornecedorId(@Param("fornecedorId") Long fornecedorId);
}