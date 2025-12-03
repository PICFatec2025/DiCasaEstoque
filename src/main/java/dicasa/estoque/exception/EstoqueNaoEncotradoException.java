package dicasa.estoque.exception;

/**
 * Exception que é usada quando o estoque não for encontrado
 */
public class EstoqueNaoEncotradoException extends RuntimeException {
    /**
     * Constructor com mensagem padrão
     */
    public EstoqueNaoEncotradoException() {
        super("Estoque não encontrado: ");
    }

    /**
     * Constructor com mensagem personalizada
     * @param message texto que vai aparecer na mensagem
     */
    public EstoqueNaoEncotradoException(String message) {
        super(message);
    }

}
