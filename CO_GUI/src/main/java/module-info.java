module ro.notfound.co_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.management;
    requires jdk.management;

    opens ro.notfound.co_gui to javafx.fxml;
    exports ro.notfound.co_gui;
}