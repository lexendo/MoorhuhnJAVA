package org.example.moorhuhn;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the main menu of the game, handling user interactions for starting the game, viewing the leaderboard, and more.
 */
public class MenuController {

    @FXML
    private TextField nicknameField;
    @FXML
    private Label nicknameErrorLabel;



    /**
     * Default constructor for the MenuController.
     * Initializes a new instance of the MenuController class.
     */
    public MenuController() {
        // Default constructor
    }

    /**
     * Initializes the menu controller by loading the last used nickname from the scores file.
     */
    public void initialize() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("scores.txt"));
            if (!lines.isEmpty()) {
                String lastLine = lines.get(lines.size() - 1);
                String lastNickname = lastLine.split(" - ")[0];
                nicknameField.setText(lastNickname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the play button click event. Validates the nickname and starts the game if valid.
     *
     * @param actionEvent the action event triggered by clicking the play button.
     */
    @FXML
    public void handlePlayButtonClick(ActionEvent actionEvent) {
        String input = nicknameField.getText().trim();

        if (input.isEmpty()) {
            nicknameErrorLabel.setText("Choose your nickname");
            nicknameErrorLabel.setVisible(true);
            SequentialTransition sequence = getSequentialTransition();
            sequence.play();
        } else if (!input.matches("[a-zA-Z0-9 ]*")) {
            nicknameErrorLabel.setText("Unallowed characters");
            nicknameErrorLabel.setVisible(true);
            SequentialTransition sequence = getSequentialTransition();
            sequence.play();
        } else {
            nicknameErrorLabel.setVisible(false);

            try {
                Parent gameRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/Game.fxml")));
                Scene gameScene = new Scene(gameRoot);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(gameScene);
                stage.show();
                PlayerInfo.getInstance().setNickname(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the leaderboard button click event. Navigates to the leaderboard scene.
     *
     * @param actionEvent the action event triggered by clicking the leaderboard button.
     */
    @FXML
    private void handleLeaderboardButtonClick(ActionEvent actionEvent) {
        try {
            Parent gameRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/Leaderboard.fxml")));
            Scene leaderscoreScene = new Scene(gameRoot);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(leaderscoreScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the quit button click event. Exits the application.
     */
    @FXML
    private void handleQuitButtonClick() {
        System.exit(0);
    }

    /**
     * Handles the armory button click event. Navigates to the armory scene.
     *
     * @param actionEvent the action event triggered by clicking the armory button.
     */
    public void handleArmoryButtonClick(ActionEvent actionEvent) {
        try {
            Parent gameRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/Armory.fxml")));
            Scene armoryScene = new Scene(gameRoot);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(armoryScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the achievements button click event. Navigates to the achievements scene.
     *
     * @param actionEvent the action event triggered by clicking the achievements button.
     */
    public void handleAchievementsButtonClick(MouseEvent actionEvent) {
        try {
            Parent gameRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/Achievements.fxml")));
            Scene achievementsScene = new Scene(gameRoot);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(achievementsScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Private methods below

    private SequentialTransition getSequentialTransition() {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(150), nicknameErrorLabel);
        scaleIn.setFromX(0.0);
        scaleIn.setFromY(0.0);
        scaleIn.setToX(1.2);
        scaleIn.setToY(1.2);

        ScaleTransition scaleBack = new ScaleTransition(Duration.millis(150), nicknameErrorLabel);
        scaleBack.setToX(1.0);
        scaleBack.setToY(1.0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), nicknameErrorLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition showTransition = new ParallelTransition(fadeIn, scaleIn);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), nicknameErrorLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        return new SequentialTransition(showTransition, scaleBack, pause, fadeOut);
    }
}
