package de.philipp.advancedevolution.structures;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;

import java.util.HashMap;
import java.util.Map;

public class NBTUtil {

    public Map<String, Object> serialize(NBTCompound nbtCompound) {
        Map<String, Object> map = new HashMap<>();
        for(String key : nbtCompound.getKeys()) {
            map.put(key, nbtCompound.getObject(key, Object.class));
        }
        return map;
    }

}
