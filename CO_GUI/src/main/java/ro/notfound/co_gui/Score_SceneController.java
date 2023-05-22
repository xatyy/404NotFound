package ro.notfound.co_gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ro.notfound.co_gui.bench.IBenchmark;
import ro.notfound.co_gui.bench.cpu.CPUAES;
import ro.notfound.co_gui.bench.cpu.CPUMatrixMultiplication;
import ro.notfound.co_gui.bench.cpu.CPURSA;
import ro.notfound.co_gui.logging.ConsoleLogger;
import ro.notfound.co_gui.logging.ILog;
import ro.notfound.co_gui.logging.TimeUnit;
import ro.notfound.co_gui.timing.ITimer;
import ro.notfound.co_gui.timing.Timer;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;
import static ro.notfound.co_gui.bench.cpu.CPUAES.generateKey;
import static ro.notfound.co_gui.logging.TimeUnit.toTimeUnit;

public class Score_SceneController {
    ExecutorService threadPool = Executors.newWorkStealingPool();

    private Stage stage;
    private Scene scene;

    private int option;
    private String arg;

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
        int singleThread = 0;
        int multiThread = 0;

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
            //showCount.setText(String.valueOf(score));
            if(i == 1) {
                singleThread = (int) score;
            }
                multiThread = (int) score;
        }
        log.writeTime("Matrix multiplication took", totalTime, TimeUnit.Sec );

        showScore.setText("Singlethread: " + singleThread + " Multithread: " + multiThread);

        bench.getResult();
        log.close();
    }

    @FXML
    public void cpuAES(String message) throws NoSuchAlgorithmException {
        Timer time =new Timer();
        IBenchmark aesBenchmark = new CPUAES();
        TimeUnit  timeUnit=TimeUnit.Sec;
        // Initialize the benchmark
        int keySize=256;
        SecretKey secretKey = generateKey(keySize);

        aesBenchmark.initialize(message, secretKey,keySize);

        // Warm up the benchmark
        aesBenchmark.warmUp();

        // Run the benchmark
        time.start();
        aesBenchmark.run(message);
        double timer=toTimeUnit(time.stop(),timeUnit);

        System.out.println(((CPUAES)aesBenchmark).score(timer,keySize));

        int score = (int) ((CPUAES)aesBenchmark).score(timer,keySize);

        // Print the benchmark result
        System.out.println(aesBenchmark.getResult());
        showScore.setText("Score: " + score);

    }

    @FXML
    public void cpuRSA(String message){
        // Create a new benchmark instance
        IBenchmark rsaBenchmark = new CPURSA();
        ITimer time = new Timer();
        TimeUnit timeUnit=TimeUnit.Sec;
        int keySize=1024;
        // Initialize the benchmark
        rsaBenchmark.initialize(keySize);

        // Warm up the benchmark
        rsaBenchmark.warmUp();

        // Run the benchmark
        // rsaBenchmark.run("hello this is diana's text to encrypt");

        time.start();
        rsaBenchmark.run(message);
        double timer=toTimeUnit(time.stop(),timeUnit);
        int nrKeys=2;
        System.out.println(((CPURSA)rsaBenchmark).score(timer,nrKeys));

        int score = (int) ((CPURSA)rsaBenchmark).score(timer,nrKeys);
        // Print the benchmark result

        showScore.setText("Score: " + score);

    }

    @FXML
    protected void runBenchmark(int [] options, String inputString, int matrixSize) {
        AtomicInteger testLenght = new AtomicInteger();
        AtomicInteger testDone = new AtomicInteger(0);

        for (int i = 0; i < 3; i++) {
            if( options[i] == 1){
                testLenght.getAndIncrement();
            }
        }

        Runnable benchTask = () -> threadPool.execute(() -> {
                if (options[0] == 1) {
                    try {
                        testDone.getAndIncrement();
                        showCount.setText("Running test " + testDone + "/" + testLenght + "...");
                        Thread.sleep(2500);
                        cpuAES(inputString);
                        Thread.sleep(2500);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } if (options[1] == 1) {
                try {
                    testDone.getAndIncrement();
                    showCount.setText("Running test " + testDone + "/" + testLenght + "...");
                    Thread.sleep(2500);
                    cpuRSA(inputString);
                    Thread.sleep(2500);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                } if (options[2] == 1) {
                try {
                    testDone.getAndIncrement();
                    showCount.setText("Running test " + testDone + "/" + testLenght + "...");
                    Thread.sleep(2500);
                    matrixMultiplication(matrixSize);
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                } if (options[3] == 1) {
                    // To implement
                }

            System.out.println(testLenght);
            System.out.println(testDone);


            if(testLenght == testDone){
                showCount.setText("Benchmark completed!");
                dino_gif.setImage(new Image(getClass().getResource("/img/dino_happy.png").toString(), true));
            }


        });



        Thread thread = new Thread(benchTask);
        thread.setDaemon(true);
        thread.start();




    }

    @FXML
    protected void setOption(int n, String args){
        option = n;
        arg = args;
    }

    @FXML
    private Text showCount;

    @FXML
    private Text showScore;

    @FXML
    private ImageView dino_gif;

    @FXML
    protected void go_Back(ActionEvent go_back) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        stage = (Stage) ((Node)go_back.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /*
    public synchronized void initialize() throws InterruptedException {

        Runnable task = () -> {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Emulate a long task
            // Use Platform.runLater()

            System.out.println(option);

            Platform.runLater(() -> {
                if(option == 0) {

                    threadPool.execute(() -> {
                        try {
                            cpuAES(arg);
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else if(option == 1){
                    threadPool.execute(() -> cpuRSA(arg));
                } else if(option == 2) {

                    String [] integer = arg.split("\\.");
                    System.out.println(integer[0]);

                    threadPool.execute(() -> matrixMultiplication(Integer.parseInt(integer[0])));
                }
            });





            // Stop count down and remove the GIF
            Platform.runLater(() -> {


                //showCount.setText("Done");
            });
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }
    */
}
