package dicasa.estoque.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public boolean login(String usuario, String senha){
        return usuario.equals("admin") && senha.equals("admin");
    }
}
