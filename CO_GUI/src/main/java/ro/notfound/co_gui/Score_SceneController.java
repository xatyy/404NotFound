package ro.notfound.co_gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ro.notfound.co_gui.bench.IBenchmark;
import ro.notfound.co_gui.bench.cpu.CPUMatrixMultiplication;
import ro.notfound.co_gui.logging.ConsoleLogger;
import ro.notfound.co_gui.logging.ILog;
import ro.notfound.co_gui.logging.TimeUnit;
import ro.notfound.co_gui.timing.ITimer;
import ro.notfound.co_gui.timing.Timer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Score_SceneController {
    ExecutorService threadPool = Executors.newWorkStealingPool();

    private Stage stage;
    private Scene scene;

    private static int option;

    @FXML
    public void matrixMultiplication(int matrixSize){
         // initialize with matrix size
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
            showCount.setText(String.valueOf(score));
        }
        log.writeTime("Matrix multiplication took", totalTime, TimeUnit.Sec );

        bench.getResult();
        log.close();
    }

    @FXML
    protected void setOption(int n){
        option = n;
        System.out.println(option);
    }

    @FXML
    private Text showCount;

    @FXML
    protected void go_Back(ActionEvent go_back) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        stage = (Stage) ((Node)go_back.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public synchronized void initialize() throws InterruptedException {

        /*Platform.runLater(() -> {
            if(option == 1){
                matrixMultiplication(1500);
            }
        });*/

        Runnable task = () -> {

            // Emulate a long task
            // Use Platform.runLater()
                threadPool.execute(() -> matrixMultiplication(1500));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            // Stop count down and remove the GIF
            Platform.runLater(() -> {
                //showCount.setText("Done");
            });
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }
}
