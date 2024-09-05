module ziad.bookstoresystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires okhttp3;
    requires org.json;




    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires java.rmi;


    opens ziad.bookstoresystem to javafx.fxml;
    exports ziad.bookstoresystem;
    exports ziad.bookstoresystem.Controllers;
    opens ziad.bookstoresystem.Controllers to javafx.fxml;

}