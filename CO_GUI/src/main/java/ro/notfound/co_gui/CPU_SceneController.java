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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class CPU_SceneController {
    private Stage stage;
    private Scene scene;

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
    private Slider matrix_slider;

    @FXML
    private Text txtOut;



    @FXML
    protected synchronized void swtichToScore(ActionEvent score_view) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Score_View.fxml"));
        Parent pane = (Parent) fxmlLoader.load();
        stage = (Stage) ((Node) score_view.getSource()).getScene().getWindow();
        stage.setScene(new Scene(pane));
        stage.show();
        Score_SceneController scoreController = fxmlLoader.getController();
        final Node source = (Node) score_view.getSource();
        String id = source.getId();
        String input;
        switch (id){
            case "aes":
                System.out.println("User selected aes");
                input = aes_input.getText();
                if(input.isEmpty())
                    input = "404NotFound";
                scoreController.setOption(0, input);
                break;
            case "rsa":
                System.out.println("User selected rsa");
                input = rsa_input.getText();
                if(input.isEmpty())
                    input = "404NotFound";
                scoreController.setOption(1, input);
                break;
            case "matrix":
                System.out.println("User selected matrix");
                scoreController.setOption(2, String.valueOf(matrix_slider.getValue()));
                break;
            default:
                System.out.println("User selected all");

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
