/**
 * This module defines the org.example.moorhuhn module.
 * It requires the javafx.controls, javafx.fxml, javafx.media,
 * and java.desktop modules to function properly.
 *
 * It opens the org.example.moorhuhn package to javafx.fxml
 * and exports the org.example.moorhuhn package.
 */
module org.example.moorhuhn {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens org.example.moorhuhn to javafx.fxml;
    exports org.example.moorhuhn;
}
