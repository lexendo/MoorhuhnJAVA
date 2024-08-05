package org.example.moorhuhn;

import javafx.scene.layout.Pane;

/**
 * Manages the end-of-game process, including updating scores and displaying end game overlays.
 */
public class GameEndManager {
    private Pane gamePane;
    private int points;

    /**
     * Constructs a GameEndManager with the specified game pane and initial points.
     *
     * @param gamePane the game pane where the game is displayed.
     * @param points the initial points to be managed.
     */
    public GameEndManager(Pane gamePane, int points) {
        this.gamePane = gamePane;
        this.points = points;
    }

    /**
     * Prepares the game for ending by fading the game pane, saving the score, and updating the player's money.
     */
    public void prepareEndGame() {
        fadeGamePane();
        ScoreManager.saveScore(PlayerInfo.getInstance().getNickname(), points);
        ScoreManager.updateMoney(points);
    }

    /**
     * Sets the points to a new value.
     *
     * @param newPoints the new points to be set.
     */
    public void setPoints(int newPoints) {
        points = newPoints;
    }

    // Private methods below

    private void fadeGamePane() {
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");
        overlay.setPrefSize(gamePane.getWidth(), gamePane.getHeight());
        gamePane.getChildren().add(overlay);
    }
}
