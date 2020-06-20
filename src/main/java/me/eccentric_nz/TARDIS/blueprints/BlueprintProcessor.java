package me.eccentric_nz.TARDIS.blueprints;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class BlueprintProcessor {

    public static boolean addPermission(TARDIS plugin, ItemStack is, Player player) {
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            PersistentDataContainer pdc = im.getPersistentDataContainer();
            if (pdc.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID())) {
                // check disk UUID is same as player UUID
                UUID diskUuid = pdc.get(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID());
                if (diskUuid != player.getUniqueId()) {
                    return false;
                }
                if (pdc.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
                    // get permission
                    String perm = pdc.get(plugin.getBlueprintKey(), PersistentDataType.STRING);
                    // insert database record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", diskUuid.toString());
                    set.put("permission", perm);
                    plugin.getQueryFactory().doInsert("blueprint", set);
                    TARDISMessage.send(player, "BLUEPRINT");
                    return true;
                }
            }
        }
        return false;
    }
}
