package dicasa.estoque.models.dto;

import javafx.beans.property.*;

/**
 * Data Transfer Object (DTO) para Previsão de Compras
 *
 *
 * 1. ENCAPSULAMENTO: Todos os atributos são privados e acessados via getters
 * 2. CONSTRUTOR: Inicializa todas as propriedades obrigatórias
 * 3. PROPERTIES DO JAVAFX: Usa Property para binding bidirecional com a interface
 * 4. MÉTODOS ESTÁTICOS: Contém lógica de negócio reutilizável
 * 5. SOBRESCRITA: Implementa toString() para depuração
 * 6. JAVA BEAN PATTERN: Segue convenções de getters
 */
public class PrevisaoCompraDTO {
    private final LongProperty idProduto;
    private final StringProperty nomeProduto;
    private final StringProperty tipo;
    private final IntegerProperty estoqueAtual;
    private final IntegerProperty estoqueMinimo;
    private final IntegerProperty quantidadeComprar;
    private final StringProperty nivelUrgencia;

    public PrevisaoCompraDTO(Long idProduto, String nomeProduto, String tipo,
                             Integer estoqueAtual, Integer estoqueMinimo,
                             Integer quantidadeComprar, String nivelUrgencia) {
        this.idProduto = new SimpleLongProperty(idProduto);
        this.nomeProduto = new SimpleStringProperty(nomeProduto);
        this.tipo = new SimpleStringProperty(tipo);
        this.estoqueAtual = new SimpleIntegerProperty(estoqueAtual);
        this.estoqueMinimo = new SimpleIntegerProperty(estoqueMinimo);
        this.quantidadeComprar = new SimpleIntegerProperty(quantidadeComprar);
        this.nivelUrgencia = new SimpleStringProperty(nivelUrgencia);
    }

    // ========== GETTERS (JavaBean Pattern) ==========
    public Long getIdProduto() { return idProduto.get(); }
    public String getNomeProduto() { return nomeProduto.get(); }
    public String getTipo() { return tipo.get(); }
    public Integer getEstoqueAtual() { return estoqueAtual.get(); }
    public Integer getEstoqueMinimo() { return estoqueMinimo.get(); }
    public Integer getQuantidadeComprar() { return quantidadeComprar.get(); }
    public String getNivelUrgencia() { return nivelUrgencia.get(); }

    // ========== PROPERTY METHODS (JavaFX Binding) ==========
    public LongProperty idProdutoProperty() { return idProduto; }
    public StringProperty nomeProdutoProperty() { return nomeProduto; }
    public StringProperty tipoProperty() { return tipo; }
    public IntegerProperty estoqueAtualProperty() { return estoqueAtual; }
    public IntegerProperty estoqueMinimoProperty() { return estoqueMinimo; }
    public IntegerProperty quantidadeComprarProperty() { return quantidadeComprar; }
    public StringProperty nivelUrgenciaProperty() { return nivelUrgencia; }

    // ========== MÉTODOS DE NEGÓCIO ESTÁTICOS ==========

    /**
     * Calcula a quantidade a comprar baseado no estoque atual e mínimo
     */
    public static Integer calcularQuantidadeComprar(Integer estoqueAtual, Integer estoqueMinimo) {
        if (estoqueAtual == null || estoqueMinimo == null || estoqueMinimo <= 0) {
            return 0;
        }
        return Math.max(0, estoqueMinimo - estoqueAtual);
    }

    /**
     * Determina o nível de urgência da compra
     */
    public static String determinarUrgencia(Integer estoqueAtual, Integer estoqueMinimo) {
        if (estoqueAtual == null || estoqueMinimo == null || estoqueMinimo <= 0) {
            return "INDEFINIDO";
        }

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

    // ========== SOBRESCRITA DE toString() ==========
    @Override
    public String toString() {
        return String.format("%s: %d unidades (%s)",
                getNomeProduto(), getQuantidadeComprar(), getNivelUrgencia());
    }
}