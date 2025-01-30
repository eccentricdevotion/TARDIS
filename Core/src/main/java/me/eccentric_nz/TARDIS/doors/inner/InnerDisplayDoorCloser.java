package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.BoneDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ClassicDoorVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.doors.DoorAnimator;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InnerDisplayDoorCloser {

    private final TARDIS plugin;

    public InnerDisplayDoorCloser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void close(Block block, int id, UUID uuid, boolean outside) {
        ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
        if (display != null) {
            TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
            if (tdi != null) {
                // close door
                if (outside) {
                    ItemStack itemStack = display.getItemStack();
                    ItemMeta im = itemStack.getItemMeta();
                    if ((tdi.toString().endsWith("OPEN") || tdi == TARDISDisplayItem.CUSTOM_DOOR)) {
                        switch (tdi.getMaterial()) {
                            case IRON_DOOR -> im.setItemModel(TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey());
                            case BIRCH_DOOR -> im.setItemModel(BoneDoorVariant.BONE_DOOR_CLOSED.getKey());
                            case CHERRY_DOOR -> im.setItemModel(ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey());
                            default -> im.setItemModel(Door.getClosedModel(tdi.getMaterial()));
                        }
                    }
                    itemStack.setItemMeta(im);
                    display.setItemStack(itemStack);
                } else {
                    new DoorAnimator(plugin, display).animate(true);
                }
                if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                    if (rs.resultSet()) {
                        // remove portal
                        TARDISTeleportLocation removed = plugin.getTrackerKeeper().getPortals().remove(block.getLocation());
                        if (removed == null) {
                            DoorUtility.debugPortal(block.getLocation().toString());
                        }
                        // remove movers
                        if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                            if (rs.getTardis().getCompanions().equalsIgnoreCase("everyone")) {
                                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                    plugin.getTrackerKeeper().getMovers().remove(p.getUniqueId());
                                }
                            } else {
                                String[] companions = rs.getTardis().getCompanions().split(":");
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
}
