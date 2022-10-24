package de.philipp.advancedevolution.items;

import de.philipp.advancedevolution.items.custombases.DraconicArmorItemBase;
import de.philipp.advancedevolution.lib.xseries.XMaterial;
import de.philipp.advancedevolution.shield.Shield;
import de.philipp.advancedevolution.util.item.ArmorType;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArmorHolder {

    private Shield parentShield;

    private DraconicArmorItem helmet;
    private DraconicArmorItem chestPlate;
    private DraconicArmorItem leggings;
    private DraconicArmorItem boots;

    public ArmorHolder(Shield shield) {
        this.parentShield = shield;
    }

    public boolean hasArmor(ArmorType type) {
        if(type == ArmorType.HELMET) {
            return hasHelmet();
        } else if(type == ArmorType.CHESTPLATE) {
            return hasChestPlate();
        } else if(type == ArmorType.LEGGINGS) {
            return hasLeggings();
        } else if(type == ArmorType.BOOTS) {
            return hasBoots();
        } else return false;
    }

    public void setArmor(ArmorType type, DraconicArmorItem armorItem) {
        if(type == ArmorType.HELMET) {
            setHelmet(armorItem);
        } else if (type == ArmorType.CHESTPLATE) {
            setChestPlate(armorItem);
        } else if (type == ArmorType.LEGGINGS) {
            setLeggings(armorItem);
        } else if(type == ArmorType.BOOTS) {
            setBoots(armorItem);
        }
    }

    public DraconicArmorItem getArmor(ArmorType type) {
        if(type == ArmorType.HELMET) {
            return helmet;
        } else if (type == ArmorType.CHESTPLATE) {
           return  chestPlate;
        } else if (type == ArmorType.LEGGINGS) {
            return leggings;
        } else if(type == ArmorType.BOOTS) {
            return boots;
        }
        return null;
    }

    public DraconicArmorItem[] getEquipedArmor() {
        List<DraconicArmorItem> armorItemList = new ArrayList<>();
        if(hasHelmet()) {
                if (helmet.getCurrentStack().getType() == XMaterial.AIR.parseMaterial()) {
                    helmet.setCurrentStack(parentShield.getPlayer().getInventory().getHelmet());
                }
                armorItemList.add(helmet);

        }
        if(hasChestPlate()) {
                if (chestPlate.getCurrentStack().getType() == XMaterial.AIR.parseMaterial()) {
                    chestPlate.setCurrentStack(parentShield.getPlayer().getInventory().getChestplate());
                }
            armorItemList.add(chestPlate);

        }
        if(hasLeggings()) {
                if (leggings.getCurrentStack().getType() == XMaterial.AIR.parseMaterial()) {
                    leggings.setCurrentStack(parentShield.getPlayer().getInventory().getLeggings());
                }
            armorItemList.add(leggings);

        }
        if(hasBoots()) {
                if (boots.getCurrentStack().getType() == XMaterial.AIR.parseMaterial()) {
                    boots.setCurrentStack(parentShield.getPlayer().getInventory().getBoots());
                }
            armorItemList.add(boots);

        }
        return armorItemList.toArray(new DraconicArmorItem[armorItemList.size()]);
    }


    public boolean hasHelmet() {
        return helmet != null;
    }

    public boolean hasChestPlate() {
        return chestPlate != null;
    }

    public boolean hasLeggings() {
        return leggings != null;
    }

    public boolean hasBoots() {
        return  boots != null;
    }

    public void setHelmet(DraconicArmorItem helmet) {
        this.helmet = helmet;
    }

    public void setChestPlate(DraconicArmorItem chestPlate) {
        this.chestPlate = chestPlate;
    }

    public void setLeggings(DraconicArmorItem leggings) {
        this.leggings = leggings;
    }

    public void setBoots(DraconicArmorItem boots) {
        this.boots = boots;
    }

    public DraconicArmorItem getHelmet() {
        return helmet;
    }

    public DraconicArmorItem getChestPlate() {
        return chestPlate;
    }

    public DraconicArmorItem getLeggings() {
        return leggings;
    }

    public DraconicArmorItem getBoots() {
        return boots;
    }

    public IArmorModifier[] getModifiers() {
        List<IArmorModifier> modifiers = new ArrayList<>();
        for(DraconicArmorItem item : getEquipedArmor()) {
            for(IArmorModifier modifier : ((DraconicArmorItemBase) item.getItemBase()).getModifiers()) {
                modifiers.add(modifier);
            }
        }
        return modifiers.toArray(new IArmorModifier[modifiers.size()]);
    }

    public boolean hasModifier(Class<? extends IArmorModifier> clazz) {
        for(IArmorModifier modifier : getModifiers()) {
            if(clazz.isInstance(modifier)) {
                return true;
            }
        }
        return false;
    }

    public IArmorModifier getModifier(Class<? extends IArmorModifier> clazz) {
        for(IArmorModifier modifier : getModifiers()) {
            if(clazz.isInstance(modifier)) {
                return modifier;
            }
        }
        return null;
    }
}
