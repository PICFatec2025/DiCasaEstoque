package dicasa.estoque.models.dto;

import javafx.beans.property.*;

public class PrevisaoCompraDTO {
    private final LongProperty idProduto;
    private final StringProperty nomeProduto;
    private final StringProperty tipo;
    private final IntegerProperty estoqueAtual;
    private final IntegerProperty estoqueMinimo;
    private final IntegerProperty quantidadeComprar;
    private final StringProperty nivelUrgencia;
    private final StringProperty fornecedoresDisponiveis;

    public PrevisaoCompraDTO(Long idProduto, String nomeProduto, String tipo,
                             Integer estoqueAtual, Integer estoqueMinimo,
                             Integer quantidadeComprar, String nivelUrgencia,
                             String fornecedoresDisponiveis) {
        this.idProduto = new SimpleLongProperty(idProduto);
        this.nomeProduto = new SimpleStringProperty(nomeProduto);
        this.tipo = new SimpleStringProperty(tipo);
        this.estoqueAtual = new SimpleIntegerProperty(estoqueAtual);
        this.estoqueMinimo = new SimpleIntegerProperty(estoqueMinimo);
        this.quantidadeComprar = new SimpleIntegerProperty(quantidadeComprar);
        this.nivelUrgencia = new SimpleStringProperty(nivelUrgencia);
        this.fornecedoresDisponiveis = new SimpleStringProperty(fornecedoresDisponiveis);
    }

    // Getters no padrão JavaBean (com "get")
    public String getNomeProduto() { return nomeProduto.get(); }
    public String getTipo() { return tipo.get(); }
    public Integer getEstoqueAtual() { return estoqueAtual.get(); }
    public Integer getEstoqueMinimo() { return estoqueMinimo.get(); }
    public Integer getQuantidadeComprar() { return quantidadeComprar.get(); }
    public String getNivelUrgencia() { return nivelUrgencia.get(); }
    public String getFornecedoresDisponiveis() { return fornecedoresDisponiveis.get(); }
    public Long getIdProduto() { return idProduto.get(); }

    // Property methods para o PropertyValueFactory
    public StringProperty nomeProdutoProperty() { return nomeProduto; }
    public StringProperty tipoProperty() { return tipo; }
    public IntegerProperty estoqueAtualProperty() { return estoqueAtual; }
    public IntegerProperty estoqueMinimoProperty() { return estoqueMinimo; }
    public IntegerProperty quantidadeComprarProperty() { return quantidadeComprar; }
    public StringProperty nivelUrgenciaProperty() { return nivelUrgencia; }

    // Mantenha os métodos estáticos
    public static Integer calcularQuantidadeComprar(Integer estoqueAtual, Integer estoqueMinimo) {
        if (estoqueAtual >= estoqueMinimo) {
            return 0;
        }
        return estoqueMinimo - estoqueAtual;
    }

    public static String determinarUrgencia(Integer estoqueAtual, Integer estoqueMinimo) {
        if (estoqueAtual == 0) {
            return "CRÍTICO";
        } else if (estoqueAtual < estoqueMinimo * 0.3) {
            return "ALTO";
        } else if (estoqueAtual < estoqueMinimo * 0.7) {
            return "MÉDIO";
        } else {
            return "BAIXO";
        }
    }
}