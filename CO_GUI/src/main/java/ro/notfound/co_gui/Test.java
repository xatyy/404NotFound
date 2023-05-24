package ro.notfound.co_gui;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        var rectangle = new Rectangle(150, 100);

        var text = new Text("Hello, World!");
        text.setFill(Color.WHITE);

        var effect = new BoxBlur(0, 0, 1);
        text.setEffect(effect);

        var animation = new ParallelTransition(
                new FillTransition(Duration.seconds(1), rectangle, Color.BLACK, Color.GREEN),
                new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(effect.widthProperty(), 0),
                                new KeyValue(effect.heightProperty(), 0)
                        ),
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(effect.widthProperty(), 10),
                                new KeyValue(effect.heightProperty(), 10)
                        )
                )
        );
        rectangle.setOnMouseEntered(event -> {
            event.consume();
            animation.setRate(1);
            animation.play();
        });
        rectangle.setOnMouseExited(event -> {
            event.consume();
            animation.setRate(-1);
            animation.play();
        });

        primaryStage.setScene(new Scene(new StackPane(rectangle, text), 500, 300));
        primaryStage.show();
    }

}