package dicasa.estoque.repository;

import dicasa.estoque.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Inteface que conecta uma tabela do Supabase com o programa
 * Busca a tabela usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Optional<Usuario> findByNomeIgnoreCase(String nome);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}




