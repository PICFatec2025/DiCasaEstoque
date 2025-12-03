package dicasa.estoque.util;

import java.util.*;

/**
 * Utilitário universal para gerenciar histórico de autocomplete.
 * Pode ser usado em QUALQUER projeto JavaFX sem modificações.
 */
public class AutoCompleteHistory {
    private static final int MAX_HISTORY_SIZE = 10;
    private static final LinkedHashSet<String> history = new LinkedHashSet<>();

    private AutoCompleteHistory() {
        // Classe utilitária - não deve ser instanciada
    }

    /**
     * Adiciona um item ao histórico
     */
    public static void add(String item) {
        if (item != null && !item.trim().isEmpty()) {
            String itemLower = item.trim().toLowerCase();

            // Remove se já existir para adicionar no final (mais recente)
            history.remove(itemLower);
            history.add(itemLower);

            // Mantém o tamanho máximo
            if (history.size() > MAX_HISTORY_SIZE) {
                Iterator<String> iterator = history.iterator();
                iterator.next();
                iterator.remove();
            }
        }
    }

    /**
     * Busca itens do histórico que correspondam ao termo
     */
    public static List<String> search(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>(history);
        }

        String term = searchTerm.trim().toLowerCase();
        List<String> suggestions = new ArrayList<>();

        // Ordem de relevância:
        // 1. Itens que começam com o termo
        for (String item : history) {
            if (item.startsWith(term)) {
                suggestions.add(item);
            }
        }

        // 2. Itens que contém o termo
        for (String item : history) {
            if (item.contains(term) && !suggestions.contains(item)) {
                suggestions.add(item);
            }
        }

        return suggestions.stream().limit(3).toList();
    }

    /**
     * Limpa o histórico
     */
    public static void clear() {
        history.clear();
    }

    /**
     * Obtém todo o histórico
     */
    public static List<String> getAll() {
        return new ArrayList<>(history);
    }
}