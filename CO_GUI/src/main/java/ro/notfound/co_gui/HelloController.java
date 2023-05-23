package ro.notfound.co_gui;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bson.Document;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.mongodb.client.model.Filters.eq;

public class HelloController {
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;
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
    protected void switchtoCPU(ActionEvent TO_CPU) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CPU_Scene.fxml"));
        stage = (Stage) ((Node)TO_CPU.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();


    }
    @FXML
    protected void switchtoRAM(ActionEvent TO_RAM) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RAM_Scene.fxml"));
        stage = (Stage) ((Node)TO_RAM.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private ImageView sattelite;

    @FXML
    private ImageView no_wifi;

    @FXML
    public void initialize(){

        final RotateTransition rt = new RotateTransition(Duration.millis(15000), sattelite);
        rt.setByAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(Timeline.INDEFINITE);
        rt.play();


        Runnable db_connection = () -> {
            Platform.runLater(() -> {
                String connectionString = "mongodb+srv://xaty:KtnZPZybZtMfSn8t@404database.coe1uer.mongodb.net/?retryWrites=true&w=majority";

                threadPool.execute( () -> {
                    try {
                        System.out.println("Trying db connection----------");
                        MongoClient mongoClient = MongoClients.create(connectionString);
                        MongoDatabase database = mongoClient.getDatabase("404Database");
                        MongoCollection<Document> collection = database.getCollection("userScores");
                        Document doc = collection.find(eq("userName", "PC-1")).first();
                    } catch (Exception e) {
                        System.out.println(e);
                        no_wifi.setOpacity(1);
                    }
                });
            });
        };

        Thread thread = new Thread(db_connection);
        thread.setDaemon(true);
        thread.start();

    }
}