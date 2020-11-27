package de.philipp.advancedevolution;

public enum StorageKeys {

    ENERGY_STORED("energyStored"),
    ENERGY_CAPACITY("energyCapacity"),
    OWNING_PLAYER("owningPlayer"),
    ACCESS_PRIVATE("accessPrivate"),
    PLUGIN("advancedEvolution"),
    REGISTRY_NAME("dataName"),
    SHIELD_TYPE("shieldType"),
    SHIELD_REGENERATION("shieldRegeneration"),
    SHIELD_CAPACITY("shieldCapacity"),
    UNIQUE_ITEM_ID("uniqueItemId");



    private String nbtkey;
    StorageKeys(String nbtkey) {
        this.nbtkey = nbtkey;
    }

    public String getNBTKey() {
        return nbtkey;
    }

}
