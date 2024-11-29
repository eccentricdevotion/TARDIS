package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodeldata.keys.LightVariant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class VariableLight {

    private final Material material;
    private final Location location;
    private final Transformation transformation = new Transformation(TARDISConstants.VECTOR_ZERO, TARDISConstants.AXIS_ANGLE_ZERO, new Vector3f(0.99f, 0.99f, 0.99f), TARDISConstants.AXIS_ANGLE_ZERO);

    public VariableLight(Material material, Location location) {
        this.material = material;
        this.location = location;
    }

    public VariableLight(Location location) {
        this.material = null;
        this.location = location;
    }

    public void set() {
        set(LightVariant.OFF.getKey(), 15);
    }

    public void set(NamespacedKey model, int level) {
        World world = location.getWorld();
        ItemDisplay roundel = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
        roundel.getPersistentDataContainer().set(TARDIS.plugin.getTardisIdKey(), PersistentDataType.BOOLEAN, true);
        ItemStack r = new ItemStack(Material.GLASS);
        ItemMeta rim = r.getItemMeta();
        rim.setItemModel(model);
        r.setItemMeta(rim);
        roundel.setItemStack(r);
        ItemDisplay inner = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
        ItemStack b = new ItemStack(material);
        inner.setItemStack(b);
        inner.setTransformation(transformation);
        // set a light block
        Levelled light = TARDISConstants.LIGHT;
        light.setLevel(level);
        location.getBlock().setBlockData(light);
        // also set an interaction entity
        Interaction interaction = (Interaction) world.spawnEntity(location.clone().subtract(0, 0.5d, 0), EntityType.INTERACTION);
        interaction.setResponsive(true);
        interaction.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1001);
        interaction.setPersistent(true);
    }

    public void change(NamespacedKey model, int level) {
        Block block = location.getBlock();
        for (ItemDisplay display : TARDISDisplayItemUtils.getAll(block)) {
            if (display.getPersistentDataContainer().has(TARDIS.plugin.getTardisIdKey(), PersistentDataType.BOOLEAN)) {
                ItemStack roundel = display.getItemStack();
                ItemMeta rim = roundel.getItemMeta();
                rim.setItemModel(model);
                roundel.setItemMeta(rim);
                display.setItemStack(roundel);
            }
        }
        // set a light block
        Levelled light = TARDISConstants.LIGHT;
        light.setLevel(level);
        block.setBlockData(light);
    }
}
