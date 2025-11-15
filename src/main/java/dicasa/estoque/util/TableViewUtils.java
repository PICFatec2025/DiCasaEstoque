package dicasa.estoque.util;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.function.Function;

/**
 * Classe quegera um factory de uma campo de tabela nova
 */
public class TableViewUtils {
    /**
     * Função que carrega uma coluna que vai receber uma String
     * @param column
     * @param extractor
     * @param <T>
     */
    public static <T> void setupColumnString(TableColumn<T, String> column, Function<T, String> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleStringProperty(extractor.apply(cellData.getValue())));
    }

    public static <T> void setupColumnLong(TableColumn<T, Long> column, Function<T, Long> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleLongProperty(extractor.apply(cellData.getValue())).asObject());
    }

    /**
     * Função que faz a tabela ocupar a tela inteira
     * @param tableView
     * @param <T>
     */
    public static <T> void tableViewFillHeight(TableView<T> tableView){
        Stage stage = (Stage) ScreenNavigator.getScene().getWindow();
        tableView.prefHeightProperty().bind(stage.heightProperty());
    }
}
