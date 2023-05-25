package ro.notfound.co_gui;

import com.sun.management.OperatingSystemMXBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Scanner;

public class RAM_SceneController {
    private Stage stage;
    private Scene scene;
    @FXML
    private Label myLabelProcessor ;
    @FXML
    private Label myLabelOS ;
    @FXML
    private Label myLabelMemory ;
    @FXML
    private Label myLabelUsername;

    @FXML
    private Slider ram_slider;

    @FXML
    private Text txtOut;

    @FXML
    private Text errorString;

    private int [] options = new int[4];

    @FXML
    protected void History(ActionEvent history) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("History_RAM.fxml"));
        stage = (Stage) ((Node) history.getSource()).getScene().getWindow();
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

    @FXML
    protected void switchtoRAM(ActionEvent TO_RAM) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RAM_Scene.fxml"));
        stage = (Stage) ((Node)TO_RAM.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    protected void PC_Specs(ActionEvent TO_Specs) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Specs_RAM.fxml"));
        stage = (Stage) ((Node) TO_Specs.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected synchronized void swtichToScore(ActionEvent score_view) throws IOException, InterruptedException {
            if(ram_slider.getValue() != 0) {
                errorString.setText("");
                options[3] = 1;
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Score_View.fxml"));
                Parent pane = (Parent) fxmlLoader.load();
                stage = (Stage) ((Node) score_view.getSource()).getScene().getWindow();
                stage.setScene(new Scene(pane));
                stage.show();
                Score_SceneController scoreController = fxmlLoader.getController();
                int size = (int) ram_slider.getValue();
                scoreController.runBenchmark(options, " ", size);
            } else {
                errorString.setText("Size cannot be 0 !");
            }
    }

    public void initialize() {
        ram_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                txtOut.setText(String.valueOf(newValue.intValue()));
            }
        });
    }


}