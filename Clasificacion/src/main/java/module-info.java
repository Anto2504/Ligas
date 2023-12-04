module com.example.clasificacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.clasificacion to javafx.fxml;
    exports com.example.clasificacion;
}