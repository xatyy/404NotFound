module ro.notfound.co_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    requires java.management;
    requires jdk.management;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires slf4j.api;


    opens ro.notfound.co_gui to javafx.fxml;
    exports ro.notfound.co_gui;
}