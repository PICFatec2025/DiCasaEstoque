package dicasa.estoque.util;

import dicasa.estoque.models.entities.Produto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductSavedEvent extends ApplicationEvent {
    private final Produto produto;
    public ProductSavedEvent(Object source, Produto produto) {
        super(source);
        this.produto = produto;
    }
}
