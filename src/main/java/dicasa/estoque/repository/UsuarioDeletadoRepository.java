package dicasa.estoque.repository;

import dicasa.estoque.models.entities.UsuarioDeletado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDeletadoRepository extends JpaRepository<UsuarioDeletado, Long> {
}
