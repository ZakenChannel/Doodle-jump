module org.doodlejump.doodlejump {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.doodlejump to javafx.fxml;
    exports org.doodlejump;
    exports org.doodlejump.audiocontrol;
    opens org.doodlejump.audiocontrol to javafx.fxml;
}