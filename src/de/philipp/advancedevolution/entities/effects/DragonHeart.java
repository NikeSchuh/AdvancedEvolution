package de.philipp.advancedevolution.entities.effects;

import de.philipp.advancedevolution.entities.DraconicEntity;
import de.philipp.advancedevolution.items.DraconicItem;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

public class DragonHeart extends DraconicEntity {

    private Item item;

    public DragonHeart(Location location) {
        super(location, EntityType.DROPPED_ITEM);
        item = location.getWorld().dropItem(location, DraconicItem.instantiateItem("DragonHeart").getCurrentStack());
        item.setGravity(false);
        item.setGlowing(true);
    }

}
