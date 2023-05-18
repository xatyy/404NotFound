package ro.notfound.co_gui;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import static com.mongodb.client.model.Filters.eq;
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("404 Benchmark");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        /*
        String connectionString = "mongodb+srv://xaty:KtnZPZybZtMfSn8t@404database.coe1uer.mongodb.net/?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("404Database");
            MongoCollection<Document> collection = database.getCollection("userScores");
            Document doc = collection.find(eq("userName", "PC-1")).first();
            System.out.println(doc.toJson());
        }*/
    }


    public static void main(String[] args) {
        launch();
    }
}