package dicasa.estoque.util;

import dicasa.estoque.models.entities.Usuario;

/**
 * Classe utilitária para gerenciar o usuário logado.
 */
public class SessionManager {

    private static Usuario usuarioLogado;

    private SessionManager() {
        // Construtor privado para impedir instanciação
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isAdmin() {
        return usuarioLogado != null && Boolean.TRUE.equals(usuarioLogado.getIsAdmin());
    }

    public static void logout() {
        usuarioLogado = null;
    }
}
