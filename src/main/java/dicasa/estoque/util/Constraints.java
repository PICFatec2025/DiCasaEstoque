package dicasa.estoque.util;

import javafx.scene.control.*;

public class Constraints {
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
    public static void defineTamanhoMaximoTextField(TextField textField, int maxValue) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String novoTexto = change.getControlNewText();
            if (novoTexto.length()<=maxValue) {
                return change;
            }
            return null;
        }));
    }
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
