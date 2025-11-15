package dicasa.estoque.util;

import dicasa.estoque.models.entities.Produto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Classe exemplo do que vai ser o alert que recebe a classe alterada, salva ou excluída no banco de dados
 * e que vai mandar um alert para o JavaFX atualizar a lista
 */

@Getter
public class ProductSavedEvent extends ApplicationEvent {
    private final Produto produto;
    public ProductSavedEvent(Object source, Produto produto) {
        super(source);
        this.produto = produto;
    }
}
