package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class ConsoleBuilder {

    private final TARDIS plugin;

    public ConsoleBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void create(Block block, int type, int id) {
        Block up = block.getRelative(BlockFace.UP);
        for (int i = 0; i < 6; i++) {
            ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = shard.getItemMeta();
            im.setCustomModelData((1000 * type) + (i + 1));
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, i);
            shard.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(shard);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            display.setPersistent(true);
            display.setInvulnerable(true);
            float yaw = i * 60.0f;
            yaw = Location.normalizeYaw(yaw);
            // set display rotation
            display.setRotation(yaw, 0);
        }
        for (int i = 30; i < 360; i += 60) {
            ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = shard.getItemMeta();
            im.setCustomModelData((1000 * type) + 7);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, i);
            shard.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(shard);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            display.setPersistent(true);
            display.setInvulnerable(true);
            float yaw = Location.normalizeYaw(i);
            // set display rotation
            display.setRotation(yaw, 0);
        }
        // set interaction entities for console controls
        for (ConsoleInteraction i : ConsoleInteraction.values()) {
            UUID uuid = null;
            if (i.hasModel()) {
                // spawn a display entity and save it's UUID to the interaction entity
                uuid = spawnControl(i, block.getLocation());
            }
            double x = i.getRelativePosition().getX();
            double z = i.getRelativePosition().getZ();
            Location location = block.getLocation().clone().add(x, 1, z);
            Interaction interaction = (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);
            interaction.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID(), interaction.getUniqueId());
            if (i == ConsoleInteraction.THROTTLE) {
                interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.INTEGER, -1);
            }
            if (i == ConsoleInteraction.EXTERIOR_LAMP_LEVEL_SWITCH || i == ConsoleInteraction.INTERIOR_LIGHT_LEVEL_SWITCH) {
                interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.INTEGER, 1);
                // add a control record
                int cid = (i == ConsoleInteraction.EXTERIOR_LAMP_LEVEL_SWITCH) ? 49 : 50;
                plugin.getQueryFactory().insertControl(id, cid, location.toString(), 0);
            }
            if (uuid != null) {
                interaction.getPersistentDataContainer().set(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
            }
            interaction.setInteractionWidth(i.getWidth());
            interaction.setInteractionHeight(i.getHeight());
            interaction.setPersistent(true);
            interaction.setInvulnerable(true);
            HashMap<String, Object> data = new HashMap<>();
            data.put("tardis_id", id);
            data.put("uuid", interaction.getUniqueId());
            data.put("control", i.toString());
            data.put("state", i.getDefaultState());
            plugin.getQueryFactory().doInsert("interactions", data);
        }
    }

    private UUID spawnControl(ConsoleInteraction interaction, Location location) {
        Material material = Material.LEVER;
        int cmd;
        switch (interaction) {
            case HANDBRAKE -> {
                cmd = 5001;
            }
            case THROTTLE -> {
                material = Material.LEVER;
                cmd = 5001;
            }
            case HELMIC_REGULATOR -> {
                material = Material.LEVER;
                cmd = 5001;
            }
            case DIRECTION -> {
                material = Material.LEVER;
                cmd = 5001;
            }
            case RELATIVITY_DIFFERENTIATOR -> {
                material = Material.LEVER;
                cmd = 5001;
            }
            case INTERIOR_LIGHT_LEVEL_SWITCH -> {
                material = Material.LEVER;
                cmd = 5001;
            }
            default -> {
                material = Material.LEVER;
                cmd = 5001;
            }
        }
        // spawn a handbrake
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(cmd);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
        is.setItemMeta(im);
        ItemDisplay handbrake = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, 0.5d, 0.5d), EntityType.ITEM_DISPLAY);
        handbrake.setItemStack(is);
        handbrake.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        handbrake.setPersistent(true);
        handbrake.setInvulnerable(true);
        UUID uuid = handbrake.getUniqueId();
        handbrake.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
        return uuid;
    }
}
