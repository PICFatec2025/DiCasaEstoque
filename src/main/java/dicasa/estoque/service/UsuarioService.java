package dicasa.estoque.service;

import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.models.entities.UsuarioDeletado;
import dicasa.estoque.repository.UsuarioDeletadoRepository;
import dicasa.estoque.repository.UsuarioRepository;
import dicasa.estoque.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviço responsável pelas operações relacionadas aos usuários do sistema,
 * incluindo autenticação, criação, atualização de perfil, redefinição de senha
 * e exclusão lógica (com salvamento em tabela de auditoria).
 *
 * <p>Esta classe centraliza regras de negócio e interações entre a camada de
 * controle e repositórios, garantindo a integridade dos dados através do uso de
 * transações.</p>
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDeletadoRepository usuarioDeletadoRepository;

    /**
     * Construtor com injeção dos repositórios necessários.
     *
     * @param usuarioRepository Repositório responsável pela manipulação da tabela de usuários.
     * @param usuarioDeletadoRepository Repositório da tabela de auditoria de usuários excluídos.
     */
    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioDeletadoRepository usuarioDeletadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDeletadoRepository = usuarioDeletadoRepository;
    }

    // ===================== LOGIN =====================

    /**
     * Realiza o login do usuário verificando credenciais por nome ou e-mail.
     * Aceita senhas antigas (texto puro) e as converte automaticamente para hash BCrypt.
     *
     * @param usuarioOuEmail Nome de usuário ou e-mail informado no login.
     * @param senha Senha digitada pelo usuário.
     * @return {@code true} caso as credenciais sejam válidas, caso contrário {@code false}.
     */
    @Transactional
    public boolean login(String usuarioOuEmail, String senha) {
        Optional<Usuario> usuarioOpt =
                usuarioRepository.findByEmailIgnoreCase(usuarioOuEmail)
                        .or(() -> usuarioRepository.findByNomeIgnoreCase(usuarioOuEmail));

        if (usuarioOpt.isEmpty()) return false;

        Usuario usuario = usuarioOpt.get();
        String senhaBanco = usuario.getSenha();

        // Senha já hashada (BCrypt)
        if (senhaBanco != null && senhaBanco.startsWith("$2")) {
            return PasswordUtil.verifyPassword(senha, senhaBanco);
        }

        // Senha antiga em texto puro → converter para BCrypt
        if (senhaBanco != null && senhaBanco.equals(senha)) {
            String novoHash = PasswordUtil.hashPassword(senha);
            usuario.setSenha(novoHash);
            usuarioRepository.save(usuario);
            return true;
        }

        return false;
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Optional contendo o usuário, caso exista.
     */
    public Optional<Usuario> bucarPorID(Long id) {
        return usuarioRepository.findById(id);
    }

    // ===================== BUSCAS =====================

    /**
     * Busca um usuário pelo nome, ignorando caixa alta/baixa.
     *
     * @param nome Nome do usuário.
     * @return Optional contendo o usuário, caso encontrado.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome);
    }

    /**
     * Busca um usuário pelo e-mail, ignorando caixa alta/baixa.
     *
     * @param email E-mail do usuário.
     * @return Optional contendo o usuário, caso encontrado.
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email);
    }

    /**
     * Busca um usuário por e-mail ou nome, retornando null caso nenhum seja encontrado.
     *
     * @param usuarioOuEmail Nome ou e-mail.
     * @return Usuário encontrado ou {@code null}.
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorNomeOuEmail(String usuarioOuEmail) {
        return usuarioRepository.findByEmailIgnoreCase(usuarioOuEmail)
                .or(() -> usuarioRepository.findByNomeIgnoreCase(usuarioOuEmail))
                .orElse(null);
    }

    // ===================== MÉTODOS BOOLEAN =====================

    /**
     * Verifica se existe um usuário com o e-mail informado.
     *
     * @param email E-mail procurado.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    @Transactional(readOnly = true)
    public boolean buscarUsuario(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email).isPresent();
    }

    /**
     * Verifica se existe um usuário com o nome informado.
     *
     * @param nome Nome do usuário.
     * @return {@code true} se existir.
     */
    @Transactional(readOnly = true)
    public boolean buscarPorNomeBoolean(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome).isPresent();
    }

    /**
     * Verifica se existe um usuário com o nome informado.
     *
     * @param nome Nome do usuário.
     * @return {@code true} se existir.
     */
    @Transactional(readOnly = true)
    public boolean existsByNome(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome).isPresent();
    }

    /**
     * Verifica se existe um usuário com o e-mail informado.
     *
     * @param email E-mail do usuário.
     * @return {@code true} se existir.
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email).isPresent();
    }

    // ===================== SENHA =====================

    /**
     * Retorna a senha hashada armazenada para o e-mail informado.
     *
     * @param email E-mail do usuário.
     * @return Senha hashada ou {@code null} caso o usuário não exista.
     */
    @Transactional(readOnly = true)
    public String getSenhaPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .map(Usuario::getSenha)
                .orElse(null);
    }

    /**
     * Atualiza a senha de um usuário, aplicando hashing BCrypt.
     *
     * @param email E-mail do usuário que terá a senha alterada.
     * @param novaSenha Nova senha em texto puro.
     * @return {@code true} se a atualização ocorrer, {@code false} caso o usuário não exista.
     */
    @Transactional
    public boolean atualizarSenha(String email, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailIgnoreCase(email);
        if (usuarioOpt.isEmpty()) return false;

        Usuario usuario = usuarioOpt.get();
        String hash = PasswordUtil.hashPassword(novaSenha);
        usuario.setSenha(hash);
        usuarioRepository.save(usuario);
        return true;
    }

    // ===================== CRIAR USUÁRIO =====================

    /**
     * Cria um novo usuário, caso nome e e-mail ainda não existam no sistema.
     *
     * @param nome Nome do usuário.
     * @param email E-mail do usuário.
     * @param senha Senha em texto puro.
     * @return {@code true} se o usuário for criado com sucesso; {@code false} se já existir.
     */
    @Transactional
    public boolean criarUsuario(String nome, String email, String senha) {
        if (existsByEmail(email) || existsByNome(nome)) {
            return false;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(PasswordUtil.hashPassword(senha));
        usuario.setIsAdmin(false);

        usuarioRepository.save(usuario);
        return true;
    }

    // ===================== PERFIL =====================

    /**
     * Atualiza nome e e-mail de um usuário, garantindo que não exista outro usuário
     * com os mesmos dados.
     *
     * @param idUsuario ID do usuário a ser atualizado.
     * @param novoNome Novo nome.
     * @param novoEmail Novo e-mail.
     * @return {@code true} se o perfil for atualizado; {@code false} se houver conflito.
     */
    @Transactional
    public boolean atualizarPerfil(Long idUsuario, String novoNome, String novoEmail) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) return false;

        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(novoEmail, idUsuario) ||
                usuarioRepository.existsByNomeIgnoreCaseAndIdNot(novoNome, idUsuario)) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setNome(novoNome);
        usuario.setEmail(novoEmail);
        usuario.setUpdated_at(java.time.LocalDateTime.now());
        usuarioRepository.save(usuario);
        return true;
    }

    // ===================== DELETAR USUÁRIO =====================

    /**
     * Remove um usuário do banco e salva seus dados em uma tabela de auditoria
     * (<code>UsuarioDeletado</code>), preservando informações relevantes.
     *
     * @param idUsuario ID do usuário a ser deletado.
     * @return {@code true} se excluído com sucesso, {@code false} caso o usuário não exista
     *         ou ocorra algum erro no processo.
     */
    @Transactional
    public boolean deletarUsuario(Long idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isEmpty()) return false;

        Usuario usuario = usuarioOpt.get();

        try {
            UsuarioDeletado deletado = new UsuarioDeletado();
            deletado.setIdOriginal(usuario.getId());
            deletado.setNome(usuario.getNome());
            deletado.setEmail(usuario.getEmail());
            deletado.setIsAdmin(usuario.getIsAdmin());
            deletado.setMotivoExclusao("Usuário optou por deletar a conta.");
            deletado.setDataExclusao(java.time.LocalDateTime.now());

            usuarioDeletadoRepository.save(deletado);
            usuarioRepository.delete(usuario);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
