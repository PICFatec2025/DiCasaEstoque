package dicasa.estoque.utils;

public class CnpjValidator {
    public static boolean isValid(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) return false;

        // Implemente a validação do dígito verificador
        // ...

        return true;
    }
}