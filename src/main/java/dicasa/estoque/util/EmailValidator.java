package dicasa.estoque.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Classe utilitária para validação de e-mails.
 * Valida formato básico e fornece validação opcional de domínio.
 */
public class EmailValidator {

    /**
     * Regex melhorada para formato de e-mail baseada na RFC 5322
     * Mais flexível e compatível com emails corporativos
     */
    private static final String EMAIL_REGEX =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Lista de domínios de email gratuitos/comuns (para validação opcional)
     * Útil se quiser restringir cadastros apenas a emails corporativos
     */
    private static final Set<String> DOMINIOS_EMAILS_GRATUITOS = new HashSet<>(Arrays.asList(
            "gmail.com", "gmail.com.br",
            "hotmail.com", "hotmail.com.br",
            "outlook.com", "outlook.com.br",
            "live.com", "live.com.br",
            "yahoo.com", "yahoo.com.br",
            "yahoo.com.ar", "yahoo.com.mx",
            "uol.com.br",
            "bol.com.br",
            "terra.com.br",
            "ig.com.br",
            "icloud.com",
            "aol.com",
            "zoho.com",
            "protonmail.com",
            "tutanota.com",
            "mail.com",
            "yandex.com",
            "gmx.com"
    ));

    /**
     * Extensões de TLD comuns (para validação opcional)
     */
    private static final Set<String> EXTENSOES_VALIDAS = new HashSet<>(Arrays.asList(
            "com", "com.br", "com.ar", "com.mx",
            "org", "org.br",
            "net", "net.br",
            "gov", "gov.br",
            "edu", "edu.br",
            "mil", "mil.br",
            "br",
            "ar",
            "mx",
            "us",
            "uk",
            "ca",
            "au",
            "de",
            "fr",
            "it",
            "es",
            "pt",
            "io",
            "dev",
            "app",
            "site",
            "online",
            "tech",
            "digital",
            "cloud"
    ));

    /**
     * Valida se o e-mail informado tem formato válido.
     * Esta é a validação PRINCIPAL que você deve usar.
     *
     * @param email Email a ser validado
     * @return true se o formato for válido, false caso contrário
     */
    public static boolean isValidFormat(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        String trimmed = email.trim().toLowerCase();

        // Validação básica de comprimento
        if (trimmed.length() > 254) { // RFC 5321 limita para 254 caracteres
            return false;
        }

        // Validação do formato
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            return false;
        }

        // Validação adicional: não pode começar ou terminar com ponto
        String parteLocal = trimmed.substring(0, trimmed.indexOf("@"));
        if (parteLocal.startsWith(".") || parteLocal.endsWith(".")) {
            return false;
        }

        // Validação adicional: não pode ter dois pontos consecutivos
        if (parteLocal.contains("..")) {
            return false;
        }

        return true;
    }

    /**
     * Verifica se o email é de um domínio de email gratuito (gmail, hotmail, etc.)
     * Útil se quiser restringir cadastros apenas a emails corporativos.
     *
     * @param email Email a ser verificado
     * @return true se for email gratuito, false se for corporativo ou desconhecido
     */
    public static boolean isFreeEmail(String email) {
        if (!isValidFormat(email)) {
            return false;
        }

        String trimmed = email.trim().toLowerCase();
        String dominio = trimmed.substring(trimmed.indexOf("@") + 1);

        return DOMINIOS_EMAILS_GRATUITOS.contains(dominio);
    }

    /**
     * Verifica se o email tem uma extensão (TLD) válida.
     * Útil para filtrar emails com extensões suspeitas.
     *
     * @param email Email a ser verificado
     * @return true se a extensão for válida, false caso contrário
     */
    public static boolean hasValidExtension(String email) {
        if (!isValidFormat(email)) {
            return false;
        }

        String trimmed = email.trim().toLowerCase();
        String dominio = trimmed.substring(trimmed.indexOf("@") + 1);

        // Extrai a última parte (extensão)
        String[] partesDominio = dominio.split("\\.");
        if (partesDominio.length < 2) {
            return false;
        }

        String extensao = partesDominio[partesDominio.length - 1];
        String extensaoCompleta = partesDominio.length > 2 ?
                partesDominio[partesDominio.length - 2] + "." + extensao :
                extensao;

        return EXTENSOES_VALIDAS.contains(extensao) ||
                EXTENSOES_VALIDAS.contains(extensaoCompleta);
    }

    /**
     * Validação COMPLETA para sistemas corporativos.
     * Aceita qualquer email com formato válido, mas pode configurar restrições.
     *
     * @param email Email a ser validado
     * @param aceitarEmailsGratuitos Se true, aceita gmail/hotmail/etc. Se false, só aceita corporativos
     * @param validarExtensao Se true, valida a extensão do domínio
     * @return true se for válido de acordo com os parâmetros
     */
    public static boolean isValidForBusiness(String email, boolean aceitarEmailsGratuitos, boolean validarExtensao) {
        if (!isValidFormat(email)) {
            return false;
        }

        if (validarExtensao && !hasValidExtension(email)) {
            return false;
        }

        if (!aceitarEmailsGratuitos && isFreeEmail(email)) {
            return false;
        }

        return true;
    }

    /**
     * Validação SIMPLIFICADA para uso geral no sistema.
     * Aceita qualquer email com formato válido (recomendado para fornecedores).
     *
     * @param email Email a ser validado
     * @return true se o formato for válido
     */
    public static boolean isValid(String email) {
        return isValidFormat(email);
    }

    /**
     * Extrai o domínio do email (parte após o @)
     *
     * @param email Email válido
     * @return Domínio do email ou null se inválido
     */
    public static String extractDomain(String email) {
        if (!isValidFormat(email)) {
            return null;
        }

        String trimmed = email.trim().toLowerCase();
        return trimmed.substring(trimmed.indexOf("@") + 1);
    }

    /**
     * Extrai a parte local do email (parte antes do @)
     *
     * @param email Email válido
     * @return Parte local do email ou null se inválido
     */
    public static String extractLocalPart(String email) {
        if (!isValidFormat(email)) {
            return null;
        }

        String trimmed = email.trim().toLowerCase();
        return trimmed.substring(0, trimmed.indexOf("@"));
    }

    /**
     * Normaliza o email (trim + lowercase)
     *
     * @param email Email a ser normalizado
     * @return Email normalizado ou null se inválido
     */
    public static String normalize(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        String trimmed = email.trim().toLowerCase();
        return isValidFormat(trimmed) ? trimmed : null;
    }

    /**
     * Mantém compatibilidade com o nome antigo
     * (Chamando a validação principal).
     *
     * @param email Email a ser validado
     * @return true se for válido
     */
    public static boolean isValidEmail(String email) {
        return isValidFormat(email);
    }
}
