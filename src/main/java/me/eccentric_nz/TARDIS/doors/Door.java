package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

import java.util.HashMap;

public class Door {

    public static final HashMap<Material, Door> byMaterial = new HashMap<>();
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

    public static Door getByMaterial(Material material) {
        return byMaterial.get(material);
    }

    public static int getCMD(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            return 10000 + door.getFrames().length;
        } else {
            TARDIS.plugin.debug("getCMD material = " + material);
        }
        return -1;
    }

    public static DoorAnimationData getOpenData(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            return new DoorAnimationData(door.frameTick, door.openSound);
        } else {
            TARDIS.plugin.debug("getOpenData material = " + material);
        }
        return null;
    }

    public static DoorAnimationData getCloseData(Material material) {
        Door door = byMaterial.get(material);
        if (door != null) {
            return new DoorAnimationData(door.frameTick, door.closeSound);
        } else {
            TARDIS.plugin.debug("getCloseData material = " + material);
        }
        return null;
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

    public boolean isExtra() {
        return extra;
    }

    public boolean isCustom() {
        return custom;
    }
}
