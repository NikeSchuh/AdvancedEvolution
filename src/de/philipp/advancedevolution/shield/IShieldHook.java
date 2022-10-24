package de.philipp.advancedevolution.shield;

import org.bukkit.plugin.Plugin;

public interface IShieldHook  {

    Plugin getHandle();
    void run(Shield shield);


}
