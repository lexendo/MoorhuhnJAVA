package org.example.moorhuhn;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a score entry in the game, containing the player's nickname, score, and position.
 */
public class ScoreEntry {
    private SimpleStringProperty nickname;
    private SimpleIntegerProperty score;
    private SimpleIntegerProperty position;

    /**
     * Constructs a ScoreEntry with the specified nickname and score.
     *
     * @param nickname the player's nickname.
     * @param score the player's score.
     */
    public ScoreEntry(String nickname, int score) {
        this.nickname = new SimpleStringProperty(nickname);
        this.score = new SimpleIntegerProperty(score);
        this.position = new SimpleIntegerProperty();
    }

    /**
     * Gets the position property of the score entry.
     *
     * @return the position property.
     */
    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    /**
     * Gets the nickname of the player.
     *
     * @return the player's nickname.
     */
    public String getNickname() {
        return nickname.get();
    }

    /**
     * Gets the score of the player.
     *
     * @return the player's score.
     */
    public Integer getScore() {
        return score.get();
    }
}
