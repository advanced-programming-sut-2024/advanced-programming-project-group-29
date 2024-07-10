package Client.View;

import Client.Client;
import Client.Enum.Menu;
import Client.Regex.RankingMenuRegex;
import Client.Model.ApplicationRunningTimeData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RankingMenu extends Application {

    public Label nUsers;
    public TableView tableView;
    private Client client;
    private Timeline timeline;

    public RankingMenu() {
        super();
        client = Client.getClient();
        client.sendCommand("menu enter " + Menu.RANKING_MENU.toString());
    }


    private ObservableList<ArrayList<String>> getData() {
        ArrayList<String> ranks = (ArrayList<String>) client.sendCommand(RankingMenuRegex.GET_RANKING.getRegex());
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (String line : ranks) {
            ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split("\t")));
            result.add(splitLine);
        }
        for (ArrayList<String> a : result) {
            int n = result.indexOf(a);
            if (n >= 0 && n <= 9) a.add("See Last Game");
        }
        ArrayList<String> names = new ArrayList<>() {{
            add("Status");
            add("Rank");
            add("Username");
            add("Count of Wins");
            add("Last Game");
        }};
        tableView.getColumns().clear();
        for (int i = 0; i < 5; i++) {
            final int index = i;
            TableColumn<ArrayList<String>, String> column = new TableColumn<>(names.get(i));
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(index)));
            column.setStyle("-fx-alignment: CENTER;");
            if (i == 4) {
                column.setCellFactory(c -> new TableCell<ArrayList<String>, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            setOnMouseClicked(event -> {
                                if (item.equals("See Last Game") && event.getClickCount() == 2) {
                                    Platform.runLater(() -> {
                                        int row = getTableRow().getIndex();
                                        String name = ((ArrayList<String>) tableView.getItems().get(row)).get(2);
                                        Client.setSeeThisUserLastGame(name);
                                        try {
                                            timeline.stop();
                                            (new BroadCastMenu()).start(ApplicationRunningTimeData.getStage());
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
            tableView.getColumns().add(column);
        }
        ObservableList<ArrayList<String>> data = FXCollections.observableArrayList(result);
        tableView.setRowFactory(new Callback<TableView<ArrayList<String>>, TableRow<ArrayList<String>>>() {
            @Override
            public TableRow<ArrayList<String>> call(TableView<ArrayList<String>> arrayListTableView) {
                return new TableRow<ArrayList<String>>() {
                    protected void updateItem(ArrayList<String> score, boolean empty) {
                        super.updateItem(score, empty);
                        if (getIndex() == 0 && !empty) {
                            setStyle("-fx-background-color: gold;");
                        } else if (getIndex() == 1 && !empty) {
                            setStyle("-fx-background-color: silver;");
                        } else if (getIndex() == 2 && !empty) {
                            setStyle("-fx-background-color: rgb(205, 127, 50);");
                        } else {
                            setStyle("-fx-background-color: gray;");
                        }
                    }
                };
            }
        });
        int numColumns = tableView.getColumns().size();
        tableView.getColumns().forEach(column ->
                ((TableColumn) column).prefWidthProperty().bind(
                        tableView.widthProperty().divide(numColumns)
                )
        );
        return data;
    }

    private void refreshPain(ObservableList<ArrayList<String>> data, int n) {
        tableView.setItems(data);
        nUsers.setText("Ranking between " + n + " users");
    }


    @FXML
    public void initialize() {
        final ObservableList<ArrayList<String>>[] data = new ObservableList[]{getData()};
        refreshPain(data[0], data[0].size());
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<ArrayList<String>> newData = getData();
                if (!data[0].equals(newData)) {
                    data[0] = newData;
                    refreshPain(data[0], data[0].size());
                }
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        stage.centerOnScreen();
        Pane pane = FXMLLoader.load(Objects.requireNonNull(RegisterMenu.class.getResource("/FXML/RankingMenu.fxml")));
        try {
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
            ApplicationRunningTimeData.setPane(pane);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        timeline.stop();
        new MainMenu().start(ApplicationRunningTimeData.getStage());
    }

    public void buttonEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = ApplicationRunningTimeData.getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("e47429"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("e47429"));
        }
    }

    public void buttonExited(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Rectangle) {
            Pane pane = ApplicationRunningTimeData.getPane();
            int n = pane.getChildren().indexOf((Rectangle) mouseEvent.getSource()) + 1;
            ((Label) pane.getChildren().get(n)).setTextFill(Paint.valueOf("black"));
        } else {
            ((Label) mouseEvent.getSource()).setTextFill(Paint.valueOf("black"));
        }
    }
}
