/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.utility;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Openable;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISStaticUtils {

    private static final UUID ZERO_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static NamedTextColor policeBoxToNamedTextColor(String preset) {
        switch (preset) {
            case "POLICE_BOX_WHITE" -> {
                return NamedTextColor.WHITE;
            }
            case "POLICE_BOX_BROWN", "POLICE_BOX_ORANGE" -> {
                return NamedTextColor.GOLD;
            }
            case "POLICE_BOX_BLACK" -> {
                return NamedTextColor.BLACK;
            }
            case "POLICE_BOX_CYAN" -> {
                return NamedTextColor.DARK_AQUA;
            }
            case "POLICE_BOX_LIGHT_BLUE" -> {
                return NamedTextColor.BLUE;
            }
            case "POLICE_BOX_GRAY" -> {
                return NamedTextColor.DARK_GRAY;
            }
            case "POLICE_BOX_GREEN" -> {
                return NamedTextColor.DARK_GREEN;
            }
            case "POLICE_BOX_PURPLE" -> {
                return NamedTextColor.DARK_PURPLE;
            }
            case "POLICE_BOX_RED" -> {
                return NamedTextColor.DARK_RED;
            }
            case "POLICE_BOX_LIGHT_GRAY" -> {
                return NamedTextColor.GRAY;
            }
            case "POLICE_BOX_LIME" -> {
                return NamedTextColor.GREEN;
            }
            case "POLICE_BOX_PINK" -> {
                return NamedTextColor.LIGHT_PURPLE;
            }
            case "POLICE_BOX_MAGENTA" -> {
                return NamedTextColor.RED;
            }
            case "POLICE_BOX_YELLOW" -> {
                return NamedTextColor.YELLOW;
            }
            default -> {
                return NamedTextColor.DARK_BLUE;
            }
        }
    }

    /**
     *
     */
    public static boolean isOwnerOnline(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, where, "", false);
        if (rst.resultSet()) {
            Tardis tardis = rst.getTardis();
            if (!tardis.isTardisInit()) {
                return false;
            }
            UUID ownerUUID = tardis.getUuid();
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(TARDIS.plugin, ownerUUID.toString());
            boolean hads_on = true;
            if (rsp.resultSet()) {
                hads_on = rsp.isHadsOn();
            }
            return (TARDIS.plugin.getServer().getOfflinePlayer(ownerUUID).isOnline() && hads_on);
        } else {
            return false;
        }
    }

    /**
     * Get the direction a player is facing.
     *
     * @param p    the player
     * @param swap whether to swap the direction E &lt;-&gt; W, S &lt;-&gt; N
     * @return the direction the player is facing
     */
    public static String getPlayersDirection(Player p, boolean swap) {
        // get player direction
        String d = p.getFacing().toString();
        if (swap) {
            d = switch (p.getFacing()) {
                case EAST -> "WEST";
                case NORTH -> "SOUTH";
                case WEST -> "EAST";
                default -> "NORTH";
            };
        }
        return d;
    }

    /**
     * Get whether the biome is ocean.
     *
     * @param b the biome to check
     * @return true if it is ocean
     */
    public static boolean isOceanBiome(Biome b) {
        return b.getKey().getKey().endsWith("ocean");
    }

    /**
     * Get a human-readable time from server ticks.
     *
     * @param t the time in ticks
     * @return a string representation of the time
     */
    public static String getTime(long t) {
        if (t > 0 && t <= 2000) {
            return "early morning";
        }
        if (t > 2000 && t <= 3500) {
            return "mid morning";
        }
        if (t > 3500 && t <= 5500) {
            return "late morning";
        }
        if (t > 5500 && t <= 6500) {
            return "around noon";
        }
        if (t > 6500 && t <= 8000) {
            return "afternoon";
        }
        if (t > 8000 && t <= 10000) {
            return "mid afternoon";
        }
        if (t > 10000 && t <= 12000) {
            return "late afternoon";
        }
        if (t > 12000 && t <= 14000) {
            return "twilight";
        }
        if (t > 14000 && t <= 16000) {
            return "evening";
        }
        if (t > 16000 && t <= 17500) {
            return "late evening";
        }
        if (t > 17500 && t <= 18500) {
            return "around midnight";
        }
        if (t > 18500 && t <= 20000) {
            return "the small hours";
        }
        if (t > 20000 && t <= 22000) {
            return "the wee hours";
        } else {
            return "pre-dawn";
        }
    }

    /**
     * Checks whether a door is open.
     *
     * @param door the door block
     * @return true or false
     */
    public static boolean isDoorOpen(Block door) {
        if (door.getBlockData() instanceof Openable openable) {
            return openable.isOpen();
        }
        ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(door);
        if (display != null) {
            TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
            // need to cater for all doors including custom
            return tdi == TARDISDisplayItem.DOOR_OPEN
                    || tdi == TARDISDisplayItem.DOOR_BOTH_OPEN
                    || tdi == TARDISDisplayItem.BONE_DOOR_OPEN
                    || tdi == TARDISDisplayItem.CLASSIC_DOOR_OPEN
                    || isCustomDoorOpen(display);
        }
        return false;
    }

    private static boolean isCustomDoorOpen(ItemDisplay display) {
        return display.getItemStack().getItemMeta().getItemModel().getKey().endsWith("_open");
    }

    /**
     * Sets the Chameleon Sign text or messages the player.
     *
     * @param loc  the location string retrieved from the database
     * @param line the line number to set
     * @param text the text to write
     * @param p    the player to message (if the Chameleon control is not a sign)
     */
    public static void setSign(String loc, int line, String text, Player p) {
        if (!loc.isEmpty()) {
            // get sign block so we can update it
            Location l = TARDISStaticLocationGetters.getLocationFromDB(loc);
            if (l != null) {
                Chunk chunk = l.getChunk();
                while (!chunk.isLoaded()) {
                    chunk.load();
                }
                Block cc = l.getBlock();
                if (Tag.SIGNS.isTagged(cc.getType())) {
                    Sign sign = (Sign) cc.getState();
                    sign.getSide(Side.FRONT).line(line, Component.text(text));
                    sign.update();
                } else {
                    TARDIS.plugin.getMessenger().send(p, TardisModule.TARDIS, "CHAM", " " + text);
                }
            }
        }
    }

    public static boolean isInfested(Material material) {
        return switch (material) {
            case INFESTED_CHISELED_STONE_BRICKS, INFESTED_COBBLESTONE, INFESTED_CRACKED_STONE_BRICKS,
                 INFESTED_MOSSY_STONE_BRICKS, INFESTED_STONE, INFESTED_STONE_BRICKS -> true;
            default -> false;
        };
    }

    public static boolean isBanner(Material material) {
        return Tag.BANNERS.isTagged(material);
    }

    public static String getNick(UUID uuid) {
        if (TARDIS.plugin.getConfig().getBoolean("police_box.use_nick")) {
            Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            if (essentials != null) {
                User user = essentials.getUser(uuid);
                String prefix = essentials.getSettings().getNicknamePrefix();
                return ChatColor.stripColor(user.getNick()).replace(prefix, "");
            }
        }
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            return (offlinePlayer.getName() != null) ? offlinePlayer.getName() : "Unknown";
        } else {
            return player.getName();
        }
    }

    public static String getNick(Player player) {
        if (TARDIS.plugin.getConfig().getBoolean("police_box.use_nick")) {
            Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            if (essentials != null) {
                User user = essentials.getUser(player.getUniqueId());
                String prefix = essentials.getSettings().getNicknamePrefix();
                return ChatColor.stripColor(user.getNick()).replace(prefix, "");
            }
        }
        return player.getName();
    }

    public static UUID getZERO_UUID() {
        return ZERO_UUID;
    }

    /**
     * Checks whether an ItemStack is a sonic screwdriver
     *
     * @param is the ItemStack to check
     * @return true if the ItemStack is a Sonic Screwdriver
     */
    public static boolean isSonic(ItemStack is) {
        if (is != null && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                return ComponentUtils.endsWith(im.displayName(),"Sonic Screwdriver");
            }
        }
        return false;
    }

    public static void warnPreset(UUID uuid) {
        Player player = TARDIS.plugin.getServer().getPlayer(uuid);
        if (player != null) {
            TARDIS.plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_BROKEN");
        }
    }

    /**
     * Gets the chat colour from a display name. Used by Key and Sonic preference GUIs.
     *
     * @param input the display name of the item
     * @return a NamedTextColor
     */
    public static NamedTextColor getColor(Component input) {
        NamedTextColor colour = NamedTextColor.WHITE;
        GsonComponentSerializer serializer = GsonComponentSerializer.gson();
        String json = serializer.serialize(input);
        if (json.startsWith("{")) {
            JsonObject object = JsonParser.parseString(json).getAsJsonObject();
            if (object.has("color")) {
                String c = object.get("color").getAsString();
                for (NamedTextColor ntc : NamedTextColor.NAMES.values()) {
                    if (c.equalsIgnoreCase(ntc.toString())) {
                        colour = ntc;
                    }
                }
            }
        }
        return colour;
    }
}
