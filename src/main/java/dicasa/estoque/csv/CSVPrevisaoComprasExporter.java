package dicasa.estoque.csv;

import dicasa.estoque.models.dto.PrevisaoCompraDTO;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CSVPrevisaoComprasExporter {

    public String exportarPrevisaoComprasCSV(List<PrevisaoCompraDTO> previsoes) {
        Path diretorioRelatorios = Paths.get("relatorios", "previsao_compras");
        Path caminhoArquivo = diretorioRelatorios.resolve(
                "previsao_compras_" + LocalDateTime.now().toString().replaceAll("[:.]", "-") + ".csv"
        );

        try {
            Files.createDirectories(diretorioRelatorios);
        } catch (IOException e) {
            return "❌ Erro ao criar diretório de relatórios";
        }

        try (FileWriter writer = new FileWriter(caminhoArquivo.toFile())) {
            // Cabeçalho
            writer.append("Produto,Tipo,Estoque Atual,Estoque Minimo,Quantidade Comprar,Urgencia,Fornecedores\n");

            // Linhas
            for (PrevisaoCompraDTO previsao : previsoes) {
                writer.append(escape(previsao.getNomeProduto())).append(",")
                        .append(escape(previsao.getTipo())).append(",")
                        .append(String.valueOf(previsao.getEstoqueAtual())).append(",")
                        .append(String.valueOf(previsao.getEstoqueMinimo())).append(",")
                        .append(String.valueOf(previsao.getQuantidadeComprar())).append(",")
                        .append(escape(previsao.getNivelUrgencia())).append(",")
                        .append(escape(previsao.getFornecedoresDisponiveis())).append("\n");
            }

            return "✅ CSV gerado em: " + caminhoArquivo.toAbsolutePath();
        } catch (IOException e) {
            return "❌ Erro ao gerar o CSV: " + e.getMessage();
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
