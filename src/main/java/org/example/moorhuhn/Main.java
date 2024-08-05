package org.example.moorhuhn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main class for the Moorhuhn game application.
 * This class initializes and starts the JavaFX application.
 */
public class Main extends Application {

    private static MusicPlayer musicPlayer;


    /**
     * Default constructor for the Main.
     * Initializes a new instance of the Main class.
     */
    public Main() {
        // Default constructor
    }



    /**
     * Entry point for the JavaFX application.
     * Initializes the primary stage, loads the main menu, and starts background music.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception if an error occurs during loading FXML or resources.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        PlayerInfo playerInfo = PlayerInfo.getInstance();
        WeaponData equippedWeapon = playerInfo.getEquippedWeapon();

        musicPlayer = new MusicPlayer();
        musicPlayer.play();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/MainMenu.fxml")));
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/moorhuhn/pics/MoorhuhnIcon.png")));

        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Moorhuhn");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Stops the JavaFX application and releases resources.
     * Specifically, stops the background music if it is playing.
     */
    @Override
    public void stop() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    /**
     * The main method which serves as the entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
