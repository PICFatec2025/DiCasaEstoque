package dicasa.estoque.util;

import java.util.*;

/**
 * Utilitário para gerenciar o histórico de logins e fornecer sugestões de autocomplete.
 * Armazena os usuários que fizeram login com sucesso e fornece busca eficiente.
 */
public class LoginHistory {
    private static final int MAX_HISTORY_SIZE = 10;
    private static Set<String> loginHistory = new LinkedHashSet<>();

    // Classe interna para gerenciar o histórico com limite de tamanho
    private static class LimitedLinkedHashSet extends LinkedHashSet<String> {
        @Override
        public boolean add(String element) {
            boolean added = super.add(element);
            if (size() > MAX_HISTORY_SIZE) {
                // Remove o elemento mais antigo
                Iterator<String> iterator = iterator();
                if (iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }
            }
            return added;
        }
    }

    static {
        // Inicializa o conjunto com a implementação limitada
        loginHistory = new LimitedLinkedHashSet();
    }

    private LoginHistory() {
        // Classe utilitária - não deve ser instanciada
    }

    /**
     * Adiciona um usuário ao histórico de logins
     * @param usuario Nome de usuário ou email a ser adicionado ao histórico
     */
    public static void addToHistory(String usuario) {
        if (usuario != null && !usuario.trim().isEmpty()) {
            // Remove se já existir para adicionar no final (mais recente)
            loginHistory.remove(usuario);
            loginHistory.add(usuario.trim().toLowerCase());
        }
    }

    /**
     * Obtém o histórico de logins filtrado por um termo de busca
     * @param searchTerm Termo para filtrar os resultados (case insensitive)
     * @return Lista de sugestões ordenadas por relevância
     */
    public static List<String> getFilteredHistory(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>(loginHistory);
        }

        String term = searchTerm.trim().toLowerCase();
        List<String> suggestions = new ArrayList<>();

        // Primeiro: usuários que começam com o termo (maior relevância)
        for (String user : loginHistory) {
            if (user.toLowerCase().startsWith(term)) {
                suggestions.add(user);
            }
        }

        // Segundo: usuários que contém o termo em qualquer posição
        for (String user : loginHistory) {
            if (user.toLowerCase().contains(term) && !suggestions.contains(user)) {
                suggestions.add(user);
            }
        }

        return suggestions.stream().limit(3).toList();
    }

    /**
     * Limpa todo o histórico de logins
     */
    public static void clearHistory() {
        loginHistory.clear();
    }

    /**
     * Obtém todo o histórico de logins
     * @return Lista com todos os usuários do histórico
     */
    public static List<String> getFullHistory() {
        return new ArrayList<>(loginHistory);
    }
}