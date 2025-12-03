package dicasa.estoque.util;

import dicasa.estoque.models.entities.EstoqueProduto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Classe que avisa ao controller que um EstoqueProduto foi alterado
 */

@Getter
public class EstoqueProdutoSavedEvent extends ApplicationEvent {
    private final EstoqueProduto estoqueProduto;
    public EstoqueProdutoSavedEvent(Object source, EstoqueProduto estoqueProduto) {
        super(source);
        this.estoqueProduto = estoqueProduto;
    }
}
