package dicasa.estoque.service;

import dicasa.estoque.models.entities.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Uma classe de exemplo de como vai funcionar o service, uma classe que conecta o Controller com o Banco de dados
 */
@Service
public class UsuarioService {
    private List<Usuario> listUsuarios = new ArrayList<>();
    public UsuarioService() {
        listUsuarios.add(new Usuario(
                1L,
                "admin",
                "admin@gmail.com",
                "admin",
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    /**
     * Função para fazer o login
     * Verifica se o usuário e senha estão no sistema
     * @param usuario
     * @param senha
     * @return
     */

    public boolean login(String usuario, String senha){
        return usuario.equals("admin") && senha.equals("admin");
    }

    /**
     * Função que verifica se há algum usuário com essa senha no sistema
     * @param email
     * @return
     */
    public boolean buscarUsuario(String email){
        return email.equals("admin@gmail.com");
    }
}
