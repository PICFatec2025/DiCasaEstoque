package dicasa.estoque.service;

import dicasa.estoque.repository.*;
import org.springframework.stereotype.Service;

/**
 * Classe de service para Pedido
 * Conecta os Repositories relacionados a Pedido com os controllers de telas de pedidos
 */

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PedidoProdutoRepository pedidoProdutoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoDetalhadoRepository pedidoDetalhadoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            PedidoProdutoRepository pedidoProdutoRepository,
            FornecedorRepository fornecedorRepository,
            ProdutoRepository produtoRepository,
            PedidoDetalhadoRepository pedidoDetalhadoRepository)
    {
        this.pedidoRepository = pedidoRepository;
        this.pedidoProdutoRepository = pedidoProdutoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoDetalhadoRepository = pedidoDetalhadoRepository;
    }
}
