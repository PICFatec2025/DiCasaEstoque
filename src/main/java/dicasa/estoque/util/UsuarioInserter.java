package dicasa.estoque.util;

import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.service.UsuarioService;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Classe utilitária para criar novos usuários no sistema.
 * Sempre cria usuários com isAdmin = false.
 */
@Component
public class UsuarioInserter implements CommandLineRunner {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioInserter(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {
    }

    /**
     * Cria um usuário novo com isAdmin = false.
     */
    private void criarUsuario(String nome, String email, String senha) {
        boolean criado = usuarioService.criarUsuario(nome, email, senha);
        if (criado) {
            System.out.println("Usuário " + nome + " criado com sucesso!");
        } else {
            System.out.println("Não foi possível criar usuário " + nome + ". Nome ou email já existem.");
        }
    }
}
