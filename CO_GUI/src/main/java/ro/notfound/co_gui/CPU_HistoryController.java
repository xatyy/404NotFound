package ro.notfound.co_gui;

import com.mongodb.client.*;
import javafx.application.Platform;
import javafx.beans.Observable;
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
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;

public class CPU_HistoryController {

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
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
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
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchtoCPU(ActionEvent TO_CPU) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CPU_Scene.fxml"));
        stage = (Stage) ((Node)TO_CPU.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();


    }
    @FXML
    protected void PC_Specs(ActionEvent TO_Specs) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Specs_CPU.fxml"));
        stage = (Stage) ((Node) TO_Specs.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    protected void About_CPU(ActionEvent TO_CPU) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("About_CPU.fxml"));
        stage = (Stage) ((Node) TO_CPU.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void go_Back(ActionEvent go_back) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        stage = (Stage) ((Node)go_back.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
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
                        MongoCollection<Document> collection = database.getCollection("userScores");
                        FindIterable<Document> iterable = collection.find();
                        data = FXCollections.observableArrayList();
                       for (Document document : iterable) {
                            String userName = document.getString("userName");
                            String cpuModel = document.getString("cpuModel");
                            Double scoreAES = document.getDouble("scoreAES");
                            Double scoreRSA = document.getDouble("scoreRSA");
                            Double scoreMatrixSingle = document.getDouble("scoreMatrixSingle");
                            Double scoreMatrixMulti = document.getDouble("scoreMatrixMulti");
                           data.add(new Leaderboard(userName, cpuModel, scoreAES, scoreRSA, scoreMatrixSingle, scoreMatrixMulti));
                       }

                        TableColumn<Leaderboard, String> userColumn = new TableColumn<>("Username");
                        userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

                        TableColumn<Leaderboard, String> cpuColumn = new TableColumn<>("CPU Model");
                        cpuColumn.setCellValueFactory(new PropertyValueFactory<>("cpuModel"));

                        TableColumn<Leaderboard, String> aesColumn = new TableColumn<>("Score AES");
                        aesColumn.setCellValueFactory(new PropertyValueFactory<>("scoreAES"));

                        TableColumn<Leaderboard, String> rsaColumn = new TableColumn<>("Score RSA");
                        rsaColumn.setCellValueFactory(new PropertyValueFactory<>("scoreRSA"));

                        TableColumn<Leaderboard, String> matrixColumnSingle = new TableColumn<>("Score Matrix Single");
                        matrixColumnSingle.setCellValueFactory(new PropertyValueFactory<>("scoreMatrixSingle"));

                        TableColumn<Leaderboard, String> matrixColumnMulti = new TableColumn<>("Score Matrix Multi");
                        matrixColumnMulti.setCellValueFactory(new PropertyValueFactory<>("scoreMatrixMulti"));

                        leaderboard.getColumns().add(userColumn);
                        leaderboard.getColumns().add(cpuColumn);
                        leaderboard.getColumns().add(aesColumn);
                        leaderboard.getColumns().add(rsaColumn);
                        leaderboard.getColumns().add(matrixColumnSingle);
                        leaderboard.getColumns().add(matrixColumnMulti);

                        leaderboard.setItems(data);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });




    }

    public static class Leaderboard {
        private String userName;

        private String cpuModel;
        private Double scoreAES;
        private Double scoreRSA;
        private Double scoreMatrixSingle;
        private Double scoreMatrixMulti;

        public Leaderboard(String userName, String cpuModel, Double scoreAES, Double scoreRSA, Double scoreMatrixSingle, Double scoreMatrixMulti) {
            this.userName = userName;
            this.cpuModel = cpuModel;
            this.scoreAES = scoreAES;
            this.scoreRSA = scoreRSA;
            this.scoreMatrixSingle = scoreMatrixSingle;
            this.scoreMatrixMulti = scoreMatrixMulti;
        }

        public String getUserName() {
            return userName;
        }

        public String getCpuModel() {
            return cpuModel;
        }

        public double getScoreAES() {
            return scoreAES;
        }

        public double getScoreRSA() {
            return scoreRSA;
        }


        public double getScoreMatrixSingle() {
            return scoreMatrixSingle;
        }
        public double getScoreMatrixMulti() {
            return scoreMatrixMulti;
        }

    }
}


