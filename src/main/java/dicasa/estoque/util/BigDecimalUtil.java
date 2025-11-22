package dicasa.estoque.util;

import java.math.BigDecimal;

/**
 * Classe que converte String em BigDecimal, usado para variáveies de número
 */
public class BigDecimalUtil {
    public static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        // Como já foi validado pelo TextFormatter, basta substituir vírgula por ponto
        String normalizedValue = value.replace(',', '.');

        try {
            return new BigDecimal(normalizedValue);
        } catch (NumberFormatException e) {
            return null; // Isso não deve acontecer devido à validação anterior
        }
    }
}
