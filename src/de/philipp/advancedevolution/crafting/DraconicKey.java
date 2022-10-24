package de.philipp.advancedevolution.crafting;

import org.bukkit.NamespacedKey;

public class DraconicKey {

    public static final String Plugin_key = "draconic";
    private NamespacedKey key;
    private String minecraft;

    public DraconicKey(String minecraft) {
        this.minecraft = minecraft;
        this.key = NamespacedKey.minecraft(Plugin_key + minecraft.toLowerCase());
    }

    public NamespacedKey getKey() {
        return key;
    }
}
