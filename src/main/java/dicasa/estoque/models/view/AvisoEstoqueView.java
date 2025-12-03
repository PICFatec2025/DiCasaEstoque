package dicasa.estoque.models.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa um aviso exibido na tela inicial com informações sobre o estoque mínimo dos produtos.
 */
public class AvisoEstoqueView {

    private final StringProperty data;
    private final StringProperty hora;
    private final StringProperty aviso;
    private final StringProperty quantidadeAteMinimo;
    private final StringProperty prioridade;

    public AvisoEstoqueView(String data, String hora, String aviso, String quantidadeAteMinimo, String prioridade) {
        this.data = new SimpleStringProperty(data);
        this.hora = new SimpleStringProperty(hora);
        this.aviso = new SimpleStringProperty(aviso);
        this.quantidadeAteMinimo = new SimpleStringProperty(quantidadeAteMinimo);
        this.prioridade = new SimpleStringProperty(prioridade);
    }

    public String getData() {
        return data.get();
    }

    public StringProperty dataProperty() {
        return data;
    }

    public String getHora() {
        return hora.get();
    }

    public StringProperty horaProperty() {
        return hora;
    }

    public String getAviso() {
        return aviso.get();
    }

    public StringProperty avisoProperty() {
        return aviso;
    }

    public String getQuantidadeAteMinimo() {
        return quantidadeAteMinimo.get();
    }

    public StringProperty quantidadeAteMinimoProperty() {
        return quantidadeAteMinimo;
    }

    public String getPrioridade() {
        return prioridade.get();
    }

    public StringProperty prioridadeProperty() {
        return prioridade;
    }
}