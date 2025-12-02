package dicasa.estoque.controller.estoque;

import dicasa.estoque.controller.DataFormController;
import dicasa.estoque.models.dto.EstoqueProdutoCompletoResponseDTO;
import dicasa.estoque.models.dto.EstoqueProdutoRequestDTO;
import dicasa.estoque.models.dto.EstoqueProdutoResponseDTO;
import dicasa.estoque.models.entities.EstoqueProduto;
import dicasa.estoque.navigation.ScreenNavigator;
import dicasa.estoque.service.EstoqueService;
import dicasa.estoque.util.EstoqueProdutoSavedEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static dicasa.estoque.util.Alerts.messageError;
import static dicasa.estoque.util.Constraints.textFieldEstaEmBranco;
import static dicasa.estoque.util.Constraints.textFieldRecebeApenasNumerosInteiros;
import static dicasa.estoque.util.NumberValidatorUtil.ehUmNumeroInteiroPositivo;

/**
 * Controller que gerencia a janela de Edição de Estoque
 * Podemos alterar a quantidade do estoque atual, além da quantidade mínima e a quantidade emergencial
 */
@Component
public class EditarEstoqueController implements Initializable, DataFormController {

    EstoqueProdutoResponseDTO estoqueEditado;
    @FXML
    private TextField textQuantidadeForm;
    @FXML
    private TextField textMinimoForm;
    @FXML
    private TextField textEmergencialForm;
    @FXML
    private Label errorNameLabelQuantidade;
    @FXML
    private Label errorNameLabelMinimo;
    @FXML
    private Label errorNameLabelEmergencial;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonSave;

    private final ApplicationContext applicationContext;

    private final EstoqueService estoqueService;

    public EditarEstoqueController(
            ApplicationContext applicationContext,
            EstoqueService estoqueService) {
        this.applicationContext = applicationContext;
        this.estoqueService = estoqueService;
    }

    /**
     * Função que roda ao inicializar a tela
     * Ele prepara o formulário e carrega os itens do banco de dados
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
        configuraTeclaEnter();
    }

    /**
     * Função que configura os formulários para receber os dados do banco de dados
     */
    public void initializeNodes() {
        textFieldRecebeApenasNumerosInteiros(textQuantidadeForm, 9);
        textFieldRecebeApenasNumerosInteiros(textMinimoForm, 9);
        textFieldRecebeApenasNumerosInteiros(textEmergencialForm, 9);
    }

    /**
     * Função que gera a ação ao clicar no Botão de Salvar
     * A ação é delegada para a função editarEstoque
     *
     * @param event
     */
    @FXML
    public void onClickButtonSave(ActionEvent event) {
        editarEstoque(event);
    }

    /**
     * Função que gera a ação ao clicar no Botão de Cancelar
     * Ele volta para a tela anterior, de lista de estoque
     *
     * @param event
     */
    public void onClickButtonCancel(ActionEvent event) {
        resetForm();
        ScreenNavigator.closeWindow(event);
    }

    /**
     * Função que recebe o objeto da tela anterior e joga na tela atual
     *
     * @param data Objeto do tipo EstoqueProdutoCompletoResponseDTO que vai ser recebido da tela anterior
     */
    @Override
    public void setFormData(Object data) {
        if (data instanceof EstoqueProdutoCompletoResponseDTO) {
            estoqueEditado = estoqueService.acharEstoquePorId(
                    ((EstoqueProdutoCompletoResponseDTO) data).id_estoque_produto()
            );
            preencherFormulario();
        }
    }

    /**
     * Função que é chamado ao inicializar a janela
     * Onde será feito o preenchimento dos dados do formulário
     * com o estoque que vai ser editado
     */

    private void preencherFormulario() {
        if (estoqueEditado != null) {
            textQuantidadeForm.setText(estoqueEditado.quantidade());
            textMinimoForm.setText(estoqueEditado.quantidadeMinima());
            textEmergencialForm.setText(estoqueEditado.estoqueEmergencial());
        }
    }

    /**
     * Essa função é chamada ao sair dessa janela
     * Ela limpa os dados do Estoque e do formulário para não haver dados ao voltar nessa janela
     */
    @Override
    public void resetForm() {
        this.estoqueEditado = null;
        textQuantidadeForm.clear();
        textMinimoForm.clear();
        textEmergencialForm.clear();
    }

    /**
     * Função para ver se o formulário não tem nenhum dado em Branco
     * Se tiver, aparecerá uma label indicando mensagem de erro
     *
     * @return Se o formulário está preenchido
     */
    private boolean validateForm() {
        boolean isValid = true;
        if (textFieldEstaEmBranco(textQuantidadeForm, errorNameLabelQuantidade)) isValid = false;
        if (textFieldEstaEmBranco(textMinimoForm, errorNameLabelMinimo)) isValid = false;
        if (textFieldEstaEmBranco(textEmergencialForm, errorNameLabelEmergencial)) isValid = false;
        return isValid;
    }

    /**
     * Função que vai fazer uma série de validações nos dados do formulário
     * Se tiverem válidas, ele salva as alterações na tabela
     * Dando certo, ele fecha a janela
     * Dando errado em alguma etapa, aparecerá um alert com mensagem de erro
     *
     * @param event
     */
    private void editarEstoque(ActionEvent event) {
        if (!validateForm()) {
            messageError(
                    "Erro ao cadastrar",
                    "Preencha todos os campos!"
            );
            return;
        }
        int quantidade = 0, minimo = 0, emergencial = 0;
        try {
            quantidade = ehUmNumeroInteiroPositivo(textQuantidadeForm.getText());
            minimo = ehUmNumeroInteiroPositivo(textMinimoForm.getText());
            emergencial = ehUmNumeroInteiroPositivo(textEmergencialForm.getText());
        } catch (NumberFormatException e) {
            messageError(
                    "Erro ao cadastrar",
                    "Formato de número inválido"
            );
            return;
        }
        if(minimo<emergencial){
            messageError("Erro ao cadastrar","Estoque mínimo não pode ser menor que o emergencial");
            errorNameLabelMinimo.setText("Mínimo tem que ser maior que o emergencial");
            return;
        }

        try {
            EstoqueProdutoRequestDTO estoqueAlterado = new EstoqueProdutoRequestDTO(
                    estoqueEditado.id_estoque_produto(),
                    quantidade,
                    minimo,
                    emergencial,
                    LocalDateTime.now()
            );
            EstoqueProduto estoqueSalvo = estoqueService.editarEstoque(estoqueAlterado);
            publicaEvento(estoqueSalvo);
            resetForm();
            ScreenNavigator.closeWindow(event);
        } catch (Exception e) {
            messageError(
                    "Erro ao cadastrar",
                    "Não foi possível alterar estoque"
            );
            return;
        }
    }

    /**
     * Função que avisa ao sistema que o estoque foi alterado
     * Assim a tela de relatório de estoque se atualiza com essa nova alteração
     *
     * @param estoqueProduto estoque que vai ser alterado
     */
    public void publicaEvento(EstoqueProduto estoqueProduto) {
        applicationContext.publishEvent(new EstoqueProdutoSavedEvent(this, estoqueProduto));
    }

    private void configuraTeclaEnter() {
        // Configuração para o campo de Quantidade Atual
        textQuantidadeForm.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                textMinimoForm.requestFocus(); // Move o foco para o próximo campo
            }
        });

        // Configuração para o campo de Quantidade Mínima
        textMinimoForm.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Quando pressionar ENTER no campo Mínimo, move para Emergencial
                textEmergencialForm.requestFocus(); // Move o foco para o próximo campo
            }
        });

        // Configuração para o campo de Quantidade Emergencial
        textEmergencialForm.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Quando pressionar ENTER no campo Emergencial, simula clique no botão Salvar
                buttonSave.fire();
            }
        });

        // Configuração adicional: pressionar ESC no botão Cancelar
        buttonCancel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
                buttonCancel.fire();
            }
        });

        // Configuração adicional: pressionar ENTER no botão Salvar
        buttonSave.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buttonSave.fire();
            }
        });

    }
}
