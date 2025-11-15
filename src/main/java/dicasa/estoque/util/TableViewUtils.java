package dicasa.estoque.util;

import dicasa.estoque.navigation.ScreenNavigator;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.function.Function;

public class TableViewUtils {
    public static <T> void setupColumnString(TableColumn<T, String> column, Function<T, String> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleStringProperty(extractor.apply(cellData.getValue())));
    }

    public static <T> void setupColumnLong(TableColumn<T, Long> column, Function<T, Long> extractor) {
        column.setCellValueFactory(cellData ->
                new SimpleLongProperty(extractor.apply(cellData.getValue())).asObject());
    }
    public static <T> void tableViewFillHeight(TableView<T> tableView){
        Stage stage = (Stage) ScreenNavigator.getScene().getWindow();
        tableView.prefHeightProperty().bind(stage.heightProperty());
    }
}
