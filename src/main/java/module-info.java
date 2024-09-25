module github.com.arithabandar.waiter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;

    opens github.com.arithabandar.waiter to javafx.fxml;

    opens github.com.arithabandar.waiter.control to javafx.fxml,javafx.base, javafx.swing;
    exports github.com.arithabandar.waiter.control to javafx.fxml, javafx.base, javafx.swing;

    exports github.com.arithabandar.waiter.control.networkAdapter to javafx.graphics,javafx.fxml;
    opens github.com.arithabandar.waiter.control.networkAdapter to javafx.fxml;
    exports github.com.arithabandar.waiter to javafx.fxml, javafx.graphics;
    exports github.com.arithabandar.waiter.control.table to javafx.base, javafx.fxml, javafx.swing;
    opens github.com.arithabandar.waiter.control.table to javafx.base, javafx.fxml, javafx.swing;

}