package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.List;

public class SonicUpgradeData {

    public static final HashMap<String, String> upgrades = new HashMap<>();
    public static final HashMap<NamespacedKey, String> customModelData = new HashMap<>();
    public static final List<String> sonicKeys = List.of(
            "Bio-scanner Upgrade",
            "Diamond Upgrade",
            "Emerald Upgrade",
            "Redstone Upgrade",
            "Painter Upgrade",
            "Ignite Upgrade",
            "Pickup Arrows Upgrade",
            "Knockback Upgrade",
            "Brush Upgrade",
            "Conversion Upgrade");

    static {
        upgrades.put("Admin Upgrade", "admin");
        upgrades.put("Bio-scanner Upgrade", "bio");
        upgrades.put("Redstone Upgrade", "redstone");
        upgrades.put("Diamond Upgrade", "diamond");
        upgrades.put("Emerald Upgrade", "emerald");
        upgrades.put("Painter Upgrade", "painter");
        upgrades.put("Ignite Upgrade", "ignite");
        upgrades.put("Pickup Arrows Upgrade", "arrow");
        upgrades.put("Knockback Upgrade", "knockback");
        upgrades.put("Brush Upgrade", "brush");
        upgrades.put("Conversion Upgrade", "conversion");
        customModelData.put(CircuitVariant.ADMIN.getKey(), "Admin Upgrade");
        customModelData.put(CircuitVariant.BIO.getKey(), "Bio-scanner Upgrade");
        customModelData.put(CircuitVariant.REDSTONE.getKey(), "Redstone Upgrade");
        customModelData.put(CircuitVariant.DIAMOND.getKey(), "Diamond Upgrade");
        customModelData.put(CircuitVariant.EMERALD.getKey(), "Emerald Upgrade");
        customModelData.put(CircuitVariant.PAINTER.getKey(), "Painter Upgrade");
        customModelData.put(CircuitVariant.IGNITE.getKey(), "Ignite Upgrade");
        customModelData.put(CircuitVariant.PICKUP.getKey(), "Pickup Arrows Upgrade");
        customModelData.put(CircuitVariant.KNOCKBACK.getKey(), "Knockback Upgrade");
        customModelData.put(CircuitVariant.BRUSH.getKey(), "Brush Upgrade");
        customModelData.put(CircuitVariant.CONVERSION.getKey(), "Conversion Upgrade");
    }
}
