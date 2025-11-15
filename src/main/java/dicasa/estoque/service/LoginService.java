package dicasa.estoque.service;

import org.springframework.stereotype.Service;

/**
 * Uma classe exemplo de como vai funcionar o service, uma classe que conecta o Controller com o Banco de dados
 */
@Service
public class LoginService {
    public boolean login(String usuario, String senha){
        return usuario.equals("admin") && senha.equals("admin");
    }
}
