package me.eccentric_nz.tardis.blueprints;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class BlueprintProcessor {

	public static void addPermission(TARDISPlugin plugin, ItemStack is, Player player) {
		ItemMeta im = is.getItemMeta();
		if (im != null) {
			PersistentDataContainer pdc = im.getPersistentDataContainer();
			if (pdc.has(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID())) {
				// check disk UUID is same as player UUID
				UUID diskUuid = pdc.get(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID());
				assert diskUuid != null;
				if (!diskUuid.equals(player.getUniqueId())) {
					return;
				}
				if (pdc.has(plugin.getBlueprintKey(), PersistentDataType.STRING)) {
					// get permission
					String perm = pdc.get(plugin.getBlueprintKey(), PersistentDataType.STRING);
					// insert database record
					HashMap<String, Object> set = new HashMap<>();
					set.put("uuid", diskUuid.toString());
					set.put("permission", perm);
					plugin.getQueryFactory().doInsert("blueprint", set);
					TARDISMessage.send(player, "BLUEPRINT", Objects.requireNonNull(im.getLore()).get(0));
				}
			}
		}
	}
}
