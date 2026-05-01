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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.playerprefs;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIPlayerPreferences;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConsoleLabel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
public class TARDISGeneralPrefsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Inventory inventory;

    public TARDISGeneralPrefsInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("Player Prefs Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // get player preferences
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        List<Boolean> values = new ArrayList<>();
        if (!rsp.resultSet()) {
            // make a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("player_prefs", set);
            // get the new record
            rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            rsp.resultSet();
        }
        Player player = plugin.getServer().getPlayer(uuid);
        values.add(rsp.isAnnounceRepeatersOn()); // 0
        values.add(rsp.isAutoOn()); // 1
        values.add(rsp.isAutoRescueOn()); // 2
        values.add(rsp.isTravelbarOn()); // 3
        values.add(rsp.isBeaconOn()); // 4
        values.add(rsp.isBuildOn()); // 5
        values.add(rsp.isDialogsOn()); // 6
        values.add(rsp.isDND()); // 7
        values.add(rsp.isOpenDisplayDoorOn()); // 8
        values.add(rsp.isDynamicLightsOn()); // 9
        values.add(rsp.isEpsOn()); // 10
        values.add(rsp.isFarmOn()); // 11
        values.add(plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid)); // 12
        values.add(rsp.isCloseGUIOn()); // 13
        values.add(rsp.isHadsOn()); // 14
        values.add(rsp.getHadsType().equals(HADS.DISPERSAL)); // 15
        values.add(rsp.isInfoOn()); // 16
        // get TARDIS preset
        Tardis tardis = null;
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("uuid", uuid.toString());
        ResultSetTardis rst = new ResultSetTardis(plugin, wherep, "", false);
        boolean hasTARDIS = rst.resultSet();
        if (hasTARDIS) {
            tardis = rst.getTardis();
            values.add(tardis.isIsomorphicOn()); // 17
            values.add(rst.getTardis().getPreset().equals(ChameleonPreset.JUNK_MODE)); // junk mode - 18
            ResultSetConsoleLabel rs = new ResultSetConsoleLabel(plugin, tardis.getTardisId());
            if (rs.resultSet()) {
                boolean hasLabels = hasLabels(rs.getLocation());
                values.add(hasLabels); // console labels - 19
            } else {
                values.add(false); // 19
            }
        } else {
            values.add(false); // 17
            values.add(false); // 18
            values.add(false); // 19
        }
        if (plugin.isWorldGuardOnServer()) {
            String chunk = tardis != null ? rst.getTardis().getChunk() : "minecraft:tardis_timevortex:1";
            String[] split = chunk.split(":");
            World world = plugin.getServer().getWorld(Key.key(split[1]));
            values.add(!plugin.getWorldGuardUtils().queryContainers(world, player.getName())); // lock containers - 20
        } else {
            values.add(false); // 20
        }
        values.add(rsp.isMinecartOn()); // 21
        values.add(rsp.isAutoPowerUp()); // 22
        values.add(rsp.isQuotesOn()); // 23
        values.add(rsp.isRendererOn()); // 24
        values.add(rsp.isAutoSiegeOn()); // 25
        values.add(rsp.isSignOn()); // 26
        values.add(rsp.isSfxOn()); // 27
        values.add(rsp.isSubmarineOn()); // 28
        values.add(rsp.isTelepathyOn()); // 29
        ItemStack[] stack = new ItemStack[36];
        for (GUIPlayerPreferences pref : GUIPlayerPreferences.values()) {
            if (pref.getMaterial() == Material.REPEATER) {
                ItemStack is = ItemStack.of(pref.getMaterial(), 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(pref.getName()));
                boolean v = values.get(pref.getSlot());
                if (pref.getOffFloats() != null) {
                    is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                            .addFloats(v ? pref.getOnFloats() : pref.getOffFloats())
                            .build());
                }
                if (pref == GUIPlayerPreferences.HADS_TYPE) {
                    is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(v ? "DISPERSAL" : "DISPLACEMENT")).build());
                } else {
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                            Component.text(v
                                    ? plugin.getLanguage().getString("SET_ON", "ON")
                                    : plugin.getLanguage().getString("SET_OFF", "OFF"))
                    )));
                }
                stack[pref.getSlot()] = is;
            }
        }
        if (!plugin.isWorldGuardOnServer()) {
            stack[GUIPlayerPreferences.LOCK_CONTAINERS.getSlot()] = null;
        }
        // back
        ItemStack back = ItemStack.of(GUIChameleonPresets.BACK.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        stack[33] = back;
        // close
        stack[35] = GUIItemFactory.close();
        return stack;
    }

    private boolean hasLabels(Location location) {
        ConsoleInteraction ci = ConsoleInteraction.HANDBRAKE;
        Location spawn = location.clone().add(ci.getRelativePosition().getX(), ci.getRelativePosition().getY() + ci.getHeight(), ci.getRelativePosition().getZ());
        for (Entity e : spawn.getWorld().getNearbyEntities(spawn, 1.0, 2, 1.0, (t) -> t.getType() == EntityType.TEXT_DISPLAY)) {
            if (e instanceof TextDisplay && !e.getPersistentDataContainer().has(plugin.getInteractionUuidKey(), PersistentDataType.BOOLEAN)) {
                return true;
            }
        }
        return false;
    }
}
