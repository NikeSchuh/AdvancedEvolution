package de.philipp.advancedevolution.blocks;

import de.philipp.advancedevolution.blocks.custombases.DraconicMachineBase;
import de.tr7zw.nbtapi.NBTCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CrystalLinkManager {

    private DraconicMachine draconicMachine;
    private int maxLinks;
    private NBTCompound blockData;
    private NBTCompound crystalData;
    private NBTCompound crystalLinks;

    public CrystalLinkManager(DraconicMachine machine) {
        this.draconicMachine = machine;
        this.blockData = machine.getStoredData();
        if(blockData.hasKey("crystalData")) {
            crystalData = blockData.getCompound("crystalData");
            crystalLinks = crystalData.getCompound("crystalLinks");
            maxLinks = crystalData.getInteger("maxLinks");
        }
    }

    public boolean hasCrystalData() {
        return crystalData != null;
    }

    public void createCrystalData(int maxLinks) {
        if(!hasCrystalData()) {
            this.crystalData = blockData.addCompound("crystalData");
            this.crystalLinks = crystalData.addCompound("crystalLinks");
            crystalData.setInteger("maxLinks", maxLinks);
            this.maxLinks = maxLinks;
        }
    }

    public Block[] getLinks() {
        if(getCurrentLinks() == 0) {
            return new Block[0];
        }
        List<Block> blocks = new ArrayList<>();
        for(String s : crystalLinks.getKeys()) {
            String[] loc = crystalLinks.getString(s).split(" ");
            Location location = new Location(draconicMachine.getBlock().getWorld(), Double.valueOf(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]));
            blocks.add(location.getBlock());
        }
        return blocks.toArray(new Block[blocks.size()]);
    }

    public void addLink(DraconicMachine draconicMachine) {
        if(acceptsLink()) {
            Location loc = draconicMachine.getBlock().getLocation();
            crystalLinks.setString(UUID.randomUUID().toString(), loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
        }
    }

    public void validateLinks() {
        if(getCurrentLinks() == 0) {
            return;
        }
        Block[] links = getLinks();
        for(String uid : crystalLinks.getKeys()) {
            UUID uuid = UUID.fromString(uid);
            String[] loc = crystalLinks.getString(uid).split(" ");
            Location location = new Location(draconicMachine.getBlock().getWorld(), Double.valueOf(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]));
            Block block = location.getBlock();
            if(block.getLocation().distance(draconicMachine.getBlock().getLocation()) > 127) {
                removeLink(uuid);
                continue;
            }
            if(DraconicBlock.isDraconicBlock(block)) {
                DraconicBlock draconicBlock = new DraconicBlock(block);
                if(draconicBlock.getBlockBase() instanceof DraconicMachineBase) {
                    DraconicMachine draconicMachine = new DraconicMachine(block);
                    if(draconicMachine.isEnergyAcceptable()) {
                        continue;
                    } else {
                        removeLink(uuid);
                    }
                } else {
                    removeLink(uuid);
                }
            } else {
                removeLink(uuid);
            }
        }
    }

    public boolean acceptsLink() {
        return getCurrentLinks() + 1 <= getMaxLinks();
    }

    public int getMaxLinks() {
        return maxLinks;
    }

    public boolean contains(Block block) {
        return Arrays.asList(getLinks()).contains(block);
    }

    public void removeLink(UUID uuid) {
        crystalLinks.removeKey(uuid + "");
    }

    public UUID getLinkUniqueId(Block block) {
        for(String uid : crystalLinks.getKeys()) {
            String[] loc = crystalLinks.getString(uid).split(" ");
            Location location = new Location(draconicMachine.getBlock().getWorld(), Double.valueOf(loc[0]), Double.valueOf(loc[1]), Double.valueOf(loc[2]));
            Block block1 = location.getBlock();
            if(block1.equals(block)) {
                return UUID.fromString(uid);
            }
        }
        return null;
    }

    public int getCurrentLinks() {
        try{return crystalLinks.getKeys().size();} catch (Exception e) {
            return 0;
        }
    }

    public DraconicMachine[] getMachineLinks() {
        List<DraconicMachine> draconicMachines = new ArrayList<>();
        for(Block block : getLinks()) {
            draconicMachines.add(new DraconicMachine(block));
        }
        return draconicMachines.toArray(new DraconicMachine[draconicMachines.size()]);
    }

}
