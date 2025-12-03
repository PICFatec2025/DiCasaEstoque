package dicasa.estoque.csv;

import dicasa.estoque.models.dto.PrevisaoCompraDTO;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Componente para exportação de dados para CSV
 *
 * CONCEITOS DE POO APLICADOS:
 * 1. RESPONSABILIDADE ÚNICA: Apenas exportação CSV
 * 2. ENCAPSULAMENTO: Método público simples, lógica interna privada
 * 3. COMPOSIÇÃO: Usa FileWriter e outros objetos Java
 * 4. TRATAMENTO DE EXCEÇÕES: Try-with-resources garante fechamento do arquivo
 */
@Component
public class CSVPrevisaoComprasExporter {

    /**
     * Exporta a lista de previsões de compra para um arquivo CSV
     *
     * @param previsoes Lista de DTOs com dados das previsões
     * @return Mensagem de sucesso ou erro
     */
    public String exportarPrevisaoComprasCSV(List<PrevisaoCompraDTO> previsoes) {
        // Formata data/hora para nome do arquivo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String caminhoArquivo = "previsao_compras_" + timestamp + ".csv";

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            // Cabeçalho do CSV - ALTERADO: removido fornecedores
            writer.append("Produto,Categoria,Estoque Atual,Estoque Minimo,Quantidade Comprar,Urgencia\n");

            // Dados dos produtos - ALTERADO: removida coluna de fornecedores
            for (PrevisaoCompraDTO previsao : previsoes) {
                writer.append(escape(previsao.getNomeProduto())).append(",")
                        .append(escape(previsao.getCategoria())).append(",")
                        .append(String.valueOf(previsao.getEstoqueAtual())).append(",")
                        .append(String.valueOf(previsao.getEstoqueMinimo())).append(",")
                        .append(String.valueOf(previsao.getQuantidadeComprar())).append(",")
                        .append(escape(previsao.getNivelUrgencia())).append("\n");
            }

            return "✅ Arquivo CSV gerado com sucesso!\nLocal: " + caminhoArquivo +
                    "\nProdutos exportados: " + previsoes.size();

        } catch (IOException e) {
            return "❌ Erro ao gerar o arquivo CSV: " + e.getMessage();
        }
    }

    /**
     * Método privado para escape de strings no CSV
     * Necessário para strings que contêm vírgulas, aspas ou quebras de linha
     *
     * @param s String a ser escapada
     * @return String escapada para formato CSV
     */
    private String escape(String s) {
        if (s == null || s.isEmpty()) {
            return "\"\"";
        }

        // Se contém vírgula, aspas ou quebra de linha, envolve em aspas
        if (s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }

        return s;
    }
}