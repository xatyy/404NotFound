package ro.notfound.co_gui;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;


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
    public void initialize(){

        final RotateTransition rt = new RotateTransition(Duration.millis(15000), sattelite);
        rt.setByAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(Timeline.INDEFINITE);
        rt.play();
    }
}