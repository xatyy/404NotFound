package ro.notfound.co_gui;

import com.sun.management.OperatingSystemMXBean;
import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Label;
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


}