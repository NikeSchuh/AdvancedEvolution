package de.philipp.advancedevolution.achievments;

import de.philipp.advancedevolution.lib.xseries.XSound;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;

public class TestAchievment extends DraconicAchievement {

    public TestAchievment(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onTest(PlayerInteractAtEntityEvent entityEvent) {
        executeAdvancement(entityEvent.getPlayer(), this);
    }

    @Override
    public ChatColor getAchievementColor() {
        return ChatColor.AQUA;
    }

    @Override
    public String getStorageKey() {
        return "test";
    }

    @Override
    public String getAchievementName() {
        return "Is this a test?";
    }

    @Override
    public String getAchievementDescription() {
        return "Yup, it is a test";
    }

    @Override
    public XSound getAchievementSound() {
        return XSound.UI_TOAST_CHALLENGE_COMPLETE;
    }

    @Override
    public float getAchievementSoundPitch() {
        return 1.0f;
    }
}
