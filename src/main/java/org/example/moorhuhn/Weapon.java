package org.example.moorhuhn;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Represents a weapon in the game, handling ammunition, reloading, and shooting.
 */
public class Weapon {
    private int currentAmmo;
    private final int maxAmmo;
    private final double reloadTime;
    private final Label ammoLabel;
    private boolean isReloading = false;
    private final Sounds soundEffects = new Sounds();
    private final GameController gameController;

    /**
     * Constructs a Weapon with the specified maximum ammunition, reload time, ammo label, and game controller.
     *
     * @param maxAmmo the maximum ammunition capacity of the weapon.
     * @param reloadTime the time it takes to reload the weapon.
     * @param ammoLabel the label to display the current ammunition status.
     * @param gameController the game controller to interact with the game state.
     */
    public Weapon(int maxAmmo, double reloadTime, Label ammoLabel, GameController gameController) {
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.ammoLabel = ammoLabel;
        this.gameController = gameController;
        this.reloadTime = reloadTime;
        updateAmmoDisplay();
    }

    /**
     * Shoots the weapon if there is ammunition available and the weapon is not reloading.
     *
     * @return true if the weapon was successfully shot, false otherwise.
     */
    public boolean shoot() {
        if (currentAmmo > 0 && !isReloading) {
            soundEffects.playGunshot();
            currentAmmo--;
            updateAmmoDisplay();
            gameController.drawAmmo();
            return true;
        }
        if (currentAmmo < 1) {
            soundEffects.playNoAmmoSound();
        }
        return false;
    }

    /**
     * Gets the current ammunition count.
     *
     * @return the current ammunition count.
     */
    public int getCurrentAmmo() {
        return currentAmmo;
    }

    /**
     * Reloads the weapon if it is not already reloading and if the current ammo is not at maximum capacity.
     */
    public void reload() {
        if (gameController.isFirethrowerActive()) {
            return;
        }
        if (!isReloading && currentAmmo != maxAmmo) {
            isReloading = true;
            soundEffects.playGunReloadSound();
            updateAmmoDisplay();

            PauseTransition pause = new PauseTransition(Duration.seconds(reloadTime));
            pause.setOnFinished(_ -> {
                currentAmmo = maxAmmo;
                isReloading = false;
                updateAmmoDisplay();
                gameController.drawAmmo();
            });
            pause.play();
        }
    }

    // Private methods below

    private void updateAmmoDisplay() {
        if (isReloading) {
            ammoLabel.setText("Reloading...");
        } else if (getCurrentAmmo() == 0) {
            ammoLabel.setText("SPACE to reload");
        }
        if (getCurrentAmmo() == maxAmmo) {
            ammoLabel.setText("");
        }
    }
}
