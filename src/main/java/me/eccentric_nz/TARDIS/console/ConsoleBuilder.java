package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
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
    private UUID right;
    private UUID wxyz;

    public ConsoleBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void create(Block block, int type, int id, String playerUuid) {
        StringBuilder builder = new StringBuilder();
        String prefix = "~";
        Block up = block.getRelative(BlockFace.UP);
        // save the block location
        HashMap<String, Object> setb = new HashMap<>();
        setb.put("tardis_id", id);
        setb.put("uuid", TARDISStaticLocationGetters.makeLocationStr(block.getLocation()));
        setb.put("control", "CENTRE");
        setb.put("state", 0);
        plugin.getQueryFactory().doInsert("interactions", setb);
        // spawn a centre display item
        UUID centre = spawnCentreDisplay(up.getLocation(), type);
        builder.append(centre);
        for (int i = 0; i < 6; i++) {
            ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = shard.getItemMeta();
            im.setCustomModelData(1000 + type);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, i);
            shard.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.25d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(shard);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            UUID uuid = display.getUniqueId();
            builder.append(prefix).append(uuid);
            display.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
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
            im.setCustomModelData(2000 + type);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, i);
            shard.setItemMeta(im);
            ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(up.getLocation().add(0.5d, 0.25d, 0.5d), EntityType.ITEM_DISPLAY);
            display.setItemStack(shard);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
            UUID uuid = display.getUniqueId();
            builder.append(prefix).append(uuid);
            display.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
            display.setPersistent(true);
            display.setInvulnerable(true);
            float yaw = Location.normalizeYaw(i);
            // set display rotation
            display.setRotation(yaw, 0);
        }
        // set interaction entities for console controls
        for (ConsoleInteraction i : ConsoleInteraction.values()) {
            // spawn a display entity and save it's UUID to the interaction entity
            UUID uuid = spawnControl(i, block.getLocation(), i.getYaw(), id, playerUuid);
            double x = i.getRelativePosition().getX();
            double z = i.getRelativePosition().getZ();
            Location location = block.getLocation().clone().add(x, 0.75, z);
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
            if (i == ConsoleInteraction.SCREEN_RIGHT || i == ConsoleInteraction.SCREEN_LEFT) {
                interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.STRING, builder.toString());
            }
            interaction.getPersistentDataContainer().set(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
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

    private UUID spawnControl(ConsoleInteraction interaction, Location location, float angle, int id, String playerUuid) {
        if (interaction == ConsoleInteraction.SCREEN_LEFT && right != null) {
            return right;
        }
        if ((interaction == ConsoleInteraction.X || interaction == ConsoleInteraction.Z || interaction == ConsoleInteraction.MULTIPLIER) && wxyz != null) {
            return wxyz;
        }
        Material material = interaction.getMaterial();
        int cmd = interaction.getCustomModelData();
        if (interaction == ConsoleInteraction.DIRECTION) {
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            cmd = (rsc.resultSet()) ? getCmd(rsc.getDirection()) + 10000 : 10000;
        }
        if (interaction == ConsoleInteraction.THROTTLE || interaction == ConsoleInteraction.RELATIVITY_DIFFERENTIATOR) {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUuid);
            if (rsp.resultSet()) {
                cmd = (interaction == ConsoleInteraction.THROTTLE) ? 1000 + rsp.getThrottle() : 6000 + rsp.getFlightMode();
            }
        }
        // spawn a control
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(cmd);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
        is.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) location.getWorld().spawnEntity(location.add(0.5d, 1.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        display.setPersistent(true);
        display.setInvulnerable(true);
        UUID uuid = display.getUniqueId();
        display.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
        float yaw = Location.normalizeYaw(angle);
        // set display rotation
        display.setRotation(yaw, 0);
        if (interaction == ConsoleInteraction.SCREEN_RIGHT) {
            right = uuid;
        }
        if (interaction == ConsoleInteraction.WORLD) {
            wxyz = uuid;
        }
        return uuid;
    }

    private int getCmd(COMPASS direction) {
        int cmd;
        switch (direction) {
            case NORTH_EAST -> cmd = 1;
            case EAST -> cmd = 2;
            case SOUTH_EAST -> cmd = 3;
            case SOUTH -> cmd = 4;
            case SOUTH_WEST -> cmd = 5;
            case WEST -> cmd = 6;
            case NORTH_WEST -> cmd = 7;
            default -> cmd = 0;
        }
        return cmd;
    }

    private UUID spawnCentreDisplay(Location up, int type) {
        ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta im = shard.getItemMeta();
        int cmd = 3000 + type;
        im.setCustomModelData(cmd);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, cmd);
        shard.setItemMeta(im);
        ItemDisplay display = (ItemDisplay) up.getWorld().spawnEntity(up.add(0.5d, 0.25d, 0.5d), EntityType.ITEM_DISPLAY);
        display.setItemStack(shard);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);
        UUID uuid = display.getUniqueId();
        display.getPersistentDataContainer().set(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
        display.setPersistent(true);
        display.setInvulnerable(true);
        return uuid;
    }
}
