package dicasa.estoque.service;

import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.models.entities.UsuarioDeletado;
import dicasa.estoque.repository.UsuarioDeletadoRepository;
import dicasa.estoque.repository.UsuarioRepository;
import dicasa.estoque.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDeletadoRepository usuarioDeletadoRepository;

    // 🔹 Construtor com injeção dos dois repositórios
    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioDeletadoRepository usuarioDeletadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDeletadoRepository = usuarioDeletadoRepository;
    }

    // ===================== LOGIN =====================
    @Transactional
    public boolean login(String usuarioOuEmail, String senha) {
        Optional<Usuario> usuarioOpt =
                usuarioRepository.findByEmailIgnoreCase(usuarioOuEmail)
                        .or(() -> usuarioRepository.findByNomeIgnoreCase(usuarioOuEmail));

        if (usuarioOpt.isEmpty()) return false;

        Usuario usuario = usuarioOpt.get();
        String senhaBanco = usuario.getSenha();

        if (senhaBanco != null && senhaBanco.startsWith("$2")) {
            return PasswordUtil.verifyPassword(senha, senhaBanco);
        }

        if (senhaBanco != null && senhaBanco.equals(senha)) {
            String novoHash = PasswordUtil.hashPassword(senha);
            usuario.setSenha(novoHash);
            usuarioRepository.save(usuario);
            return true;
        }

        return false;
    }

    // ===================== BUSCAS =====================
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorNomeOuEmail(String usuarioOuEmail) {
        return usuarioRepository.findByEmailIgnoreCase(usuarioOuEmail)
                .or(() -> usuarioRepository.findByNomeIgnoreCase(usuarioOuEmail))
                .orElse(null);
    }

    // ===================== MÉTODOS BOOLEAN =====================
    @Transactional(readOnly = true)
    public boolean buscarUsuario(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean buscarPorNomeBoolean(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean existsByNome(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email).isPresent();
    }

    // ===================== SENHA =====================
    @Transactional(readOnly = true)
    public String getSenhaPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .map(Usuario::getSenha)
                .orElse(null);
    }

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
