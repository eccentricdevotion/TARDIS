package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ClassicDoorVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

public class Door {

    public static final HashMap<Material, Door> byMaterial = new HashMap<>();
    public static final HashMap<String, Door> byName = new HashMap<>();
    private final String name;
    private final Material material;
    private final int[] frames;
    private final long frameTick;
    private final boolean extra;
    private final String openSound;
    private final String closeSound;
    private final boolean custom;

    public Door(String name, Material material, int[] frames, long frameTick, boolean extra, String openSound, String closeSound, boolean custom) {
        this.name = name;
        this.material = material;
        this.frames = frames;
        this.frameTick = frameTick;
        this.extra = extra;
        this.openSound = openSound;
        this.closeSound = closeSound;
        this.custom = custom;
    }

    public static NamespacedKey getExtraModel(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            return new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toDashedLowercase(door.getName()) + "_extra");
        }
        return null;
    }

    public static NamespacedKey getOpenModel(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            return new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toDashedLowercase(door.getName()) + "_open");
        }
        return null;
    }

    public static NamespacedKey getClosedModel(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            return new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toDashedLowercase(door.getName()) + "_closed");
        }
        return null;
    }

    public static DoorAnimationData getOpenData(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            // get animation
            NamespacedKey[] animation = new NamespacedKey[door.frames.length + 1];
            for (int i : door.frames) {
                animation[i] = new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toUnderscoredLowercase(door.getName()) + "_" + i);
            }
            animation[door.frames.length] = new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toUnderscoredLowercase(door.getName()) + "_open");
            return new DoorAnimationData(door.frameTick, door.openSound, animation);
        } else {
            // default for classic door
            return new DoorAnimationData(4L, "tardis_door_open", new NamespacedKey[]{ClassicDoorVariant.CLASSIC_DOOR_OPEN.getKey()});
        }
    }

    public static DoorAnimationData getCloseData(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            // get animation
            NamespacedKey[] animation = new NamespacedKey[door.frames.length + 1];
            int[] reversed = ArrayUtils.clone(door.frames);
            ArrayUtils.reverse(reversed);
            int i = 0;
            for (int r : reversed) {
                animation[i] = new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toUnderscoredLowercase(door.getName()) + "_" + r);
                i++;
            }
            animation[door.frames.length] = new NamespacedKey(TARDIS.plugin, TARDISStringUtils.toUnderscoredLowercase(door.getName()) + "_closed");
            return new DoorAnimationData(door.frameTick, door.closeSound, animation);
        } else {
            // default for classic door
            return new DoorAnimationData(4L, "tardis_door_close", new NamespacedKey[]{ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey()});
        }
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int[] getFrames() {
        return frames;
    }

    public boolean hasExtra() {
        return extra;
    }

    public boolean isCustom() {
        return custom;
    }
}
