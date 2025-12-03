package dicasa.estoque.service;

import dicasa.estoque.models.dto.FornecedorRequestDTO;
import dicasa.estoque.models.dto.FornecedorResponseDTO;
import dicasa.estoque.models.entities.EnderecoFornecedor;
import dicasa.estoque.models.entities.Fornecedor;
import dicasa.estoque.models.entities.TelefoneFornecedor;
import dicasa.estoque.models.entities.Usuario;
import dicasa.estoque.models.mapper.FornecedorMapper;
import dicasa.estoque.repository.EnderecoFornecedorRepository;
import dicasa.estoque.repository.FornecedorRepository;
import dicasa.estoque.repository.TelefoneFornecedorRepository;
import dicasa.estoque.util.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;
    private final EnderecoFornecedorRepository enderecoFornecedorRepository;
    private final TelefoneFornecedorRepository telefoneFornecedorRepository;
    private final FornecedorMapper fornecedorMapper;

    public FornecedorService(
            FornecedorRepository fornecedorRepository,
            EnderecoFornecedorRepository enderecoFornecedorRepository,
            TelefoneFornecedorRepository telefoneFornecedorRepository,
            FornecedorMapper fornecedorMapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.enderecoFornecedorRepository = enderecoFornecedorRepository;
        this.telefoneFornecedorRepository = telefoneFornecedorRepository;
        this.fornecedorMapper = fornecedorMapper;
    }

    public List<Fornecedor> listarFornecedores() {
        try {
            return fornecedorRepository.findAll();
        } catch (Exception e) {
            log.error("Erro ao listar fornecedores: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> listarFornecedoresCompleto() {
        try {
            List<Fornecedor> fornecedores = listarFornecedores();
            return fornecedorMapper.toDtoList(fornecedores);
        } catch (Exception e) {
            log.error("Erro ao listar fornecedores completos: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public FornecedorResponseDTO salvarFornecedor(FornecedorRequestDTO dto) {
        try {
            Usuario usuario = SessionManager.getUsuarioLogado();
            if (usuario == null || usuario.getId() == null) {
                throw new IllegalStateException("Usuário não encontrado na sessão. Faça login novamente.");
            }

            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setCnpj(dto.cnpj());
            fornecedor.setNomeFantasia(dto.nomeFantasia());
            fornecedor.setRazaoSocial(dto.razaoSocial());
            fornecedor.setDataCriacao(LocalDateTime.now());
            fornecedor.setUsuario(usuario);

            Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);

            if (dto.endereco() != null) {
                EnderecoFornecedor endereco = new EnderecoFornecedor();
                endereco.setLogradouro(dto.endereco().logradouro());
                endereco.setComplemento(dto.endereco().complemento());
                endereco.setBairro(dto.endereco().bairro());
                endereco.setCidade(dto.endereco().cidade());
                endereco.setUf(dto.endereco().uf());
                endereco.setCep(dto.endereco().cep());
                endereco.setFornecedor(fornecedorSalvo);
                enderecoFornecedorRepository.save(endereco);
                fornecedorSalvo.setEnderecoFornecedor(endereco);
            }

            if (dto.telefones() != null && !dto.telefones().isEmpty()) {
                for (String telefone : dto.telefones()) {
                    TelefoneFornecedor telefoneFornecedor = new TelefoneFornecedor();
                    telefoneFornecedor.setTelefone(telefone);
                    telefoneFornecedor.setFornecedor(fornecedorSalvo);
                    telefoneFornecedorRepository.save(telefoneFornecedor);
                }
            }

            return fornecedorMapper.toDto(fornecedorSalvo);

        } catch (DataIntegrityViolationException e) {
            String mensagem = obterMensagemCurta(e);
            log.warn("Falha de integridade ao salvar fornecedor: {}", mensagem);
            throw new IllegalArgumentException("Dados inválidos ao salvar fornecedor: " + mensagem, e);
        } catch (Exception e) {
            String mensagem = obterMensagemCurta(e);
            log.error("Erro ao salvar fornecedor: {}", mensagem, e);
            throw new IllegalStateException("Erro ao salvar fornecedor: " + mensagem, e);
        }
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> buscarFornecedores(String filtro) {
        try {
            List<Fornecedor> fornecedores;

            if (filtro == null || filtro.trim().isEmpty()) {
                fornecedores = fornecedorRepository.findAll();
            } else {
                fornecedores = fornecedorRepository.findByFiltro(filtro);
            }

            return fornecedorMapper.toDtoList(fornecedores);

        } catch (Exception e) {
            log.error("Erro ao buscar fornecedores: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> buscarFornecedoresPorTermo(String tipo, String termo) {
        try {
            List<Fornecedor> fornecedores;

            if (termo == null || termo.trim().isEmpty()) {
                fornecedores = fornecedorRepository.findAll();
            } else {
                String termoLike = "%" + termo.toLowerCase() + "%";

                switch (tipo) {
                    case "Razão Social":
                        fornecedores = fornecedorRepository.findByRazaoSocialContainingIgnoreCase(termo);
                        break;
                    case "Nome Fantasia":
                        fornecedores = fornecedorRepository.findByNomeFantasiaContainingIgnoreCase(termo);
                        break;
                    case "CNPJ":
                        fornecedores = fornecedorRepository.findByCnpjContaining(termo);
                        break;
                    case "Cidade":
                        fornecedores = fornecedorRepository.findByEnderecoFornecedorCidadeContainingIgnoreCase(termo);
                        break;
                    case "Estado (UF)":
                        fornecedores = fornecedorRepository.findByEnderecoFornecedorUfContainingIgnoreCase(termo);
                        break;
                    case "Telefone":
                        fornecedores = fornecedorRepository.findByTelefonesTelefoneContaining(termo);
                        break;
                    default: // Qualquer outro (incluindo "Todos os campos" se você adicionar)
                        fornecedores = fornecedorRepository.findByFiltroGeral(termoLike);
                }
            }

            return fornecedorMapper.toDtoList(fornecedores);

        } catch (Exception e) {
            log.error("Erro ao buscar fornecedores por termo: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public FornecedorResponseDTO atualizarFornecedor(Long id, FornecedorRequestDTO dto) {
        try {
            Fornecedor fornecedor = fornecedorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

            fornecedor.setCnpj(dto.cnpj());
            fornecedor.setNomeFantasia(dto.nomeFantasia());
            fornecedor.setRazaoSocial(dto.razaoSocial());
            fornecedor.setDataAtualizacao(LocalDateTime.now());

            if (dto.endereco() != null) {
                EnderecoFornecedor endereco = fornecedor.getEnderecoFornecedor();
                if (endereco == null) {
                    endereco = new EnderecoFornecedor();
                    endereco.setFornecedor(fornecedor);
                }
                endereco.setLogradouro(dto.endereco().logradouro());
                endereco.setComplemento(dto.endereco().complemento());
                endereco.setBairro(dto.endereco().bairro());
                endereco.setCidade(dto.endereco().cidade());
                endereco.setUf(dto.endereco().uf());
                endereco.setCep(dto.endereco().cep());
                enderecoFornecedorRepository.save(endereco);
            }

            List<TelefoneFornecedor> telefonesExistentes = telefoneFornecedorRepository.findByFornecedorId(id);
            telefoneFornecedorRepository.deleteAll(telefonesExistentes);

            if (dto.telefones() != null && !dto.telefones().isEmpty()) {
                for (String telefone : dto.telefones()) {
                    TelefoneFornecedor telefoneFornecedor = new TelefoneFornecedor();
                    telefoneFornecedor.setTelefone(telefone);
                    telefoneFornecedor.setFornecedor(fornecedor);
                    telefoneFornecedorRepository.save(telefoneFornecedor);
                }
            }

            Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedor);
            return fornecedorMapper.toDto(fornecedorAtualizado);

        } catch (DataIntegrityViolationException e) {
            String mensagem = obterMensagemCurta(e);
            log.warn("Falha de integridade ao atualizar fornecedor: {}", mensagem);
            throw new IllegalArgumentException("Dados inválidos ao atualizar fornecedor: " + mensagem, e);
        } catch (Exception e) {
            String mensagem = obterMensagemCurta(e);
            log.error("Erro ao atualizar fornecedor: {}", mensagem, e);
            throw new IllegalStateException("Erro ao atualizar fornecedor: " + mensagem, e);
        }
    }

    @Transactional
    public void excluirFornecedor(Long id) {
        try {
            Fornecedor fornecedor = fornecedorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

            telefoneFornecedorRepository.deleteByFornecedorId(id);

            if (fornecedor.getEnderecoFornecedor() != null) {
                enderecoFornecedorRepository.delete(fornecedor.getEnderecoFornecedor());
            }

            fornecedorRepository.delete(fornecedor);

        } catch (Exception e) {
            log.error("Erro ao excluir fornecedor: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao excluir fornecedor", e);
        }
    }

    // Método adicionado para debug
    @Transactional(readOnly = true)
    public void debugFornecedores() {
        try {
            List<Fornecedor> fornecedores = fornecedorRepository.findAll();
            log.info("Total de fornecedores no banco: {}", fornecedores.size());

            for (Fornecedor fornecedor : fornecedores) {
                log.info("Fornecedor ID: {}, Razão Social: {}, Nome Fantasia: {}, CNPJ: {}",
                        fornecedor.getIdFornecedor(),
                        fornecedor.getRazaoSocial(),
                        fornecedor.getNomeFantasia(),
                        fornecedor.getCnpj());

                if (fornecedor.getEnderecoFornecedor() != null) {
                    log.info("  Endereço: {}, {}, {}/{}",
                            fornecedor.getEnderecoFornecedor().getLogradouro(),
                            fornecedor.getEnderecoFornecedor().getBairro(),
                            fornecedor.getEnderecoFornecedor().getCidade(),
                            fornecedor.getEnderecoFornecedor().getUf());
                }

                if (fornecedor.getTelefones() != null && !fornecedor.getTelefones().isEmpty()) {
                    log.info("  Telefones: {}",
                            fornecedor.getTelefones().stream()
                                    .map(TelefoneFornecedor::getTelefone)
                                    .toList());
                }
            }
        } catch (Exception e) {
            log.error("Erro no debug: {}", e.getMessage(), e);
        }
    }

    private String obterMensagemCurta(Exception e) {
        Throwable causa = e;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }

        String mensagem = causa.getMessage();
        if (mensagem == null || mensagem.isBlank()) {
            return e.getClass().getSimpleName();
        }

        return mensagem.split("\n")[0].trim();
    }
}