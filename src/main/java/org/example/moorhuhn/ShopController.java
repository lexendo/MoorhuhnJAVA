package org.example.moorhuhn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;

import java.io.IOException;
import java.util.Objects;
import java.util.List;

/**
 * Controller for the in-game shop, managing the display and purchase of weapons.
 */
public class ShopController {

    /**
     * Button which onclick equips or buys a currently selected weapon
     */
    @FXML public Button BuyEquipButton;
    @FXML private ImageView flameThrowerView, superGunView, shotgunView, pistolView;
    @FXML private Label weaponInfoLabel, weaponNameLabel, weaponCost, moneyLabel;

    private List<WeaponData> weaponsInventory;
    private ImageView selectedWeaponView;
    private WeaponData selectedWeapon;
    private long money;


    /**
     * Default constructor for the ShopController.
     * Initializes a new instance of the ShopController class.
     */
    public ShopController() {
        // Default constructor
    }
    /**
     * Initializes the shop controller, setting up the weapon information and the player's current money.
     */
    public void initialize() {
        weaponsInventory = PlayerInfo.getInstance().getWeaponsInventory();
        setupWeaponInfo();
        money = ScoreManager.readCurrentMoney();
        moneyLabel.setText("$" + money);
    }

    /**
     * Handles the click event for the buy/equip button. Buys or equips the selected weapon.
     *
     * @param actionEvent the action event triggered by clicking the button.
     * @throws IOException if an I/O error occurs while updating the weapons file.
     */
    @FXML
    private void handleBuyEquipButtonClick(ActionEvent actionEvent) throws IOException {
        if ("Buy".equals(BuyEquipButton.getText())) {
            if (selectedWeapon.getCost() <= money) {
                selectedWeapon.setBought(true);
                money -= selectedWeapon.getCost();
                moneyLabel.setText("$" + money);
                BuyEquipButton.setText("Equip");
                BuyEquipButton.setDisable(false);
            }
        } else if ("Equip".equals(BuyEquipButton.getText())) {
            weaponsInventory.forEach(weapon -> weapon.setEquipped(false));
            selectedWeapon.setEquipped(true);
            BuyEquipButton.setText("Equipped");
            BuyEquipButton.setDisable(true);
            updateButton(selectedWeapon);
        }
        PlayerInfo.getInstance().updateWeaponsFile();
        ScoreManager.saveMoney(money);

    }

    /**
     * Handles the click event for the quit button. Exits the application.
     */
    @FXML
    private void handleQuitButtonClick() {
        System.exit(0);
    }

    /**
     * Handles the click event for the menu button. Navigates back to the main menu.
     *
     * @param actionEvent the action event triggered by clicking the button.
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

    // Private methods below

    private void setupWeaponInfo() {
        ImageView[] views = {pistolView, shotgunView, flameThrowerView, superGunView};
        for (int i = 0; i < weaponsInventory.size(); i++) {
            if (i < views.length) {
                configureWeaponView(views[i], weaponsInventory.get(i));
            }
        }
    }

    private void configureWeaponView(ImageView imageView, WeaponData weapon) {
        DropShadow shadow = new DropShadow(20, javafx.scene.paint.Color.BLACK);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), imageView);
        st.setToX(1.1);
        st.setToY(1.1);

        imageView.setOnMouseClicked(_ -> {
            selectWeapon(weapon, imageView, shadow, st);
        });
    }

    private void selectWeapon(WeaponData weapon, ImageView imageView, DropShadow shadow, ScaleTransition st) {
        if (selectedWeaponView != null) {
            selectedWeaponView.setEffect(null);
            selectedWeaponView.setScaleX(1.0);
            selectedWeaponView.setScaleY(1.0);
        }
        selectedWeapon = weapon;
        selectedWeaponView = imageView;
        imageView.setEffect(shadow);
        st.playFromStart();
        weaponNameLabel.setText(weapon.getName());
        weaponInfoLabel.setText(weapon.getDescription());
        weaponCost.setText(weapon.isBought() ? "Owned" : "$" + weapon.getCost());
        BuyEquipButton.setVisible(true);
        updateButton(weapon);
    }

    private void updateButton(WeaponData weapon) {
        if (!weapon.isBought()) {
            BuyEquipButton.setText("Buy");
            BuyEquipButton.setDisable(weapon.getCost() > money);
        } else {
            if (weapon.isEquipped()) {
                BuyEquipButton.setText("Equipped");
                BuyEquipButton.setDisable(true);
            } else {
                BuyEquipButton.setText("Equip");
                BuyEquipButton.setDisable(false);
            }
        }
    }
}
