package de.philipp.advancedevolution.nms;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkyUtils {

    public static final List<String> FREEZE_UNSUPPORTED = (List<String>) Stream.<String>of(new String[] { "1.8", "1.13" }).collect(Collectors.toList());

    public static boolean changeSky(Player p, float number) {
        return changeSky(p, ChangeType.RAIN, number);
    }

    public static boolean changeSky(Player p, ChangeType packet, float number) {
        try {
            int major = ReflectionUtil.getMajor(), minor = ReflectionUtil.getMinor();
            Object payload = null;
            if (major == 1)
                if (minor < 16) {
                    payload = createPacket_18_to_115(packet.getValue(), number);
                } else {
                    payload = createPacket_116_plus(packet.getValue(), number);
                }
            if (payload != null) {
                deliverPacket(payload, p);
                return true;
            }
            return false;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public boolean freeze(Player p) {
        return sendFreezePacket(p);
    }

    public boolean unfreeze(Player p) {
        return p.teleport(p.getLocation());
    }

    protected static Object getConnection(Player player) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> ocbPlayer = ReflectionUtil.getOCBClass("entity.CraftPlayer");
        Method getHandle = ReflectionUtil.getMethod(ocbPlayer, "getHandle", new Class[0]);
        Object nmsPlayer = ((Method)Objects.<Method>requireNonNull(getHandle)).invoke(player, new Object[0]);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        return conField.get(nmsPlayer);
    }

    protected static void deliverPacket(Object packet, Player player) throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        Method sendPacket = ReflectionUtil.getNMSClass("PlayerConnection").getMethod("sendPacket", new Class[] { ReflectionUtil.getNMSClass("Packet") });
        sendPacket.invoke(getConnection(player), new Object[] { packet });
    }

    protected static Object createPacket_18_to_115(int packetNum, float number) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> ClientboundGameEventPacket = ReflectionUtil.getNMSClass("PacketPlayOutGameStateChange");
        Constructor<?> packetConstructor = ClientboundGameEventPacket.getConstructor(new Class[] { int.class, float.class });
        return packetConstructor.newInstance(new Object[] { Integer.valueOf(packetNum), Float.valueOf(number) });
    }

    public static Object createPacket_116_plus(int packetNum, float number) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> ClientboundGameEventPacket = ReflectionUtil.getNMSClass("PacketPlayOutGameStateChange");
        Class<?> packetTypeClass = ReflectionUtil.getDeclaredClass(ClientboundGameEventPacket, "a");
        Constructor<?> packetConstructor = ClientboundGameEventPacket.getConstructor(new Class[] { packetTypeClass, float.class });
        Constructor<?> packetTypeConstructor = ((Class)Objects.<Class<?>>requireNonNull(packetTypeClass)).getConstructor(new Class[] { int.class });
        Object packetType = packetTypeConstructor.newInstance(new Object[] { Integer.valueOf(packetNum) });
        return packetConstructor.newInstance(new Object[] { packetType, Float.valueOf(number) });
    }

    private Object getTypeKey(Class<?> WorldClass, Object world) throws InvocationTargetException, IllegalAccessException {
        Method getTypeKey = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(WorldClass, "getTypeKey", new Class[0]));
        return getTypeKey.invoke(world, new Object[0]);
    }

    private Object getDimensionManager1162Plus(Class<?> WorldClass, Object world) throws InvocationTargetException, IllegalAccessException {
        Method getDimensionManager = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(WorldClass, "getDimensionManager", new Class[0]));
        return getDimensionManager.invoke(world, new Object[0]);
    }

    private Object getDimensionKey(Class<?> WorldClass, Object world) throws InvocationTargetException, IllegalAccessException {
        Method getDimensionKey = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(WorldClass, "getDimensionKey", new Class[0]));
        return getDimensionKey.invoke(world, new Object[0]);
    }

    private Object getWorldServer(Player player) throws InvocationTargetException, IllegalAccessException {
        Class<?> craftWorldClass = ReflectionUtil.getOCBClass("CraftWorld");
        Method getHandle = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(craftWorldClass, "getHandle", new Class[0]));
        return getHandle.invoke(player.getWorld(), new Object[0]);
    }

    private Object getDimensionManager(Object worldServer) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class<?> worldProviderClass = ReflectionUtil.getNMSClass("WorldProvider");
        Class<?> worldClass = ReflectionUtil.getNMSClass("World");
        Field worldProviderField = worldClass.getDeclaredField("worldProvider");
        Object worldProvider = worldProviderField.get(worldServer);
        Method getDimensionManager = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(worldProviderClass, "getDimensionManager", new Class[0]));
        return getDimensionManager.invoke(worldProvider, new Object[0]);
    }

    private Object getWorldType(Object worldServer) throws InvocationTargetException, IllegalAccessException {
        Class<?> WorldServerClass = ReflectionUtil.getNMSClass("WorldServer");
        Method getWorldData = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(WorldServerClass, "getWorldData", new Class[0]));
        Object worldData = getWorldData.invoke(worldServer, new Object[0]);
        Class<?> worldDataClass = ReflectionUtil.getNMSClass("WorldData");
        Method getType = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(worldDataClass, "getType", new Class[0]));
        return getType.invoke(worldData, new Object[0]);
    }

    private int getWorldEnvironmentId(Player player) throws InvocationTargetException, IllegalAccessException {
        Method getId = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(World.Environment.class, "getId", new Class[0]));
        return ((Integer)getId.invoke(player.getWorld().getEnvironment(), new Object[0])).intValue();
    }

    private int getGameModeValue(Player player) throws InvocationTargetException, IllegalAccessException {
        Method deprecatedGetValue = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(GameMode.class, "getValue", new Class[0]));
        return ((Integer)deprecatedGetValue.invoke(player.getGameMode(), new Object[0])).intValue();
    }

    private Object getEnumGamemode(Class<?> EnumGamemodeClass, Player player) throws InvocationTargetException, IllegalAccessException {
        Method gmGetById = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(EnumGamemodeClass, "getById", new Class[] { int.class }));
        return gmGetById.invoke(null, new Object[] { Integer.valueOf(getGameModeValue(player)) });
    }

    private Object getEnumDifficulty(Class<?> EnumDifficultyClass, Player player) throws InvocationTargetException, IllegalAccessException {
        Method diffGetById = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(EnumDifficultyClass, "getById", new Class[] { int.class }));
        Method deprecatedGetValue = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(Difficulty.class, "getValue", new Class[0]));
        return diffGetById.invoke(null, new Object[] { deprecatedGetValue.invoke(player.getWorld().getDifficulty(), new Object[0]) });
    }

    protected boolean sendFreezePacket(Player player) {
        int major = ReflectionUtil.getMajor(), minor = ReflectionUtil.getMinor(), r = ReflectionUtil.getR();
        if (FREEZE_UNSUPPORTED.contains(major + "." + minor)) return false;
        Class<?> ClientboundRespawnPacket = ReflectionUtil.getNMSClass("PacketPlayOutRespawn");
        try {
            Object packet;
            if (major == 1) {
                if (minor >= 16) {
                    Class<?> EnumGamemodeClass = ReflectionUtil.getNMSClass("EnumGamemode");
                    Object worldServer = getWorldServer(player);
                    Object gameMode = getEnumGamemode(EnumGamemodeClass, player);
                    Class<?> WorldClass = ReflectionUtil.getNMSClass("World");
                    Class<?> ResourceKeyClass = ReflectionUtil.getNMSClass("ResourceKey");
                    if (r >= 2) {
                        Class<?> DimensionManagerClass = ReflectionUtil.getNMSClass("DimensionManager");
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { DimensionManagerClass, ResourceKeyClass, long.class, EnumGamemodeClass, EnumGamemodeClass, boolean.class, boolean.class, boolean.class });
                        packet = packetConstructor.newInstance(new Object[] { getDimensionManager1162Plus(WorldClass, worldServer),
                                getDimensionKey(WorldClass, worldServer),
                                Long.valueOf(player.getWorld().getSeed()), gameMode, gameMode,

                                Boolean.valueOf(false),
                                Boolean.valueOf(false),
                                Boolean.valueOf(true) });
                    } else {
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { ResourceKeyClass, ResourceKeyClass, long.class, EnumGamemodeClass, EnumGamemodeClass, boolean.class, boolean.class, boolean.class });
                        packet = packetConstructor.newInstance(new Object[] { getTypeKey(WorldClass, worldServer),
                                getDimensionKey(WorldClass, worldServer),
                                Long.valueOf(player.getWorld().getSeed()), gameMode, gameMode,

                                Boolean.valueOf(false),
                                Boolean.valueOf(false),
                                Boolean.valueOf(true) });
                    }
                } else if (minor >= 13) {
                    Class<?> EnumGamemodeClass = ReflectionUtil.getNMSClass("EnumGamemode");
                    Object worldServer = getWorldServer(player);
                    Class<?> DimensionManagerClass = ReflectionUtil.getNMSClass("DimensionManager");
                    Class<?> WorldTypeClass = ReflectionUtil.getNMSClass("WorldType");
                    if (minor == 15) {
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { DimensionManagerClass, long.class, WorldTypeClass, EnumGamemodeClass });
                        packet = packetConstructor.newInstance(new Object[] { getDimensionManager(worldServer),
                                Long.valueOf(player.getWorld().getSeed()),
                                getWorldType(worldServer),
                                getEnumGamemode(EnumGamemodeClass, player) });
                    } else if (minor == 14) {
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { DimensionManagerClass, WorldTypeClass, EnumGamemodeClass });
                        packet = packetConstructor.newInstance(new Object[] { getDimensionManager(worldServer),
                                getWorldType(worldServer),
                                getEnumGamemode(EnumGamemodeClass, player) });
                    } else {
                        Class<?> EnumDifficultyClass = ReflectionUtil.getNMSClass("EnumDifficulty");
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { DimensionManagerClass, EnumDifficultyClass, WorldTypeClass, EnumGamemodeClass });
                        packet = packetConstructor.newInstance(new Object[] { getDimensionManager(worldServer),
                                getEnumDifficulty(EnumDifficultyClass, player),
                                getWorldType(worldServer),
                                getEnumGamemode(EnumGamemodeClass, player) });
                    }
                } else {
                    Class<?> EnumDifficultyClass = ReflectionUtil.getNMSClass("EnumDifficulty");
                    Class<?> WorldTypeClass = ReflectionUtil.getNMSClass("WorldType");
                    Object WorldType_NORMAL = WorldTypeClass.getField("NORMAL").get(null);
                    if (minor >= 10) {
                        Class<?> EnumGamemodeClass = ReflectionUtil.getNMSClass("EnumGamemode");
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { int.class, EnumDifficultyClass, WorldTypeClass, EnumGamemodeClass });
                        packet = packetConstructor.newInstance(new Object[] { Integer.valueOf(getWorldEnvironmentId(player)),
                                getEnumDifficulty(EnumDifficultyClass, player), WorldType_NORMAL,

                                getEnumGamemode(EnumGamemodeClass, player) });
                    } else {
                        Class<?> WorldSettingsClass = ReflectionUtil.getNMSClass("WorldSettings");
                        Class<?> EnumGamemodeClass_Declared = ReflectionUtil.getDeclaredClass(WorldSettingsClass, "EnumGamemode");
                        Method getById = Objects.<Method>requireNonNull(ReflectionUtil.getMethod(EnumGamemodeClass_Declared, "getById", new Class[] { int.class }));
                        Constructor<?> packetConstructor = ClientboundRespawnPacket.getConstructor(new Class[] { int.class, EnumDifficultyClass, WorldTypeClass, EnumGamemodeClass_Declared });
                        packet = packetConstructor.newInstance(new Object[] { Integer.valueOf(getWorldEnvironmentId(player)),
                                getEnumDifficulty(EnumDifficultyClass, player), WorldType_NORMAL, getById

                                .invoke(null, new Object[] { Integer.valueOf(getGameModeValue(player)) }) });
                    }
                }
            } else {
                return false;
            }
            deliverPacket(packet, player);
            player.updateInventory();
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public enum ChangeType {
        RAIN(7),
        THUNDER_(8);

        private final int value;

        ChangeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}