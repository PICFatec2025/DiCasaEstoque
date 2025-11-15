package dicasa.estoque.util;

import lombok.Getter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Classe auxiliar para mandar o context da tela atual
 */

@Component
public class SpringFXManager {
    @Getter
    private static ConfigurableApplicationContext context;

    public SpringFXManager(ConfigurableApplicationContext context) {
        SpringFXManager.context = context;
    }
}
