package org.example.moorhuhn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the leaderboard, managing the display of scores and navigation to other scenes.
 */
public class LeaderboardController {
    @FXML private TableView<ScoreEntry> scoreTable;
    @FXML private TableColumn<ScoreEntry, Integer> positionColumn;
    @FXML private TableColumn<ScoreEntry, String> nicknameColumn;
    @FXML private TableColumn<ScoreEntry, Integer> scoreColumn;

    private ObservableList<ScoreEntry> scoreData = FXCollections.observableArrayList();

    /**
     * Default constructor for the LeaderboardController.
     * Initializes a new instance of the Main LeaderboardController.
     */
    public LeaderboardController() {
        // Default constructor
    }



    /**
     * Initializes the leaderboard controller, setting up the table columns and loading the scores.
     */
    public void initialize() {
        positionColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty().asObject());
        nicknameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        positionColumn.setCellFactory(column -> new TableCell<ScoreEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                    if (getIndex() == 0) {
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/moorhuhn/pics/medal1.png"))));
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        setGraphic(imageView);
                    } else if (getIndex() == 1) {
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/moorhuhn/pics/medal2.png"))));
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        setGraphic(imageView);
                    } else if (getIndex() == 2) {
                        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/moorhuhn/pics/medal3.png"))));
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        loadScores();
    }

    /**
     * Loads the scores from the score manager and populates the table with them.
     */
    private void loadScores() {
        List<String> scores = ScoreManager.loadScores();
        scoreData.clear();
        for (String entry : scores) {
            String[] parts = entry.split(" - ");
            scoreData.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
        }
        scoreData.sort(Comparator.comparing(ScoreEntry::getScore).reversed());
        scoreTable.setItems(scoreData);
    }

    /**
     * Handles the menu button click event. Navigates back to the main menu.
     *
     * @param actionEvent the action event triggered by clicking the menu button.
     */
    public void handleMenuButtonClick(ActionEvent actionEvent) {
        try {
            Parent mainMenuRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/moorhuhn/MainMenu.fxml")));
            Scene mainMenuScene = new Scene(mainMenuRoot);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene);
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
}
