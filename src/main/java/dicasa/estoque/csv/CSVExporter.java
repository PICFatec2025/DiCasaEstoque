package dicasa.estoque.csv;

import dicasa.estoque.models.dto.EstoqueProdutoCompletoResponseDTO;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe que pega os dados de uma tabela e gera um relatório em formato de CSV
 */
@Component
public class CSVExporter {
    /**
     * pega a tabela de produtos e exporta em CSV
     * @param produtos tabela de produtos com estoque que vai ser exportado
     * @return mensagem que vai aparecer na tela após completar a ação
     */
    public String exportarEstoqueEmCSV(List<EstoqueProdutoCompletoResponseDTO> produtos){
        String caminhoArquivo = "estoque"+ LocalDateTime.now()+".csv";
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            // Cabeçalho
            writer.append("ID Produto,Nome,Marca,Tipo,Data Criacao, Quantidade,Minima,Emergencial,Status Texto\n");

            // Linhas
            for (EstoqueProdutoCompletoResponseDTO produto : produtos) {
                writer.append(String.valueOf(produto.idProduto())).append(",")
                        .append(escape(produto.nome())).append(",")
                        .append(escape(produto.marca())).append(",")
                        .append(escape(produto.tipo())).append(",")
                        .append(escape(produto.dataCriacao())).append(",")
                        .append(String.valueOf(produto.quantidade())).append(",")
                        .append(String.valueOf(produto.quantidadeMinima())).append(",")
                        .append(String.valueOf(produto.estoqueEmergencial())).append(",")
                        .append(escape(produto.statusTexto())).append("\n");
            }

            return "✅ CSV gerado em: " + caminhoArquivo;
        } catch (IOException e) {
            return "Erro ao gerar CSV";
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
