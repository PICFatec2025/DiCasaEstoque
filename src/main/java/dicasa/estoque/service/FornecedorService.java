package dicasa.estoque.service;

import dicasa.estoque.models.dto.FornecedorResponseDTO;
import dicasa.estoque.models.entities.Fornecedor;
import dicasa.estoque.models.mapper.FornecedorMapper;
import dicasa.estoque.repository.EnderecoFornecedorRepository;
import dicasa.estoque.repository.FornecedorRepository;
import dicasa.estoque.repository.TelefoneFornecedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Classe de service para Fornecedor
 * Conecta os Repositories relacionados a Fornecedor com os controllers de telas de fornecedores
 */
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

    /**
     * Função que mostra a lista de Funcionários
     * @return Lista de Funcionários
     */
    public List<Fornecedor> listarFornecedores() {
        try {
            return fornecedorRepository.findAll();

        } catch (Exception e) {
            log.error("Erro ao listar fornecedores: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Função que busca a lista de Funcionários, e junta com os dados de endereço e de Telefone
     * @return Lista de Funcionários com dados completos
     */
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


}
