package de.philipp.advancedevolution.items;

import org.bukkit.Bukkit;

public enum DraconicArmorType {

    BASIC(0, false, false),
    WYVERN(1, false, false),
    DRACONIC(2, true, true),
    CHAOTIC(3,true, true)
    , GODLY(4, true, true);

    private boolean flight;
    private boolean revivable;
    private int upgradeLevel;

    DraconicArmorType(int upgradeLevel, boolean flight, boolean revivable) {
        this.flight = flight;
        this.revivable = revivable;
        this.upgradeLevel = upgradeLevel;
    }

    public static DraconicArmorType matchType(int upgradeLevel) {
        for(DraconicArmorType type : DraconicArmorType.values()) {
            if(type.getUpgradeLevel() == upgradeLevel) {
                return type;
            }
        }
        return null;
    }

    public static boolean isTierHigher(DraconicArmorType type2, DraconicArmorType type1) {
        if(type1 == null && type2 == null) return false;
        if(type1 == null) return true;
        if(type2 == null) return false;
        return type1.getUpgradeLevel() < type2.getUpgradeLevel();
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public boolean canFlight() {
        return flight;
    }

    public boolean isRevivable() {
        return revivable;
    }

    public boolean isDraconic() {
        return this == DraconicArmorType.DRACONIC;
    }

    public boolean isBasic() {
        return this == DraconicArmorType.BASIC;
    }

    public boolean isWyvern() {
        return this == DraconicArmorType.WYVERN;
    }

    public boolean isChaotic() {
        return this == DraconicArmorType.CHAOTIC;
    }

    public boolean isGodly() {
        return this == DraconicArmorType.GODLY;
    }
}
