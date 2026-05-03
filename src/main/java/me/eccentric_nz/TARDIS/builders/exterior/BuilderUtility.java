/*
 * Copyright (C) 2026 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders.exterior;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisModel;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BuilderUtility {

    public static void saveDoorLocation(BuildData bd, boolean slab) {
        World world = bd.getLocation().getWorld();
        int x = bd.getLocation().getBlockX();
        int y = bd.getLocation().getBlockY();
        if (slab) {
            y -= 1;
        }
        int z = bd.getLocation().getBlockZ();
        // remember the door location
        String doorloc = world.getKey().asString() + ":" + x + ":" + y + ":" + z;
        String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
        // should insert the door when tardis is first made, and then update location thereafter!
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("door_type", 0);
        whered.put("tardis_id", bd.getTardisID());
        ResultSetDoors rsd = new ResultSetDoors(TARDIS.plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<>();
        setd.put("door_location", doorloc);
        setd.put("door_direction", bd.getDirection().forPreset().toString());
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<>();
            whereid.put("door_id", rsd.getDoor_id());
            TARDIS.plugin.getQueryFactory().doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", bd.getTardisID());
            setd.put("door_type", 0);
            TARDIS.plugin.getQueryFactory().doInsert("doors", setd);
        }
    }

    /**
     * Saves/updates the door location after the TARDIS exterior has been flown
     *
     * @param location  the location of the TARDIS
     * @param id        the id of the TARDIS database record
     * @param direction the direction the TARDIS is facing
     */
    public static void saveDoorLocation(Location location, int id, String direction) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        // remember the door location
        String doorloc = world.getKey().asString() + ":" + x + ":" + y + ":" + z;
        String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, id);
        // should insert the door when tardis is first made, and then update location thereafter!
        HashMap<String, Object> whered = new HashMap<>();
        whered.put("door_type", 0);
        whered.put("tardis_id", id);
        ResultSetDoors rsd = new ResultSetDoors(TARDIS.plugin, whered, false);
        HashMap<String, Object> setd = new HashMap<>();
        setd.put("door_location", doorloc);
        setd.put("door_direction", direction);
        if (rsd.resultSet()) {
            HashMap<String, Object> whereid = new HashMap<>();
            whereid.put("door_id", rsd.getDoor_id());
            TARDIS.plugin.getQueryFactory().doUpdate("doors", setd, whereid);
        } else {
            setd.put("tardis_id", id);
            setd.put("door_type", 0);
            TARDIS.plugin.getQueryFactory().doInsert("doors", setd);
        }
    }

    public static Material getMaterialForArmourStand(ChameleonPreset preset, int id, boolean isMaterialisation) {
        switch (preset) {
            case ITEM -> {
                ResultSetTardisModel rstm = new ResultSetTardisModel(TARDIS.plugin);
                if (rstm.fromID(id)) {
                    String item = (isMaterialisation) ? rstm.getItemPreset() : rstm.getItemDemat();
                    return Material.valueOf(TARDIS.plugin.getCustomModelConfig().getString("models." + item + ".item"));
                } else {
                    return Material.BLUE_DYE;
                }
            }
            case WEEPING_ANGEL -> {
                return Material.GRAY_STAINED_GLASS_PANE;
            }
            case POLICE_BOX_TENNANT -> {
                return Material.CYAN_STAINED_GLASS_PANE;
            }
            case COLOURED -> {
                return Material.LEATHER_HORSE_ARMOR;
            }
            case PANDORICA -> {
                return Material.ENDER_PEARL;
            }
            case SIDRAT -> {
                return Material.GREEN_STAINED_GLASS_PANE;
            }
            case BATTLE -> {
                return Material.RED_STAINED_GLASS_PANE;
            }
            default -> {
                String split = preset.toString().replace("POLICE_BOX_", "");
                String dye = split + "_DYE";
                return Material.valueOf(dye);
            }
        }
    }

    public static void setPoliceBoxHelmet(TARDIS plugin, ChameleonPreset preset, BuildData bd, ArmorStand stand) {
        Material dye = BuilderUtility.getMaterialForArmourStand(preset, bd.getTardisID(), true);
        ItemStack is = ItemStack.of(dye, 1);
        switch (dye) {
            case BLACK_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BLACK_CLOSED.getKey());
            case RED_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.RED_CLOSED.getKey());
            case BROWN_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BROWN_CLOSED.getKey());
            case GREEN_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.GREEN_CLOSED.getKey());
            case BLUE_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BLUE_CLOSED.getKey());
            case PURPLE_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.PURPLE_CLOSED.getKey());
            case CYAN_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.CYAN_CLOSED.getKey());
            case LIGHT_GRAY_DYE ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.LIGHT_GRAY_CLOSED.getKey());
            case GRAY_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.GRAY_CLOSED.getKey());
            case PINK_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.PINK_CLOSED.getKey());
            case LIME_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.LIME_CLOSED.getKey());
            case YELLOW_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.YELLOW_CLOSED.getKey());
            case LIGHT_BLUE_DYE ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.LIGHT_BLUE_CLOSED.getKey());
            case MAGENTA_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.MAGENTA_CLOSED.getKey());
            case ORANGE_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.ORANGE_CLOSED.getKey());
            case WHITE_DYE -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.WHITE_CLOSED.getKey());
            case CYAN_STAINED_GLASS_PANE ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.TENNANT_CLOSED.getKey());
            case LEATHER_HORSE_ARMOR ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ColouredVariant.TINTED_CLOSED.getKey());
            case WOLF_SPAWN_EGG -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BAD_WOLF_CLOSED.getKey());
            case ENDER_PEARL -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.PANDORICA_CLOSED.getKey());
            case GREEN_STAINED_GLASS_PANE ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.SIDRAT_CLOSED.getKey());
            case GRAY_STAINED_GLASS_PANE ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey());
            case RED_STAINED_GLASS_PANE ->
                    is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BATTLE_CLOSED.getKey());
            // CUSTOM
            default ->
                    is.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(plugin, getCustomModelPath(dye.toString()) + "_closed"));
        }
        if (bd.shouldAddSign() && bd.getPlayer() != null) {
            String pb = "";
            switch (preset) {
                case WEEPING_ANGEL -> pb = "Weeping Angel";
                case PANDORICA -> pb = "Pandorica";
                case SIDRAT -> pb = "SIDRAT";
                case BATTLE -> pb = "Battle TARDIS";
                case ITEM -> {
                    for (String k : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
                        if (plugin.getCustomModelConfig().getString("models." + k + ".item").equals(dye.toString())) {
                            pb = k;
                            break;
                        }
                    }
                }
                default -> pb = "Police Box";
            }
            String name = bd.getPlayer().getName() + "'s " + pb;
            Component custom = Component.text(name);
            is.setData(DataComponentTypes.CUSTOM_NAME, custom);
            stand.customName(custom);
            stand.setCustomNameVisible(true);
        }
        if (preset == ChameleonPreset.COLOURED) {
            // get the colour
            ResultSetColour rsc = new ResultSetColour(plugin, bd.getTardisID());
            if (rsc.resultSet()) {
                is.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                        .color(Color.fromRGB(rsc.getRed(), rsc.getGreen(), rsc.getBlue()))
                        .build());
            }
        }
        EntityEquipment ee = stand.getEquipment();
        ee.setHelmet(is, true);
    }

    public static void updateChameleonDemat(String preset, int id) {
        // update demat field in database
        HashMap<String, Object> set = new HashMap<>();
        set.put("chameleon_demat", preset);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }

    public static String getCustomModelPath(String type) {
        for (String c : TARDIS.plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (type.equals(TARDIS.plugin.getCustomModelConfig().getString("models." + c + ".item"))) {
                return TARDISStringUtils.toUnderscoredLowercase(c);
            }
        }
        return "custom";
    }
}
