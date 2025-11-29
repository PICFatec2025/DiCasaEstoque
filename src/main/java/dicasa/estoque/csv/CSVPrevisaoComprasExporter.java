package dicasa.estoque.csv;

import dicasa.estoque.models.dto.PrevisaoCompraDTO;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CSVPrevisaoComprasExporter {

    public String exportarPrevisaoComprasCSV(List<PrevisaoCompraDTO> previsoes) {
        String caminhoArquivo = "previsao_compras_" + LocalDateTime.now().toString().replaceAll("[:.]", "-") + ".csv";

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            // Cabeçalho
            writer.append("Produto,Categoria,Estoque Atual,Estoque Minimo,Quantidade Comprar,Urgencia,Fornecedores\n");

            // Linhas
            for (PrevisaoCompraDTO previsao : previsoes) {
                writer.append(escape(previsao.getNomeProduto())).append(",")
                        .append(escape(previsao.getCategoria())).append(",")
                        .append(String.valueOf(previsao.getEstoqueAtual())).append(",")
                        .append(String.valueOf(previsao.getEstoqueMinimo())).append(",")
                        .append(String.valueOf(previsao.getQuantidadeComprar())).append(",")
                        .append(escape(previsao.getNivelUrgencia())).append(",")
                        .append(escape(previsao.getFornecedoresDisponiveis())).append("\n");
            }

            return "✅ CSV gerado em: " + caminhoArquivo;
        } catch (IOException e) {
            return "❌ Erro ao gerar o CSV: " + e.getMessage();
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
