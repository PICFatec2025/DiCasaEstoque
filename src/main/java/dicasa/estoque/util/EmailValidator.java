package dicasa.estoque.util;

import java.util.regex.Pattern;

/**
 * Classe que valida se o email está no padrão válido
 */
public class EmailValidator {
    /**
     * Regex que tem o padrão do e-mail válido
     */
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Função que vê se o e-mail está seguindo os padrões estabelecidos no regex
     * @param email a ser verficiado
     * @return se está válido (true) ou não (false)
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
