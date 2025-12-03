package dicasa.estoque.util;

import java.math.BigDecimal;

/**
 * Classe que faz as validações de número
 */
public class NumberValidatorUtil {
    /**
     * Função que converte String em BigDecimal, usado para variáveies de número
     * @param value recebe o texto em String
     * @return o número em BigDecimal
     */
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

    /**
     * Função que valida String para um int
     * @param valor string que deve ser um número inteiro
     * @return retorna um int se for um valor válido
     */
    public static int ehUmNumeroInteiroPositivo(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new NumberFormatException();
        }
        String valorTrimmed = valor.trim();
        // Verifica se contém apenas dígitos (não permite sinal negativo, decimal, etc)
        if (!valorTrimmed.matches("\\d+")) {
            throw new NumberFormatException();
        }
        try {
            // Tenta converter para int
            int numero = Integer.parseInt(valorTrimmed);
            // Verifica se é positivo ou zero
            if(numero <0){
                throw new NumberFormatException();
            };
            return numero;

        } catch (NumberFormatException e) {
            // Isso não deve acontecer devido à regex, mas por segurança
            throw new NumberFormatException();
        }
    }
}
