package de.philipp.advancedevolution.util;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class BadEffects {

    public static Collection<PotionEffectType> BAD_EFFECTS = new ArrayList<>();

    public BadEffects() {
        BAD_EFFECTS.add(PotionEffectType.WITHER);
        BAD_EFFECTS.add(PotionEffectType.POISON);
        BAD_EFFECTS.add(PotionEffectType.BLINDNESS);
        BAD_EFFECTS.add(PotionEffectType.CONFUSION);
        BAD_EFFECTS.add(PotionEffectType.HUNGER);
        BAD_EFFECTS.add(PotionEffectType.HARM);
        BAD_EFFECTS.add(PotionEffectType.WEAKNESS);
        BAD_EFFECTS.add(PotionEffectType.UNLUCK);
    }


    public  boolean isBadEffect(PotionEffectType type) {
        return BAD_EFFECTS.contains(type);
    }
}

