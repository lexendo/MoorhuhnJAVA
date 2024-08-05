package org.example.moorhuhn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Controller for the achievements scene, managing navigation back to the main menu or quitting the game.
 */
public class AchievementsController {



    /**
     * Default constructor for the AchievementsController.
     * Initializes a new instance of the AchievementsController.
     */
    public AchievementsController() {
        // Default constructor
    }


    /**
     * Handles the quit button click event. Exits the application.
     */
    @FXML
    private void handleQuitButtonClick() {
        System.exit(0);
    }

    /**
     * Handles the menu button click event. Navigates back to the main menu.
     *
     * @param actionEvent the action event triggered by clicking the menu button.
     * @throws Exception if an error occurs while loading the main menu.
     */
    @FXML
    public void handleMenuButtonClick(ActionEvent actionEvent) throws Exception {
        Parent mainMenuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/MainMenu.fxml")));
        Scene mainMenuScene = new Scene(mainMenuRoot);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(mainMenuScene);
        stage.show();
    }
}
