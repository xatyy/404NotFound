package ro.notfound.co_gui;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bson.Document;
import org.bson.types.ObjectId;
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
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Integer.parseInt;
import static ro.notfound.co_gui.bench.cpu.CPUAES.generateKey;
import static ro.notfound.co_gui.logging.TimeUnit.toTimeUnit;

public class Score_SceneController {
    ExecutorService threadPool = Executors.newWorkStealingPool();

    private Stage stage;
    private Scene scene;

    @FXML
    private AnchorPane pane;

    private int option;
    private String arg;

    @FXML
    public double matrixMultiplication(int matrixSize, AtomicInteger testNo){
        ITimer timer = new Timer();
        ILog log = new ConsoleLogger();
        IBenchmark bench = new CPUMatrixMultiplication();
        int numThreads = Runtime.getRuntime().availableProcessors();
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

        double test = testNo.doubleValue();

        score1.setY(((test - 1) * 30));

        score1.setText("Matrix Score: Singlethread: " + singleThread + " Multithread: " + multiThread);

        bench.getResult();
        log.close();

        return multiThread;
    }

    @FXML
    public double cpuAES(String message, AtomicInteger testNo) throws NoSuchAlgorithmException {
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

        double test = testNo.doubleValue();

        score2.setY(((test - 1) * 30));

        score2.setText("AES Encryption Score: " + score);

        return score;

    }

    @FXML
    public double cpuRSA(String message, AtomicInteger testNo){
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

        double test = testNo.doubleValue();

        score3.setY(((test - 1) * 30));
        // Print the benchmark result

        score3.setText("RSA Encryption Score: " + score);


        return score;


    }

    private void appendDatabase(double matrix, double AES, double RSA){
        String connectionString = "mongodb+srv://xaty:KtnZPZybZtMfSn8t@404database.coe1uer.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("404Database");
            MongoCollection<Document> collection = database.getCollection("userScores");
            try {
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("userName", System.getProperty("user.name"))
                        .append("scoreAES", AES)
                        .append("scoreRSA", RSA)
                        .append("scoreMatrix", matrix));
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }
    }

    @FXML
    protected void runBenchmark(int [] options, String inputString, int matrixSize) {
        AtomicReference<Double> scoreMatrix = new AtomicReference<>((double) 0);
        AtomicReference<Double> scoreAES = new AtomicReference<>((double) 0);
        AtomicReference<Double> scoreRSA = new AtomicReference<>((double) 0);

        scoreMatrix.set(0.0);
        scoreAES.set(0.0);
        scoreRSA.set(0.0);

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
                        showCount.setText("Running AES test " + testDone + "/" + testLenght + "...");
                        Thread.sleep(2500);
                        scoreAES.set(cpuAES(inputString, testDone));
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } if (options[1] == 1) {
                try {
                    testDone.getAndIncrement();
                    showCount.setText("Running RSA test " + testDone + "/" + testLenght + "...");
                    Thread.sleep(2500);
                    scoreRSA.set(cpuRSA(inputString, testDone));

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                } if (options[2] == 1) {
                try {
                    testDone.getAndIncrement();
                    showCount.setText("Running Matrix test " + testDone + "/" + testLenght + "...");
                    Thread.sleep(2500);
                    scoreMatrix.set(matrixMultiplication(matrixSize, testDone));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                } if (options[3] == 1) {
                    // To implement
                }


            if(testLenght.get() == testDone.get()){
                showCount.setText("Benchmark completed!");
                dino_gif.setImage(new Image(getClass().getResource("/img/dino_happy.png").toString(), true));
                parallelTransition.stop();
                if(testDone.get() == 3){
                    appendDatabase(scoreMatrix.get(), scoreAES.get(), scoreRSA.get());
                }
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
    private Text score1;
    @FXML
    private Text score2;
    @FXML
    private Text score3;
    @FXML
    private Text score4;

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


    private int BACKGROUND_WIDTH = 993;
    private ParallelTransition parallelTransition;

    @FXML
    ImageView background1;

    @FXML
    ImageView background2;

    protected void jump() {

                TranslateTransition translation = new TranslateTransition(Duration.millis(100), dino_gif);
                translation.interpolatorProperty().set(Interpolator.SPLINE(.1, .1, .7, .7));
                translation.setByY(-50);
                translation.setAutoReverse(true);
                translation.setCycleCount(2);
                translation.play();
    }
    public synchronized void initialize() throws InterruptedException {

        /*
        Runnable task = () -> {
                    threadPool.execute(() -> {

                    });
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
*/

        TranslateTransition translateTransition =
                new TranslateTransition(Duration.millis(2000), background1);
        translateTransition.setFromX(0);
        translateTransition.setToX(-1 * BACKGROUND_WIDTH);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition translateTransition2 =
                new TranslateTransition(Duration.millis(2000), background2);
        translateTransition2.setFromX(0);
        translateTransition2.setToX(-1 * BACKGROUND_WIDTH);
        translateTransition2.setInterpolator(Interpolator.LINEAR);

        parallelTransition =
                new ParallelTransition( translateTransition, translateTransition2 );
        parallelTransition.setCycleCount(Animation.INDEFINITE);

        parallelTransition.play();

        EventHandler<KeyEvent> keyPressListener = e -> jump();

        dino_gif.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.removeEventHandler(KeyEvent.KEY_PRESSED, keyPressListener);
            }
            if (newScene != null) {
                newScene.addEventHandler(KeyEvent.KEY_PRESSED, keyPressListener);
            }
        });




    }

}
