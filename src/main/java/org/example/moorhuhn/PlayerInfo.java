package org.example.moorhuhn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages player information including the nickname and weapons inventory.
 */
public class PlayerInfo {
    private static final String WEAPON_FILE = "weaponData.txt";
    private static PlayerInfo instance;
    private String nickname;
    private List<WeaponData> weaponsInventory;

    /**
     * Private constructor to initialize the weapons inventory and load weapons from the file.
     */
    private PlayerInfo() {
        weaponsInventory = new ArrayList<>();
        loadWeapons();
    }

    /**
     * Gets the singleton instance of the PlayerInfo.
     *
     * @return the singleton instance of PlayerInfo.
     */
    public static PlayerInfo getInstance() {
        if (instance == null) {
            instance = new PlayerInfo();
        }
        return instance;
    }

    /**
     * Gets the nickname of the player.
     *
     * @return the player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the player.
     *
     * @param nickname the player's nickname.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the list of weapons in the player's inventory.
     *
     * @return the list of weapons.
     */
    public List<WeaponData> getWeaponsInventory() {
        return weaponsInventory;
    }

    /**
     * Gets the currently equipped weapon from the player's inventory.
     *
     * @return the equipped weapon, or null if no weapon is equipped.
     */
    public WeaponData getEquippedWeapon() {
        return weaponsInventory.stream().filter(WeaponData::isEquipped).findFirst().orElse(null);
    }

    /**
     * Updates the weapons file with the current state of the weapons inventory.
     */
    public void updateWeaponsFile() {
        Path path = Paths.get(WEAPON_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write("Name:IsBought:IsEquipped:Cost:Description:MaxAmmo:ReloadTime\n"); // Write the header
            for (WeaponData weapon : weaponsInventory) {
                String line = String.format("%s:%b:%b:%d:%s:%d:%f\n",
                        weapon.getName(), weapon.isBought(), weapon.isEquipped(),
                        weapon.getCost(), weapon.getDescription(),
                        weapon.getMaxAmmo(), weapon.getReloadTime());
                bw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Private methods below

    private void loadWeapons() {
        Path path = Paths.get(WEAPON_FILE);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(":");
                if (data.length == 7) {
                    WeaponData weapon = new WeaponData(
                            data[0], // Name
                            Integer.parseInt(data[3]), // Cost
                            Boolean.parseBoolean(data[1]), // IsBought
                            Boolean.parseBoolean(data[2]), // IsEquipped
                            data[4], // Description
                            Integer.parseInt(data[5]), // MaxAmmo
                            Double.parseDouble(data[6]) // ReloadTime
                    );
                    weaponsInventory.add(weapon);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
