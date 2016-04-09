package graviton.core;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {
    @FXML
    private VBox vBox;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab settingTab;
    @FXML
    private Tab statistcsTab;

    @FXML
    private TextField serverName;
    @FXML
    private TextField delay;
    @FXML
    private TextField networkAdress;
    @FXML
    private TextField networkPort;
    @FXML
    private TextField connectionCount;

    @FXML
    private Button mainButton;

    @FXML
    private Slider slider;

    @FXML
    private TableView<Flooder> tableView;
    @FXML
    private TableColumn<Flooder, String> columnName;
    @FXML
    private TableColumn columnStatus;
    @FXML
    private TableColumn columnProgress;
    @FXML
    private TableColumn columnStop;

    @FXML
    private Label firstLabel;
    @FXML
    private Label secondLabel;

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    @FXML
    private void initialize() {
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnStatus.setCellValueFactory((new PropertyValueFactory<>("title")));
        columnProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        columnProgress.setCellFactory(ProgressBarTableCell.<Flooder>forTableColumn());

        Callback<TableColumn<Flooder, String>, TableCell<Flooder, String>> cellFactory =
                new Callback<TableColumn<Flooder, String>, TableCell<Flooder, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Flooder, String> param) {
                        final TableCell<Flooder, String> cell = new TableCell<Flooder, String>() {
                            final Button button = new Button("X");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    button.setOnAction((ActionEvent event) -> {
                                        if (getTableView().getItems().get(getIndex()).isStarted())
                                            getTableView().getItems().get(getIndex()).stop();
                                        else
                                            getTableView().getItems().remove(getIndex());
                                    });
                                    setGraphic(button);
                                    setText(null);
                                }
                            }
                        };
                        cell.setAlignment(Pos.CENTER);
                        return cell;
                    }
                };
        columnStop.setCellFactory(cellFactory);

        ImageView imageView = new ImageView(new Image("file:data/button/run.png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        mainButton.setGraphic(imageView);
    }

    @FXML
    public void startBot(ActionEvent actionEvent) {
        if (serverName.getText().isEmpty() || networkAdress.getText().isEmpty() || networkPort.getText().isEmpty() || connectionCount.getText().isEmpty())
            generateAlert(Alert.AlertType.WARNING, "Please enter all informations");
        else try {
            createTask(serverName.getText(), networkAdress.getText(), Integer.parseInt(networkPort.getText()), Integer.parseInt(connectionCount.getText()), (int) slider.getValue());
        } catch (Exception e) {
            generateAlert(Alert.AlertType.WARNING, "Please enter numeric values ");
        }
    }

    @FXML
    public void changeDelayValue() {
        delay.setText(String.valueOf((int) slider.getValue()) + " ms");
    }

    private void createTask(String name, String host, int port, int connections, int waitingTime) {
        Flooder flooder = new Flooder(name, host, port, connections, waitingTime);
        tableView.getItems().add(flooder);
        firstLabel.textProperty().bind(flooder.messageProperty());
        secondLabel.textProperty().bind(flooder.messageProperty());
        executor.execute(flooder);
        tabPane.getSelectionModel().select(statistcsTab);
    }

    private void generateAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
