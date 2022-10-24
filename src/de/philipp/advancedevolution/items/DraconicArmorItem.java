package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.StorageKeys;
import de.philipp.advancedevolution.util.item.ArmorType;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class DraconicArmorItem extends DraconicEnergyItem {

    protected double shieldCapacity;
    protected float shieldRegeneration;
    protected DraconicArmorType draconicArmorType;
    protected ArmorType type;

    public DraconicArmorItem(ItemStack stack) {
        super(stack);
        if(super.isArmorItem()) {
            this.draconicArmorType = DraconicArmorType.valueOf(data.getString(StorageKeys.SHIELD_TYPE.getNBTKey()));
            this.shieldCapacity = data.getDouble(StorageKeys.SHIELD_CAPACITY.getNBTKey());
            this.shieldRegeneration = data.getFloat(StorageKeys.SHIELD_REGENERATION.getNBTKey());
            this.type = ArmorType.matchType(stack);
        } else {
            throw new IllegalArgumentException("Cannot cast an NPE to an DraconicArmorItem");
        }
    }

    public ArmorType getType() {
        return type;
    }

    public DraconicArmorType getDraconicArmorType() {
        return draconicArmorType;
    }

    public double getShieldCapacity() {
        return shieldCapacity;
    }


    public double getShieldRegeneration() {
        return shieldRegeneration;
    }


    public void updateItem(ItemStack stack) {
        super.updateEnergy();
        NBTItem item = new NBTItem(getCurrentStack(), true);
        item.setDouble(StorageKeys.SHIELD_CAPACITY.getNBTKey(), shieldCapacity);
        item.setFloat(StorageKeys.SHIELD_REGENERATION.getNBTKey(), shieldRegeneration);
    }

    public void setShieldCapacity(double shieldCapacity) {
        this.shieldCapacity = shieldCapacity;
    }


    public void setShieldRegeneration(float shieldRegeneration) {
        this.shieldRegeneration = shieldRegeneration;
    }


}
