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
import ro.notfound.co_gui.bench.ram.RAMmemoryUsage;
import ro.notfound.co_gui.logging.ConsoleLogger;
import ro.notfound.co_gui.logging.ILog;
import ro.notfound.co_gui.logging.TimeUnit;
import ro.notfound.co_gui.timing.ITimer;
import ro.notfound.co_gui.timing.Timer;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static ro.notfound.co_gui.bench.cpu.CPUAES.generateKey;
import static ro.notfound.co_gui.logging.TimeUnit.toTimeUnit;

public class Score_SceneController {
    private String connectionString = "mongodb+srv://xaty:KtnZPZybZtMfSn8t@404database.coe1uer.mongodb.net/?retryWrites=true&w=majority";
    ExecutorService threadPool = Executors.newWorkStealingPool();

    private Stage stage;
    private Scene scene;

    CPU_SpecsController cpu = new CPU_SpecsController();

    double matrixSingle = 0.0;
    double matrixMulti = 0.0;

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
                matrixSingle =  Math.round(score * 100) / 100 ;
            }
                multiThread = (int) score;
                matrixMulti =  Math.round(score * 100) / 100 ;
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

        double score = (double) Math.round(((CPUAES)aesBenchmark).score(timer,keySize) * 100) / 100 ;

        // Print the benchmark result
        System.out.println(aesBenchmark.getResult());

        double test = testNo.doubleValue();

        score2.setY(((test - 1) * 30));

        score2.setText("AES Encryption Score: " + (int) score);

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

        double score = (double) Math.round(((CPURSA)rsaBenchmark).score(timer,nrKeys) * 100) / 100 ;

        double test = testNo.doubleValue();

        score3.setY(((test - 1) * 30));
        // Print the benchmark result

        score3.setText("RSA Encryption Score: " + (int) score);


        return score;


    }

    @FXML
    public void RAM(int size, AtomicInteger testNo){
                IBenchmark bench = new RAMmemoryUsage();
                ILog log = new ConsoleLogger();
                bench.run(size);
                log.write(bench.getResult());

                double test = testNo.doubleValue();

                double score =abs(Double.parseDouble(bench.getResult()));

                score4.setY(((test - 1) * 30));
                // Print the benchmark result

                score4.setText("RAM Usage Score: " + (double) Math.round(score * 100) / 100);
    }


    private Boolean checkDuplicateDatabase() {

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("404Database");
            MongoCollection<Document> collection = database.getCollection("userScores");
            Document myDoc = collection.find(eq("MAC", getPCID())).first();
            System.out.println(myDoc);
            if(!(myDoc == null)){
                return false;
            }
        }

        return true;


    }

    private void appendDatabase(double matrix, double AES, double RSA){

       // String connectionString = "mongodb+srv://xaty:KtnZPZybZtMfSn8t@404database.coe1uer.mongodb.net/?retryWrites=true&w=majority";
        if(checkDuplicateDatabase()) {
            try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                MongoDatabase database = mongoClient.getDatabase("404Database");
                MongoCollection<Document> collection = database.getCollection("userScores");
                try {
                    InsertOneResult result = collection.insertOne(new Document()
                            .append("_id", new ObjectId())
                            .append("userName", System.getProperty("user.name"))
                            .append("cpuModel", cpu.getCpuModel())
                            .append("scoreAES", AES)
                            .append("scoreRSA", RSA)
                            .append("scoreMatrixSingle", matrixSingle)
                            .append("scoreMatrixMulti", matrixMulti)
                            .append("MAC", getPCID()));
                    System.out.println("Success! Inserted document id: " + result.getInsertedId());
                } catch (MongoException me) {
                    System.err.println("Unable to insert due to an error: " + me);
                }
            }
        } else {
            try (MongoClient mongoClient = MongoClients.create(connectionString)) {
                MongoDatabase database = mongoClient.getDatabase("404Database");
                MongoCollection<Document> collection = database.getCollection("userScores");
                try {
                    Document query = new Document();
                    query.append("MAC",getPCID());
                    Document setData = new Document();
                    setData.append("userName", System.getProperty("user.name"))
                            .append("cpuModel", cpu.getCpuModel())
                            .append("scoreAES", AES)
                            .append("scoreRSA", RSA)
                            .append("scoreMatrixSingle", matrix)
                            .append("scoreMatrixMulti", matrix)
                            .append("MAC", getPCID());
                    Document update = new Document();
                    update.append("$set", setData);
                    collection.updateOne(query, update);
                } catch (MongoException me) {
                    System.err.println("Unable to update due to an error: " + me);
                }
            }
        }
    }

    @FXML
    protected void runBenchmark(int [] options, String inputString, int size) {
        AtomicReference<Double> scoreMatrix = new AtomicReference<>((double) 0);
        AtomicReference<Double> scoreAES = new AtomicReference<>((double) 0);
        AtomicReference<Double> scoreRSA = new AtomicReference<>((double) 0);
        AtomicReference<Double> scoreRAM = new AtomicReference<>((double) 0);

        scoreMatrix.set(0.0);
        scoreAES.set(0.0);
        scoreRSA.set(0.0);
        scoreRAM.set(0.0);

        AtomicInteger testLenght = new AtomicInteger();
        AtomicInteger testDone = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
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
                    scoreMatrix.set(matrixMultiplication(size, testDone));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                } if (options[3] == 1) {
                try {
                    testDone.getAndIncrement();
                    showCount.setText("Running RAM test " + testDone + "/" + testLenght + "...");
                    Thread.sleep(2500);
                    RAM(size, testDone);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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

    public static String getPCID() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X", mac[i]));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
