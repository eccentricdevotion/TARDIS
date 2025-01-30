package me.eccentric_nz.TARDIS.doors.outer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.utils.PandoricaOpens;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OuterDisplayDoorCloser {

    private final TARDIS plugin;

    public OuterDisplayDoorCloser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void close(ArmorStand stand, int id, UUID uuid) {
        if (stand == null) {
            return;
        }
        EntityEquipment ee = stand.getEquipment();
        ItemStack dye = ee.getHelmet();
        if (dye == null) {
            return;
        }
        if ((TARDISConstants.DYES.contains(dye.getType()) || plugin.getUtils().isCustomModel(dye)) && dye.hasItemMeta()) {
            ItemMeta dim = dye.getItemMeta();
            if (!dim.hasItemModel()) {
                return;
            }
            // exterior portal
            Location portal = new Location(stand.getWorld(), stand.getLocation().getBlockX(), stand.getLocation().getBlockY(), stand.getLocation().getBlockZ());
            // close door
            if (dye.getType() == Material.ENDER_PEARL) {
                new PandoricaOpens(plugin).animate(stand, false);
            } else {
                switch (dye.getType()) {
                    case CYAN_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.TENNANT_CLOSED.getKey());
                    case GRAY_STAINED_GLASS_PANE -> dim.setItemModel(ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey());
                    case WHITE_DYE -> dim.setItemModel(ChameleonVariant.WHITE_CLOSED.getKey());
                    case ORANGE_DYE -> dim.setItemModel(ChameleonVariant.ORANGE_CLOSED.getKey());
                    case MAGENTA_DYE -> dim.setItemModel(ChameleonVariant.MAGENTA_CLOSED.getKey());
                    case LIGHT_BLUE_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_BLUE_CLOSED.getKey());
                    case YELLOW_DYE -> dim.setItemModel(ChameleonVariant.YELLOW_CLOSED.getKey());
                    case LIME_DYE -> dim.setItemModel(ChameleonVariant.LIME_CLOSED.getKey());
                    case PINK_DYE -> dim.setItemModel(ChameleonVariant.PINK_CLOSED.getKey());
                    case GRAY_DYE -> dim.setItemModel(ChameleonVariant.GRAY_CLOSED.getKey());
                    case LIGHT_GRAY_DYE -> dim.setItemModel(ChameleonVariant.LIGHT_GRAY_CLOSED.getKey());
                    case CYAN_DYE -> dim.setItemModel(ChameleonVariant.CYAN_CLOSED.getKey());
                    case PURPLE_DYE -> dim.setItemModel(ChameleonVariant.PURPLE_CLOSED.getKey());
                    case BLUE_DYE -> dim.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
                    case BROWN_DYE -> dim.setItemModel(ChameleonVariant.BROWN_CLOSED.getKey());
                    case GREEN_DYE -> dim.setItemModel(ChameleonVariant.GREEN_CLOSED.getKey());
                    case RED_DYE -> dim.setItemModel(ChameleonVariant.RED_CLOSED.getKey());
                    case BLACK_DYE -> dim.setItemModel(ChameleonVariant.BLACK_CLOSED.getKey());
                    case LEATHER_HORSE_ARMOR -> dim.setItemModel(ColouredVariant.TINTED_CLOSED.getKey());
                    default -> {
                        // get the custom model config
                        NamespacedKey c = plugin.getUtils().getCustomModel(dye.getType(), "_closed");
                        if (c != null) {
                            dim.setItemModel(Door.getOpenModel(dye.getType()));
                        }
                    }
                }
                dye.setItemMeta(dim);
                ee.setHelmet(dye, true);
                TARDISSounds.playDoorSound(true, portal);
            }
            if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    // remove portal
                    TARDISTeleportLocation removed = plugin.getTrackerKeeper().getPortals().remove(portal);
                    if (removed == null) {
                        DoorUtility.debugPortal(portal.toString());
                    }
                    // remove movers
                    if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                        if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                plugin.getTrackerKeeper().getMovers().remove(p.getUniqueId());
                            }
                        } else {
                            String[] companions = tardis.getCompanions().split(":");
                            for (String c : companions) {
                                if (!c.isEmpty()) {
                                    plugin.getTrackerKeeper().getMovers().remove(UUID.fromString(c));
                                }
                            }
                            plugin.getTrackerKeeper().getMovers().remove(uuid);
                        }
                    }
                }
            }
        }
    }
}