package dicasa.estoque.controller;

/**
 * Inteface que vai servir para quando for alterarmos/excuirmos/criarmos uma entidade no Banco de dados,
 * o Java FX vai ser avisado e assim recarregaremos as listas
 */
public interface DataFormController {
    void setFormData(Object data);
    void resetForm();
}
