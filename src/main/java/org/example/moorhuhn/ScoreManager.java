package org.example.moorhuhn;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manages saving and loading of scores and money for the game.
 */
public class ScoreManager {
    private static final String SCORE_FILE = "scores.txt";
    private static final String MONEY_FILE = "money.txt";



    /**
     * Default constructor for the ScoreManager.
     * Initializes a new instance of the ScoreManager class.
     */
    public ScoreManager() {
        // Default constructor
    }

    /**
     * Saves the score of a player with the given nickname.
     *
     * @param nickname the nickname of the player.
     * @param score the score to be saved.
     */
    public static void saveScore(String nickname, int score) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SCORE_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(nickname + " - " + score + "\n");
        } catch (IOException e) {
            System.err.println("Unable to save score: " + e.getMessage());
        }
    }

    /**
     * Loads all saved scores.
     *
     * @return a list of strings, each representing a saved score.
     */
    public static List<String> loadScores() {
        List<String> scores = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            System.err.println("Unable to read scores: " + e.getMessage());
        }
        return scores;
    }

    /**
     * Updates the current money by adding the specified earnings.
     *
     * @param earnings the amount to be added to the current money.
     */
    public static void updateMoney(long earnings) {
        try {
            long currentMoney = readCurrentMoney();
            currentMoney += earnings;
            saveMoney(currentMoney);
        } catch (IOException e) {
            System.err.println("Failed to update money: " + e.getMessage());
        }
    }

    /**
     * Saves the specified amount of money.
     *
     * @param money the amount of money to be saved.
     * @throws IOException if an I/O error occurs.
     */
    public static void saveMoney(long money) throws IOException {
        Files.write(Paths.get(MONEY_FILE), String.valueOf(money).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Reads the current amount of money.
     *
     * @return the current amount of money.
     */
    public static long readCurrentMoney() {
        Path path = Paths.get(MONEY_FILE);
        if (Files.exists(path)) {
            try {
                String content = new String(Files.readAllBytes(path));
                return Long.parseLong(content.trim());
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error reading money: " + e.getMessage());
            }
        }
        return 0L;
    }
}
