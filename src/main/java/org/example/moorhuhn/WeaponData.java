package org.example.moorhuhn;

/**
 * Represents the data for a weapon in the game, including its name, cost, status, description, ammo capacity, and reload time.
 */
public class WeaponData {
    private String name;
    private boolean isBought;
    private boolean isEquipped;
    private int cost;
    private String description;
    private int maxAmmo;
    private double reloadTime;

    /**
     * Constructs a WeaponData object with the specified properties.
     *
     * @param name the name of the weapon.
     * @param cost the cost of the weapon.
     * @param isBought whether the weapon has been bought.
     * @param isEquipped whether the weapon is currently equipped.
     * @param description a description of the weapon.
     * @param maxAmmo the maximum ammunition capacity of the weapon.
     * @param reloadTime the time it takes to reload the weapon.
     */
    public WeaponData(String name, int cost, boolean isBought, boolean isEquipped,
                      String description, int maxAmmo, double reloadTime) {
        this.name = name;
        this.isBought = isBought;
        this.isEquipped = isEquipped;
        this.cost = cost;
        this.description = description;
        this.maxAmmo = maxAmmo;
        this.reloadTime = reloadTime;
    }

    /**
     * Gets the name of the weapon.
     *
     * @return the name of the weapon.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the weapon has been bought.
     *
     * @return true if the weapon has been bought, false otherwise.
     */
    public boolean isBought() {
        return isBought;
    }

    /**
     * Checks if the weapon is currently equipped.
     *
     * @return true if the weapon is equipped, false otherwise.
     */
    public boolean isEquipped() {
        return isEquipped;
    }

    /**
     * Gets the cost of the weapon.
     *
     * @return the cost of the weapon.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the maximum ammunition capacity of the weapon.
     *
     * @return the maximum ammunition capacity of the weapon.
     */
    public int getMaxAmmo() {
        return maxAmmo;
    }

    /**
     * Gets the reload time of the weapon.
     *
     * @return the reload time of the weapon.
     */
    public double getReloadTime() {
        return reloadTime;
    }

    /**
     * Sets the bought status of the weapon.
     *
     * @param bought true if the weapon has been bought, false otherwise.
     */
    public void setBought(boolean bought) {
        isBought = bought;
    }

    /**
     * Sets the equipped status of the weapon.
     *
     * @param equipped true if the weapon is equipped, false otherwise.
     */
    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }

    /**
     * Sets the description of the weapon.
     *
     * @param description the new description of the weapon.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the weapon.
     *
     * @return the description of the weapon.
     */
    public String getDescription() {
        return description;
    }
}
