package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ConsolePart;
import me.eccentric_nz.TARDIS.custommodels.keys.DirectionVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

    public void create(Block block, String colour, int id, String playerUuid) {
        StringBuilder builder = new StringBuilder();
        String prefix = "~";
        Block up = block.getRelative(BlockFace.UP);
        // save the block location
        if (id > 0) {
            HashMap<String, Object> setb = new HashMap<>();
            setb.put("tardis_id", id);
            setb.put("uuid", TARDISStaticLocationGetters.makeLocationStr(block.getLocation()));
            setb.put("control", "CENTRE");
            setb.put("state", 0);
            plugin.getQueryFactory().doInsert("interactions", setb);
        }
        // spawn a centre display item
        UUID centre = spawnCentreDisplay(up.getLocation(), colour);
        builder.append(centre);
        for (int i = 0; i < 6; i++) {
            ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
            ItemMeta im = shard.getItemMeta();
            switch (colour) {
                case "console_rustic" -> im.setItemModel(ConsolePart.CONSOLE_RUSTIC.getKey());
                case "console_brown" -> im.setItemModel(ConsolePart.CONSOLE_BROWN.getKey());
                case "console_pink" -> im.setItemModel(ConsolePart.CONSOLE_PINK.getKey());
                case "console_magenta" -> im.setItemModel(ConsolePart.CONSOLE_MAGENTA.getKey());
                case "console_purple" -> im.setItemModel(ConsolePart.CONSOLE_PURPLE.getKey());
                case "console_blue" -> im.setItemModel(ConsolePart.CONSOLE_BLUE.getKey());
                case "console_light_blue" -> im.setItemModel(ConsolePart.CONSOLE_LIGHT_BLUE.getKey());
                case "console_cyan" -> im.setItemModel(ConsolePart.CONSOLE_CYAN.getKey());
                case "console_green" -> im.setItemModel(ConsolePart.CONSOLE_GREEN.getKey());
                case "console_lime" -> im.setItemModel(ConsolePart.CONSOLE_LIME.getKey());
                case "console_yellow" -> im.setItemModel(ConsolePart.CONSOLE_YELLOW.getKey());
                case "console_orange" -> im.setItemModel(ConsolePart.CONSOLE_ORANGE.getKey());
                case "console_red" -> im.setItemModel(ConsolePart.CONSOLE_RED.getKey());
                case "console_white" -> im.setItemModel(ConsolePart.CONSOLE_WHITE.getKey());
                case "console_black" -> im.setItemModel(ConsolePart.CONSOLE_BLACK.getKey());
                case "console_gray" -> im.setItemModel(ConsolePart.CONSOLE_GRAY.getKey());
                default -> im.setItemModel(ConsolePart.CONSOLE_LIGHT_GRAY.getKey());
            }
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
            switch (colour) {
                case "console_rustic" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_RUSTIC.getKey());
                case "console_brown" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_BROWN.getKey());
                case "console_pink" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_PINK.getKey());
                case "console_magenta" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_MAGENTA.getKey());
                case "console_purple" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_PURPLE.getKey());
                case "console_blue" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_BLUE.getKey());
                case "console_light_blue" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_LIGHT_BLUE.getKey());
                case "console_cyan" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_CYAN.getKey());
                case "console_green" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_GREEN.getKey());
                case "console_lime" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_LIME.getKey());
                case "console_yellow" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_YELLOW.getKey());
                case "console_orange" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_ORANGE.getKey());
                case "console_red" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_RED.getKey());
                case "console_white" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_WHITE.getKey());
                case "console_black" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_BLACK.getKey());
                case "console_gray" -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_GRAY.getKey());
                default -> im.setItemModel(ConsolePart.CONSOLE_DIVISION_LIGHT_GRAY.getKey());
            }
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
                if (id > 0) {
                    int cid = (i == ConsoleInteraction.EXTERIOR_LAMP_LEVEL_SWITCH) ? 49 : 50;
                    plugin.getQueryFactory().insertControl(id, cid, location.toString(), 0);
                }
            }
            if (i == ConsoleInteraction.HANDBRAKE && id > 0) {
                plugin.getQueryFactory().insertControl(id, 0, location.toString(), 0);
            }
            if (i == ConsoleInteraction.SCREEN_RIGHT || i == ConsoleInteraction.SCREEN_LEFT) {
                interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.STRING, builder.toString());
            }
            interaction.getPersistentDataContainer().set(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
            interaction.setInteractionWidth(i.getWidth());
            interaction.setInteractionHeight(i.getHeight());
            interaction.setPersistent(true);
            interaction.setInvulnerable(true);
            if (id > 0) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("tardis_id", id);
                data.put("uuid", interaction.getUniqueId());
                data.put("control", i.toString());
                data.put("state", i.getDefaultState());
                plugin.getQueryFactory().doInsert("interactions", data);
            }
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
        NamespacedKey key = interaction.getCustomModel();
        if (interaction == ConsoleInteraction.DIRECTION && id > 0) {
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            key = (rsc.resultSet()) ? getKey(rsc.getDirection()): DirectionVariant.DIRECTION_NORTH.getKey();
        }
        if (interaction == ConsoleInteraction.THROTTLE || interaction == ConsoleInteraction.RELATIVITY_DIFFERENTIATOR) {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUuid);
            if (rsp.resultSet()) {
                key = (interaction == ConsoleInteraction.THROTTLE) ? ModelledControl.values()[70 + rsp.getThrottle()].getKey() : ModelledControl.values()[50 + rsp.getFlightMode()].getKey();
            }
        }
        // spawn a control
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        im.setItemModel(key);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, key.getKey());
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

    private NamespacedKey getKey(COMPASS direction) {
        NamespacedKey key;
        switch (direction) {
            case NORTH_EAST -> key = DirectionVariant.DIRECTION_NORTH_EAST.getKey();
            case EAST -> key = DirectionVariant.DIRECTION_EAST.getKey();
            case SOUTH_EAST -> key = DirectionVariant.DIRECTION_SOUTH_EAST.getKey();
            case SOUTH -> key = DirectionVariant.DIRECTION_SOUTH.getKey();
            case SOUTH_WEST -> key = DirectionVariant.DIRECTION_SOUTH_WEST.getKey();
            case WEST -> key = DirectionVariant.DIRECTION_WEST.getKey();
            case NORTH_WEST -> key = DirectionVariant.DIRECTION_NORTH_WEST.getKey();
            default -> key = DirectionVariant.DIRECTION_NORTH.getKey();
        }
        return key;
    }

    private UUID spawnCentreDisplay(Location up, String type) {
        ItemStack shard = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta im = shard.getItemMeta();
        NamespacedKey model;
        switch (type) {
            case "console_rustic" -> model = ConsolePart.CONSOLE_CENTRE_RUSTIC.getKey();
            case "console_brown" -> model = ConsolePart.CONSOLE_CENTRE_BROWN.getKey();
            case "console_pink" -> model = ConsolePart.CONSOLE_CENTRE_PINK.getKey();
            case "console_magenta" -> model = ConsolePart.CONSOLE_CENTRE_MAGENTA.getKey();
            case "console_purple" -> model = ConsolePart.CONSOLE_CENTRE_PURPLE.getKey();
            case "console_blue" -> model = ConsolePart.CONSOLE_CENTRE_BLUE.getKey();
            case "console_light_blue" -> model = ConsolePart.CONSOLE_CENTRE_LIGHT_BLUE.getKey();
            case "console_cyan" -> model = ConsolePart.CONSOLE_CENTRE_CYAN.getKey();
            case "console_green" -> model = ConsolePart.CONSOLE_CENTRE_GREEN.getKey();
            case "console_lime" -> model = ConsolePart.CONSOLE_CENTRE_LIME.getKey();
            case "console_yellow" -> model = ConsolePart.CONSOLE_CENTRE_YELLOW.getKey();
            case "console_orange" -> model = ConsolePart.CONSOLE_CENTRE_ORANGE.getKey();
            case "console_red" -> model = ConsolePart.CONSOLE_CENTRE_RED.getKey();
            case "console_white" -> model = ConsolePart.CONSOLE_CENTRE_WHITE.getKey();
            case "console_black" -> model = ConsolePart.CONSOLE_CENTRE_BLACK.getKey();
            case "console_gray" -> model = ConsolePart.CONSOLE_CENTRE_GRAY.getKey();
            default -> model = ConsolePart.CONSOLE_CENTRE_LIGHT_GRAY.getKey();
        }
        im.setItemModel(model);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
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
