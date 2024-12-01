/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisModel;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;

public class TARDISBuilderUtility {

    public static void saveDoorLocation(BuildData bd) {
        World world = bd.getLocation().getWorld();
        int x = bd.getLocation().getBlockX();
        int y = bd.getLocation().getBlockY();
        int z = bd.getLocation().getBlockZ();
        // remember the door location
        String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
        String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisID());
        // should insert the door when tardis is first made, and then update location there after!
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
     * @param location the location of the TARDIS
     * @param id the id of the TARDIS database record
     * @param direction the direction the TARDIS is facing
     */
    public static void saveDoorLocation(Location location, int id, String direction) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        // remember the door location
        String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
        String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
        TARDIS.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, id);
        // should insert the door when tardis is first made, and then update location there after!
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
            case WEEPING_ANGEL -> { return Material.GRAY_STAINED_GLASS_PANE; }
            case POLICE_BOX_TENNANT -> { return Material.CYAN_STAINED_GLASS_PANE; }
            case COLOURED -> { return Material.LEATHER_HORSE_ARMOR; }
            case PANDORICA -> { return Material.ENDER_PEARL; }
            default -> {
                String split = preset.toString().replace("POLICE_BOX_", "");
                String dye = split + "_DYE";
                return Material.valueOf(dye);
            }
        }
    }

    public static void setPoliceBoxHelmet(TARDIS plugin, ChameleonPreset preset, BuildData bd, ArmorStand stand) {
        Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, bd.getTardisID(), true);
        ItemStack is = new ItemStack(dye, 1);
        ItemMeta im = is.getItemMeta();
        switch (dye) {
            case BLACK_DYE -> im.setItemModel(ChameleonVariant.BLACK.getKey());
            case RED_DYE -> im.setItemModel(ChameleonVariant.RED.getKey());
            case BROWN_DYE -> im.setItemModel(ChameleonVariant.BROWN.getKey());
            case GREEN_DYE -> im.setItemModel(ChameleonVariant.GREEN.getKey());
            case BLUE_DYE -> im.setItemModel(ChameleonVariant.BLUE.getKey());
            case PURPLE_DYE -> im.setItemModel(ChameleonVariant.PURPLE.getKey());
            case CYAN_DYE -> im.setItemModel(ChameleonVariant.CYAN.getKey());
            case LIGHT_GRAY_DYE -> im.setItemModel(ChameleonVariant.LIGHT_GRAY.getKey());
            case GRAY_DYE -> im.setItemModel(ChameleonVariant.GRAY.getKey());
            case PINK_DYE -> im.setItemModel(ChameleonVariant.PINK.getKey());
            case LIME_DYE -> im.setItemModel(ChameleonVariant.LIME.getKey());
            case YELLOW_DYE -> im.setItemModel(ChameleonVariant.YELLOW.getKey());
            case LIGHT_BLUE_DYE -> im.setItemModel(ChameleonVariant.LIGHT_BLUE.getKey());
            case MAGENTA_DYE -> im.setItemModel(ChameleonVariant.MAGENTA.getKey());
            case ORANGE_DYE -> im.setItemModel(ChameleonVariant.ORANGE.getKey());
            case WHITE_DYE -> im.setItemModel(ChameleonVariant.WHITE.getKey());
            case CYAN_STAINED_GLASS_PANE -> im.setItemModel(ChameleonVariant.TENNANT.getKey());
            case LEATHER_HORSE_ARMOR -> im.setItemModel(ColouredVariant.TINTED.getKey());
            case WOLF_SPAWN_EGG -> im.setItemModel(ChameleonVariant.BAD_WOLF.getKey());
            case ENDER_PEARL -> im.setItemModel(ChameleonVariant.PANDORICA.getKey());
            case GRAY_STAINED_GLASS_PANE -> im.setItemModel(ChameleonVariant.WEEPING_ANGEL.getKey());
        }
        if (bd.shouldAddSign()) {
            String pb = "";
            switch (preset) {
                case WEEPING_ANGEL -> pb = "Weeping Angel";
                case PANDORICA -> pb = "Pandorica";
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
            im.setDisplayName(name);
            stand.setCustomName(name);
            stand.setCustomNameVisible(true);
        }
        if (preset == ChameleonPreset.COLOURED) {
            // get the colour
            ResultSetColour rsc = new ResultSetColour(plugin, bd.getTardisID());
            if (rsc.resultSet()) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) im;
                leatherArmorMeta.setColor(Color.fromRGB(rsc.getRed(), rsc.getGreen(), rsc.getBlue()));
                is.setItemMeta(leatherArmorMeta);
            }
        } else {
            is.setItemMeta(im);
        }
        EntityEquipment ee = stand.getEquipment();
        ee.setHelmet(is, true);
    }

    static void updateChameleonDemat(String preset, int id) {
        // update demat field in database
        HashMap<String, Object> set = new HashMap<>();
        set.put("chameleon_demat", preset);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }
}
