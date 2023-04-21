module ro.notfound.co_gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens ro.notfound.co_gui to javafx.fxml;
    exports ro.notfound.co_gui;
}