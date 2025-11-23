package dicasa.estoque.util;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

    /**
     * Função que carrega uma coluna que vai receber um Long
     * @param column
     * @param extractor
     * @param <T>
     */
    public static <T> void setupColumnLong(TableColumn<T, Long> column, Function<T, Long> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleLongProperty(extractor.apply(cellData.getValue())).asObject());
    }
    /**
     * Função que carrega uma coluna que vai receber uma Int
     * @param column
     * @param extractor
     * @param <T>
     */
    public static <T> void setupColumnInteger(TableColumn<T, Integer> column, Function<T, Integer> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(extractor.apply(cellData.getValue())).asObject());
    }
    /**
     * Função que carrega uma coluna que vai receber um Double
     * @param column
     * @param extractor
     * @param <T>
     */
    public static <T> void setupColumnDouble(TableColumn<T, Double> column, Function<T, Double> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(extractor.apply(cellData.getValue())).asObject());
    }

    /**
     * Função que faz a tabela ocupar a tela inteira em sua altura
     * @param tableView tabela a ser expandida
     * @param <T> tipo que a tabela recebe
     */
    public static <T> void tableViewFillHeight(TableView<T> tableView){
        Stage stage = (Stage) ScreenNavigator.getScene().getWindow();
        tableView.prefHeightProperty().bind(stage.heightProperty());
    }
    /**
     * Função que faz a tabela ocupar a tela inteira em sua largura
     * @param tableView tabela a ser expandida
     * @param <T> tipo que a tabela recebe
     */
    public static <T> void tableViewFillWidth(TableView<T> tableView){
        Stage stage = (Stage) ScreenNavigator.getScene().getWindow();
        tableView.prefWidthProperty().bind(stage.heightProperty());
    }
}
