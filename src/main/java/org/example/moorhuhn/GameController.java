package org.example.moorhuhn;

import java.net.URL;
import java.util.*;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.util.Duration;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

/**
 * Controller class for the game. Manages the game state, user interactions, and animations.
 */
public class GameController {
    /**
     * Controller class for the game. Manages the game state, user interactions, and animations.
     */
    private URL imageUrl;
    private Image bulletImage;

    @FXML private Pane gamePane;
    @FXML private Label ScoreLabel;
    @FXML private Label ammoLabel;
    @FXML private Label GameTimer;
    @FXML private HBox bulletsContainer;
    @FXML private VBox endGameVbox;
    @FXML private Label gameOverLabel;
    @FXML private Label finalScoreLabel;
    @FXML private ProgressBar fireLeftPBar;

    private int gameTime = 60;
    private int points = 0;
    private int bulletIconSize = 70;

    private final ObservableList<Chicken> chickens = FXCollections.observableArrayList();
    private final Map<Chicken, ImageView> chickenViews = new HashMap<>();
    private final ObservableList<Bird> Birds = FXCollections.observableArrayList();
    private final Map<Bird, ImageView> BirdsViews = new HashMap<>();

    private final Random random = new Random();
    private Weapon weapon;
    private GameEndManager endManager;
    private WeaponData equippedWeapon;

    private Image[] animationFrames;
    private Timeline spawnTimeline;
    private Timeline spawnTimeLineBird;
    private Timeline gameTimerTimeline;
    private AnimationTimer movementTimer;
    private Timeline birdAnimationTimeline;

    private boolean firethrowerActive = false;


    /**
     * Default constructor for the GameController.
     * Initializes a new instance of the Main GameController.
     */
    public GameController() {
        // Default constructor
    }



    /**
     * Checks if the firethrower weapon is active.
     *
     * @return true if the firethrower is active, false otherwise.
     */
    public boolean isFirethrowerActive() {
        return firethrowerActive;
    }

    /**
     * Initializes the game. Sets up the game pane, cursor, weapons, spawners, timers, and key bindings.
     */
    public void initialize() {
        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();
        imageUrl = getClass().getResource("/org/example/moorhuhn/pics/StandingChicken.png");

        loadAnimationFrames();
        Image cursorImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/moorhuhn/pics/crosshair2.png")));
        int hotspotX = 25;
        int hotspotY = 25;
        gamePane.setCursor(new ImageCursor(cursorImage, hotspotX, hotspotY));

        equippedWeapon = PlayerInfo.getInstance().getEquippedWeapon();
        String weaponImage = "";
        switch (equippedWeapon.getName()) {
            case "Johny's Shotgun":
                weaponImage = "/org/example/moorhuhn/pics/ShotgunShell.png";
                break;
            case "Inferno Firethrower":
                weaponImage = "/org/example/moorhuhn/pics/fireBullet.png";
                bulletIconSize = 130;
                break;
            case "Supergalactic Obliterator":
                bulletIconSize = 150;
                weaponImage = "/org/example/moorhuhn/pics/galacticBlast.png";
                break;
            default:
                weaponImage = "/org/example/moorhuhn/pics/Bullet.png";
        }
        bulletImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(weaponImage)));
        weapon = new Weapon(equippedWeapon.getMaxAmmo(), equippedWeapon.getReloadTime(), ammoLabel, this);

        endManager = new GameEndManager(gamePane, points);

        setupMobsSpawner();
        setupGameTimer();
        setupBirdAnimation();

        startRendering();

        setupClickHandling();
        setupKeyBindings();
        drawAmmo();
        bulletsContainer.toFront();
    }

    /**
     * Draws the current ammo on the UI.
     */
    public void drawAmmo() {
        bulletsContainer.getChildren().clear();
        for (int i = 0; i < weapon.getCurrentAmmo(); i++) {
            ImageView bulletIcon = new ImageView(bulletImage);
            bulletIcon.setFitHeight(bulletIconSize);
            bulletIcon.setPreserveRatio(true);
            bulletsContainer.getChildren().add(bulletIcon);
        }
    }

    /**
     * Handles the menu button click event. Loads and displays the main menu.
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
            System.out.println("Error loading MainMenu.fxml");
        }
    }

    /**
     * Handles the quit button click event. Exits the application.
     */
    @FXML
    private void handleQuitButtonClick() {
        System.out.println("Quit button clicked!");
        System.exit(0);
    }

    /**
     * Handles the restart button click event. Reloads and restarts the game.
     *
     * @param actionEvent the action event triggered by clicking the restart button.
     */
    @FXML
    public void handleRestartButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/moorhuhn/Game.fxml"));
            Parent gameRoot = loader.load();
            Scene gameScene = new Scene(gameRoot);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(gameScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading game scene.");
        }
    }

    // Private methods below

    private void setupKeyBindings() {
        gamePane.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case R:
                        case SPACE:
                            weapon.reload();
                            break;
                        default:
                            break;
                    }
                });
                gamePane.requestFocus();
            }
        });
    }

    private void setupGameTimer() {
        gameTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
            gameTime--;
            GameTimer.setText(String.valueOf(gameTime));
            if (gameTime <= 0) {
                endGame();
            }
        }));
        gameTimerTimeline.setCycleCount(Timeline.INDEFINITE);
        gameTimerTimeline.play();
    }

    private void setupBirdAnimation() {
        birdAnimationTimeline = new Timeline(new KeyFrame(Duration.seconds(0.05), _ -> {
            Birds.forEach(bird -> {
                bird.nextFrame();
                ImageView birdView = BirdsViews.get(bird);
                if (birdView != null) {
                    birdView.setImage(bird.getCurrentFrame());
                }
            });
        }));
        birdAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
        birdAnimationTimeline.play();
    }

    private void loadAnimationFrames() {
        animationFrames = new Image[6];
        for (int i = 1; i <= 6; i++) {
            animationFrames[i - 1] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/moorhuhn/birdAnimation/bird" + i + ".png")));
        }
    }

    private void setupMobsSpawner() {
        spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> spawnChicken()));
        spawnTimeline.setCycleCount(Timeline.INDEFINITE);
        spawnTimeline.play();

        spawnTimeLineBird = new Timeline(new KeyFrame(Duration.seconds(1), _ -> spawnBird()));
        spawnTimeLineBird.setCycleCount(Timeline.INDEFINITE);
        spawnTimeLineBird.play();
    }

    private void spawnChicken() {
        double x = 10 + random.nextDouble() * (gamePane.getPrefWidth() - 60);

        double minY = 400;
        double maxY = gamePane.getPrefHeight() - 50;

        double y = minY + random.nextDouble() * (maxY - minY);
        int size = random.nextInt(60) + 30;
        Chicken chicken = new Chicken(x, y, size);
        chickens.add(chicken);
        ImageView chickenView = new ImageView(imageUrl.toString());
        chickenView.setX(x);
        chickenView.setY(y);
        chickenView.setPreserveRatio(true);
        chickenView.setFitHeight(size);
        gamePane.getChildren().add(chickenView);
        chickenViews.put(chicken, chickenView);
    }

    private void spawnBird() {
        double y = 20 + random.nextDouble() * (gamePane.getHeight() - 150);
        int size = random.nextInt(120) + 30;
        Bird chicken = new Bird(-50, y, size, animationFrames);
        Birds.add(chicken);
        ImageView view = new ImageView(animationFrames[0]);
        view.xProperty().bind(chicken.xProperty());
        view.yProperty().bind(chicken.yProperty());
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        gamePane.getChildren().add(view);
        BirdsViews.put(chicken, view);
    }

    private void showShotEffect(double x, double y) {
        Circle shotEffect = new Circle(x, y, 5);
        shotEffect.setFill(Color.RED);

        gamePane.getChildren().add(shotEffect);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), shotEffect);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(_ -> gamePane.getChildren().remove(shotEffect));
        fade.play();
    }

    private void setupClickHandling() {
        gamePane.setOnMouseClicked(event -> {
            double clickX = event.getX();
            double clickY = event.getY();

            if (weapon.shoot()) {
                drawAmmo();
                switch (equippedWeapon.getName()) {
                    case "Supergalactic Obliterator":
                        supergalacticObliteratorUse(clickX, clickY);
                        break;
                    case "Johny's Shotgun":
                        shotgunShoot(clickX, clickY);
                        break;
                    case "Inferno Firethrower":
                        activateFirethrower();
                        break;
                    default:
                        deagleShoot(clickX, clickY);
                        break;
                }
            }
        });
    }

    private void deagleShoot(double clickX, double clickY) {
        showShotEffect(clickX, clickY);
        boolean anyHit = checkAndHandleHits(clickX, clickY);

        if (!anyHit) {
            displayScoreLabel(true, -10, clickX, clickY);
            points -= 10;
        }

        ScoreLabel.setText(String.valueOf(points));
    }

    private void activateFirethrower() {
        if (firethrowerActive) return;

        firethrowerActive = true;
        fireLeftPBar.setVisible(true);
        fireLeftPBar.setProgress(1.0);
        ammoLabel.setVisible(false);
        Timeline countdown = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.seconds(0.04), _ -> {
            double progress = fireLeftPBar.getProgress();
            fireLeftPBar.setProgress(progress - 0.01);
            if (progress <= 0.01) {
                firethrowerActive = false;
                fireLeftPBar.setProgress(0);
                fireLeftPBar.setVisible(false);
                ammoLabel.setVisible(true);
                gamePane.setOnMouseMoved(null);
            }
        });

        countdown.getKeyFrames().add(frame);
        countdown.setCycleCount(100);
        countdown.play();

        gamePane.setOnMouseMoved(event -> {
            if (firethrowerActive) {
                double mouseX = event.getX();
                double mouseY = event.getY();
                burnTargets(mouseX, mouseY);
                createFogEffect(mouseX, mouseY, 15, Color.rgb(255, 140, 0, 0.8), 0.3);
            }
        });
    }

    private void burnTargets(double mouseX, double mouseY) {
        List<Chicken> hitChickens = new ArrayList<>();
        chickens.stream().filter(chicken -> isChickenClicked(chickenViews.get(chicken), mouseX, mouseY)).forEach(chicken -> {
            int scoreAwarded = 100 - chicken.getSize();
            points += scoreAwarded;
            hitChickens.add(chicken);
            gamePane.getChildren().remove(chickenViews.get(chicken));
            displayScoreLabel(false, scoreAwarded, chickenViews.get(chicken).getX(), chickenViews.get(chicken).getY());
        });
        chickens.removeAll(hitChickens);

        List<Bird> hitBirds = new ArrayList<>();
        Birds.stream().filter(bird -> isChickenClicked(BirdsViews.get(bird), mouseX, mouseY)).forEach(bird -> {
            int scoreAwarded = 180 - bird.getSize();
            points += scoreAwarded;
            hitBirds.add(bird);
            gamePane.getChildren().remove(BirdsViews.get(bird));
            displayScoreLabel(false, scoreAwarded, BirdsViews.get(bird).getX(), BirdsViews.get(bird).getY());
        });
        Birds.removeAll(hitBirds);

        ScoreLabel.setText(String.valueOf(points));
    }

    private void shotgunShoot(double clickX, double clickY) {
        createFogEffect(clickX, clickY, 100, Color.rgb(255, 105, 180), 2);
        int pellets = 8;
        double maxRadius = 100;
        Random random = new Random();
        boolean anyHit = false;

        anyHit |= checkAndHandleHits(clickX, clickY);
        showShotEffect(clickX, clickY);

        for (int i = 1; i < pellets; i++) {
            double angle = Math.toRadians(random.nextDouble() * 360);
            double radius = random.nextDouble() * maxRadius;

            double pelletX = clickX + radius * Math.cos(angle);
            double pelletY = clickY + radius * Math.sin(angle);

            anyHit |= checkAndHandleHits(pelletX, pelletY);
            showShotEffect(pelletX, pelletY);
        }

        if (!anyHit) {
            displayScoreLabel(true, -10, clickX, clickY);
            points -= 10;
        }
        ScoreLabel.setText(String.valueOf(points));
    }

    private boolean checkAndHandleHits(double x, double y) {
        boolean chickenHit = false;
        Iterator<Chicken> chickenIterator = chickens.iterator();
        while (chickenIterator.hasNext()) {
            Chicken chicken = chickenIterator.next();
            ImageView chickenView = chickenViews.get(chicken);
            if (isChickenClicked(chickenView, x, y)) {
                chickenHit = true;
                int scoreAwarded = 100 - chicken.getSize();
                points += scoreAwarded;
                chickenViews.remove(chicken);
                gamePane.getChildren().remove(chickenView);
                displayScoreLabel(false, scoreAwarded, chickenView.getX() + chickenView.getFitWidth() / 2, chickenView.getY());
                chickenIterator.remove();
            }
        }

        Iterator<Bird> animatedChickenIterator = Birds.iterator();
        while (animatedChickenIterator.hasNext()) {
            Bird bird = animatedChickenIterator.next();
            ImageView birdView = BirdsViews.get(bird);
            if (isChickenClicked(birdView, x, y)) {
                chickenHit = true;
                int scoreAwarded = 180 - bird.getSize();
                points += scoreAwarded;
                BirdsViews.remove(bird);
                gamePane.getChildren().remove(birdView);
                displayScoreLabel(false, scoreAwarded, birdView.getX() + birdView.getFitWidth() / 2, birdView.getY());
                animatedChickenIterator.remove();
            }
        }

        return chickenHit;
    }

    private void supergalacticObliteratorUse(double clickX, double clickY) {
        createFogEffect(clickX, clickY, 400, Color.rgb(65, 105, 225), 2);

        Iterator<Chicken> iterator = chickens.iterator();
        while (iterator.hasNext()) {
            Chicken chicken = iterator.next();
            ImageView chickenView = chickenViews.get(chicken);
            if (Math.hypot(chickenView.getX() - clickX, chickenView.getY() - clickY) <= 400) {
                gamePane.getChildren().remove(chickenView);
                iterator.remove();
                int scoreAwarded = 100 - chicken.getSize();
                points += scoreAwarded;
                displayScoreLabel(false, scoreAwarded, chickenView.getX() + chickenView.getFitWidth() / 2, chickenView.getY());
                ScoreLabel.setText(String.valueOf(points));
            }
        }

        Iterator<Bird> animatedChickenIterator = Birds.iterator();
        while (animatedChickenIterator.hasNext()) {
            Bird chicken = animatedChickenIterator.next();
            ImageView birdView = BirdsViews.get(chicken);
            if (Math.hypot(birdView.getX() - clickX, birdView.getY() - clickY) <= 400) {
                int scoreAwarded = 180 - chicken.getSize();
                points += scoreAwarded;
                BirdsViews.remove(chicken);
                gamePane.getChildren().remove(birdView);
                displayScoreLabel(false, scoreAwarded, birdView.getX() + birdView.getFitWidth() / 2, birdView.getY());
                ScoreLabel.setText(String.valueOf(points));
                animatedChickenIterator.remove();
            }
        }
    }

    private void createFogEffect(double clickX, double clickY, double radius, Color color, double fadeOut) {
        Circle fogEffect = new Circle(clickX, clickY, radius);
        RadialGradient gradient = new RadialGradient(0, 0, clickX, clickY, radius, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0.7)),
                new Stop(1, Color.color(color.getRed(), color.getGreen(), color.getBlue(), 0)));

        fogEffect.setFill(gradient);
        gamePane.getChildren().add(fogEffect);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(fadeOut), fogEffect);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(_ -> gamePane.getChildren().remove(fogEffect));
        fadeTransition.play();
    }

    private boolean isChickenClicked(ImageView chickenView, double x, double y) {
        return chickenView.getBoundsInParent().contains(x, y);
    }

    private void displayScoreLabel(boolean negative, int score, double x, double y) {
        Label tempLabel = new Label("+" + score);
        if (negative) {
            tempLabel.setText(String.valueOf(score));
            tempLabel.setStyle("-fx-font-size: 24; -fx-text-fill: red; -fx-font-weight: bold;");
        } else {
            tempLabel.setStyle("-fx-font-size: 24; -fx-text-fill: gold; -fx-font-weight: bold;");
        }
        tempLabel.setLayoutX(x);
        tempLabel.setLayoutY(y);
        gamePane.getChildren().add(tempLabel);

        FadeTransition fade = new FadeTransition(Duration.seconds(1), tempLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(_ -> gamePane.getChildren().remove(tempLabel));
        fade.play();
    }

    private void startRendering() {
        movementTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBirds();
            }
        };
        movementTimer.start();
    }

    private void updateBirds() {
        Iterator<Bird> it = Birds.iterator();
        while (it.hasNext()) {
            Bird bird = it.next();
            bird.setX(bird.getX() + 2);
            ImageView view = BirdsViews.get(bird);
            if (view != null) {
                view.setImage(bird.getCurrentFrame());
            }

            if (bird.getX() > gamePane.getWidth()) {
                gamePane.getChildren().remove(view);
                BirdsViews.remove(bird);
                it.remove();
            }
        }
    }

    private void endGame() {
        if (spawnTimeline != null) {
            spawnTimeline.stop();
        }
        if (gameTimerTimeline != null) {
            gameTimerTimeline.stop();
        }
        if (spawnTimeLineBird != null) {
            spawnTimeLineBird.stop();
        }
        if (movementTimer != null) {
            movementTimer.stop();
        }
        if (birdAnimationTimeline != null) {
            birdAnimationTimeline.stop();
        }
        firethrowerActive = false;
        gamePane.setOnMouseClicked(null);
        endManager.setPoints(points);
        endManager.prepareEndGame();
        endGameVbox.toFront();
        gameOverLabel.setText("Game Over, " + PlayerInfo.getInstance().getNickname() + "!");
        finalScoreLabel.setText("Your score: " + points);

        endGameVbox.setVisible(true);

        Pane blockPane = new Pane();
        blockPane.setPrefSize(gamePane.getWidth(), gamePane.getHeight());
        blockPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        gamePane.getChildren().add(blockPane);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
        pause.setOnFinished(event -> gamePane.getChildren().remove(blockPane));
        pause.play();
    }
}
