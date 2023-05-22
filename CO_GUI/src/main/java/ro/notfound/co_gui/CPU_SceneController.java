package ro.notfound.co_gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class CPU_SceneController {
    private Stage stage;
    private Scene scene;

    private int [] options = new int[4];

    private boolean disabled = true;

    @FXML
    protected void go_Back(ActionEvent go_back) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        stage = (Stage) ((Node)go_back.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
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
    protected void PC_Specs(ActionEvent TO_CPU) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Specs_CPU.fxml"));
        stage = (Stage) ((Node) TO_CPU.getSource()).getScene().getWindow();
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
    TextField aes_input;
    @FXML
    TextField rsa_input;

    @FXML
    Button run;

    @FXML
    private Slider matrix_slider;

    @FXML
    private Text txtOut;

    @FXML
    TextArea inputString;
    @FXML
    protected void setTests(ActionEvent setTestsEvent) {
        final Node source = (Node) setTestsEvent.getSource();
        String id = source.getId();
        switch (id) {
            case "aes":
                if(options[0] == 0){
                    source.getStyleClass().remove("unchecked");
                    source.getStyleClass().add("checked");
                    options[0] = 1;
                }else{
                    source.getStyleClass().remove("checked");
                    source.getStyleClass().add("unchecked");
                    options[0] = 0;
                }
                break;
            case "rsa":
                if(options[1] == 0){
                    source.getStyleClass().remove("unchecked");
                    source.getStyleClass().add("checked");
                    options[1] = 1;
                }else{
                    source.getStyleClass().remove("checked");
                    source.getStyleClass().add("unchecked");
                    options[1] = 0;
                }
                break;
            case "matrix":
                if(options[2] == 0){
                    source.getStyleClass().remove("unchecked");
                    source.getStyleClass().add("checked");
                    options[2] = 1;
                }else{
                    source.getStyleClass().remove("checked");
                    source.getStyleClass().add("unchecked");
                    options[2] = 0;
                }
                break;
            default:
                ;
        }
        int check = 0;
        for (int i = 0; i < 4; i++){
            if(options[i] == 1){
                check++;
            }
        }

        System.out.println(check);
        if(check == 0){
            run.getStyleClass().remove("buttons");
            run.getStyleClass().add("button_disabled");
            disabled = true;
        } else {
            run.getStyleClass().remove("button_disabled");
            run.getStyleClass().add("buttons");
            disabled = false;
        }
    }



    @FXML
    protected synchronized void swtichToScore(ActionEvent score_view) throws IOException, InterruptedException {
        if(!disabled) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Score_View.fxml"));
            Parent pane = (Parent) fxmlLoader.load();
            stage = (Stage) ((Node) score_view.getSource()).getScene().getWindow();
            stage.setScene(new Scene(pane));
            stage.show();
            Score_SceneController scoreController = fxmlLoader.getController();
            int matrixSize = (int) matrix_slider.getValue();
            String encryptionString = inputString.getText();
            scoreController.runBenchmark(options, encryptionString, matrixSize);
        }
    }



    public void initialize() {
        matrix_slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                txtOut.setText(String.valueOf(newValue.intValue()));
            }
        });
    }
}
