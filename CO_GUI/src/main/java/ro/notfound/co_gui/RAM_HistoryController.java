package ro.notfound.co_gui;

import com.mongodb.client.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RAM_HistoryController {

    private Stage stage;
    private Scene scene;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button btnMin;
    @FXML
    private Button btnClose;
    @FXML
    private Pane topPane;

    @FXML
    protected void handleCloseAction(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleMinifyAction(){
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void handleClickAction(MouseEvent event) {
        xOffset = event.getX();
        yOffset = event.getY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    ExecutorService threadPool = Executors.newWorkStealingPool();

    @FXML
    private TableView<Leaderboard> leaderboard;
    private ObservableList <Leaderboard> data;

    private List<Document> results = new ArrayList<>();
    @FXML
    protected void History(ActionEvent history) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("History_CPU.fxml"));

        stage = (Stage) ((Node) history.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchtoRAM(ActionEvent TO_RAM) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RAM_Scene.fxml"));
        stage = (Stage) ((Node)TO_RAM.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void PC_Specs(ActionEvent TO_Specs) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Specs_RAM.fxml"));
        stage = (Stage) ((Node) TO_Specs.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    protected void go_Back(ActionEvent go_back) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));

        stage = (Stage) ((Node)go_back.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {

        Platform.runLater(() -> {
            String connectionString = "mongodb+srv://xaty:KtnZPZybZtMfSn8t@404database.coe1uer.mongodb.net/?retryWrites=true&w=majority";


            try {
                System.out.println("Trying db connection----------");
                MongoClient mongoClient = MongoClients.create(connectionString);
                MongoDatabase database = mongoClient.getDatabase("404Database");
                MongoCollection<Document> collection = database.getCollection("RAMScores");
                FindIterable<Document> iterable = collection.find();
                data = FXCollections.observableArrayList();
                for (Document document : iterable) {
                    String userName = document.getString("userName");
                    Long ramGB = document.getLong("ramGB");
                    Double scoreRAM = document.getDouble("scoreRAM");
                    data.add(new Leaderboard(userName, ramGB, scoreRAM));
                }

                TableColumn<Leaderboard, String> userColumn = new TableColumn<>("Username");
                userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

                TableColumn<Leaderboard, String> ramColumn = new TableColumn<>("RAM (GB)");
                ramColumn.setCellValueFactory(new PropertyValueFactory<>("ramGB"));


                TableColumn<Leaderboard, String> scoreColumn = new TableColumn<>("Score RAM");
                scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoreRAM"));

                leaderboard.getColumns().add(userColumn);
                leaderboard.getColumns().add(ramColumn);
                leaderboard.getColumns().add(scoreColumn);

                leaderboard.setItems(data);

            } catch (Exception e) {
                System.out.println(e);
            }
        });




    }

    public static class Leaderboard {
        private String userName;

        private Long ramGB;
        private Double scoreRAM;

        public Leaderboard(String userName, Long ramGB, Double scoreRAM) {
            this.userName = userName;
            this.ramGB = ramGB;
            this.scoreRAM = scoreRAM;
        }

        public String getUserName() {
            return userName;
        }

        public Long getRamGB() {
            return ramGB;
        }

        public Double getScoreRAM() {
            return scoreRAM;
        }
    }
}


