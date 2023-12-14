package me.eccentric_nz.TARDIS.sonic;

import java.util.HashMap;

public class SonicUpgradeData {

    public static final HashMap<String, String> upgrades = new HashMap<>();
    public static final HashMap<Integer, String> customModelData = new HashMap<>();

    static {
        upgrades.put("Admin Upgrade", "admin");
        upgrades.put("Bio-scanner Upgrade", "bio");
        upgrades.put("Redstone Upgrade", "redstone");
        upgrades.put("Diamond Upgrade", "diamond");
        upgrades.put("Emerald Upgrade", "emerald");
        upgrades.put("Painter Upgrade", "paint");
        upgrades.put("Ignite Upgrade", "ignite");
        upgrades.put("Pickup Arrows Upgrade", "arrow");
        upgrades.put("Knockback Upgrade", "knockback");
        upgrades.put("Brush Upgrade", "brush");
        upgrades.put("Conversion Upgrade", "conversion");
        customModelData.put(10001968, "Admin Upgrade");
        customModelData.put(10001969, "Bio-scanner Upgrade");
        customModelData.put(10001970, "Redstone Upgrade");
        customModelData.put(10001971, "Diamond Upgrade");
        customModelData.put(10001972, "Emerald Upgrade");
        customModelData.put(10001979, "Painter Upgrade");
        customModelData.put(10001982, "Ignite Upgrade");
        customModelData.put(10001984, "Pickup Arrows Upgrade");
        customModelData.put(10001986, "Knockback Upgrade");
        customModelData.put(10001987, "Brush Upgrade");
        customModelData.put(10001988, "Conversion Upgrade");
    }
}
