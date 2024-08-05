package org.example.moorhuhn;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * Manages sound effects for the game, including gunshot, no ammo, and reload sounds.
 */
public class Sounds {

    private MediaPlayer gunshotPlayer;
    private MediaPlayer noAmmoPlayer;
    private MediaPlayer gunReloadPlayer;

    /**
     * Constructs a Sounds object and loads all sound effects.
     */
    public Sounds() {
        loadGunshotSound();
        loadNoAmmoSound();
        loadGunReloadSound();
    }

    /**
     * Gets the URL of the gunshot sound based on the equipped weapon.
     *
     * @return the URL of the gunshot sound.
     */
    public URL getGunshotSound() {
        WeaponData equippedWeapon = PlayerInfo.getInstance().getEquippedWeapon();
        if (equippedWeapon == null) {
            return getClass().getResource("/org/example/moorhuhn/Sounds/deagleGunShot.mp3");
        }

        String weaponName = equippedWeapon.getName();
        switch (weaponName) {
            case "Johny's Shotgun":
                return getClass().getResource("/org/example/moorhuhn/Sounds/shotgunGunShot.mp3");
            case "Inferno Firethrower":
                return getClass().getResource("/org/example/moorhuhn/Sounds/flamethrowerGunShot.mp3");
            case "Supergalactic Obliterator":
                return getClass().getResource("/org/example/moorhuhn/Sounds/obliteratorGunShot.mp3");
            default:
                return getClass().getResource("/org/example/moorhuhn/Sounds/deagleGunShot.mp3");
        }
    }

    /**
     * Gets the URL of the no ammo sound based on the equipped weapon.
     *
     * @return the URL of the no ammo sound.
     */
    public URL getNoAmmoSound() {
        WeaponData equippedWeapon = PlayerInfo.getInstance().getEquippedWeapon();
        if (equippedWeapon == null) {
            return getClass().getResource("/org/example/moorhuhn/Sounds/NoAmmo.wav");
        }

        String weaponName = equippedWeapon.getName();
        switch (weaponName) {
            case "Inferno Firethrower":
                return null;
            case "Supergalactic Obliterator":
                return getClass().getResource("/org/example/moorhuhn/Sounds/obliteratorNoAmmo.wav");
            default:
                return getClass().getResource("/org/example/moorhuhn/Sounds/NoAmmo.wav");
        }
    }

    private Object getReloadURL() {
        WeaponData equippedWeapon = PlayerInfo.getInstance().getEquippedWeapon();
        if (equippedWeapon == null) {
            return getClass().getResource("/org/example/moorhuhn/Sounds/NoAmmo.wav");
        }

        String weaponName = equippedWeapon.getName();
        switch (weaponName) {
            case "Inferno Firethrower":
                return getClass().getResource("/org/example/moorhuhn/Sounds/flamethrowerReload.wav");
            case "Supergalactic Obliterator":
                return getClass().getResource("/org/example/moorhuhn/Sounds/obliteratorReload.wav");
            default:
                return getClass().getResource("/org/example/moorhuhn/Sounds/GunReload.mp3");
        }
    }

    /**
     * Plays the gunshot sound.
     */
    public void playGunshot() {
        if (gunshotPlayer != null) {
            gunshotPlayer.stop();
            gunshotPlayer.seek(Duration.ZERO);
            gunshotPlayer.play();
        }
    }

    /**
     * Plays the no ammo sound.
     */
    public void playNoAmmoSound() {
        if (noAmmoPlayer != null) {
            noAmmoPlayer.stop();
            noAmmoPlayer.seek(Duration.ZERO);
            noAmmoPlayer.play();
        }
    }

    /**
     * Plays the gun reload sound.
     */
    public void playGunReloadSound() {
        if (gunReloadPlayer != null) {
            gunReloadPlayer.stop();
            gunReloadPlayer.seek(Duration.ZERO);
            gunReloadPlayer.play();
        }
    }

    // Private methods below

    private void loadGunshotSound() {
        try {
            Media gunshotMedia = new Media(getGunshotSound().toString());
            gunshotPlayer = new MediaPlayer(gunshotMedia);
            gunshotPlayer.setVolume(0.5);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + e.getMessage());
        }
    }

    private void loadNoAmmoSound() {
        try {
            if (getNoAmmoSound() == null) {
                noAmmoPlayer = null;
                return;
            }
            Media noAmmoMedia = new Media(getNoAmmoSound().toString());
            noAmmoPlayer = new MediaPlayer(noAmmoMedia);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + e.getMessage());
        }
    }

    private void loadGunReloadSound() {
        try {
            Media gunReloadMedia = new Media(getReloadURL().toString());
            gunReloadPlayer = new MediaPlayer(gunReloadMedia);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + e.getMessage());
        }
    }
}
