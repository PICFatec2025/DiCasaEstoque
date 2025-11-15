package dicasa.estoque.util;

import javafx.scene.control.*;

/**
 * Classe relacionada à formatação dos TextFields
 */
public class Constraints {
    /**
     * Com essa classe, o TextField só recebe número e apenas 1 , ou .
     * @param textField
     * @param maxValue valor máximo de caracteres permitidos
     */
    public static void textFieldRecebeApenasNumeros(
            TextField textField,
            int maxValue
    ){
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String novoTexto = change.getControlNewText();
            if (
                    novoTexto.matches("\\d*(\\.|,)?\\d*")&&
                            novoTexto.length()<=maxValue
            ) {
                return change;
            }
            return null;
        }));
    }

    /**
     * Função que limita quantos caracteres 1 TextField pode receber
     * @param textField
     * @param maxValue
     */
    public static void defineTamanhoMaximoTextField(TextField textField, int maxValue) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.length()<=maxValue) {
                return change;
            }
            return null;
        }));
    }

    /**
     * Função que verifica se o TextField está em branco
     * E também define o texto de campo obrigatório na label em caso de TextField vazia
     * @param textField
     * @param label
     * @return true se tiver vazio e false se está preenchido
     */
    public static boolean textFieldEstaEmBranco(
            TextField textField,
            Label label
    ){
        if(textField.getText().isBlank()){
            label.setText("*Campo Obrigatório");
            return true;
        }else {
            label.setText("");
            return false;
        }
    }
    public static boolean isDatePickerBlank(
            DatePicker datePicker,
            Label label
    ){
        if(datePicker.getValue()==null){
            label.setText("*Campo Obrigatório");
            return true;
        } else {
            label.setText("");
            return false;
        }
    }
    public static boolean isComboBoxBlank(
            ComboBox<?> comboBox,
            Label label
    ){
        if(comboBox.getValue()==null){
            label.setText("*Campo Obrigatório");
            return true;
        }else {
            label.setText("");
            return false;
        }
    }
}
