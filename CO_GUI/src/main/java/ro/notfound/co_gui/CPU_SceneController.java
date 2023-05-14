package ro.notfound.co_gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ro.notfound.co_gui.bench.IBenchmark;
import ro.notfound.co_gui.bench.cpu.CPUMatrixMultiplication;
import ro.notfound.co_gui.logging.ConsoleLogger;
import ro.notfound.co_gui.logging.ILog;
import ro.notfound.co_gui.logging.TimeUnit;
import ro.notfound.co_gui.timing.ITimer;
import ro.notfound.co_gui.timing.Timer;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

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
    protected void CPU_Matrix(ActionEvent go_score) throws IOException {
        int matrixSize = 100; // initialize with matrix size
        ITimer timer = new Timer();
        ILog log = new ConsoleLogger();
        IBenchmark bench = new CPUMatrixMultiplication();
        int numThreads = Runtime.getRuntime().availableProcessors(); // get number of available threads
        System.out.println("threads" + numThreads);
        bench.initialize(matrixSize);
        bench.warmUp();

        long totalTime = 0;
        for(int i = 1; i <= numThreads; i *= 2){
            timer.start();
            bench.run(i);
            long time = timer.stop();
            log.writeTime("[t="+i+"] finished in", time, TimeUnit.Sec);
            totalTime += time;

            double score = (double) matrixSize/( Math.log(time) * 10E-2* i);
            log.write("Score: " + score);

            System.out.println();
        }
        log.writeTime("Matrix multiplication took", totalTime, TimeUnit.Sec );
        log.close();

        Alert a = new Alert(AlertType.NONE);

        long finalTotalTime = totalTime;
        EventHandler<ActionEvent> event = e -> {
            a.setAlertType(AlertType.CONFIRMATION);
            a.setTitle("CPU MatrixMultiplication");
            a.setContentText(String.valueOf(finalTotalTime));
            a.show();
        };

    }

}
