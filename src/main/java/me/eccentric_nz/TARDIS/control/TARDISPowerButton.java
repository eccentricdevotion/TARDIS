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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevelLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sensor.PowerSensor;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISPowerButton {

    private final TARDIS plugin;
    private final int id;
    private final Player player;
    private final ChameleonPreset preset;
    private final boolean powered;
    private final boolean hidden;
    private final boolean lights;
    private final Location loc;
    private final int level;
    private final TardisLight light;

    public TARDISPowerButton(TARDIS plugin, int id, Player player, ChameleonPreset preset, boolean powered, boolean hidden, boolean lights, Location loc, int level, TardisLight light) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.preset = preset;
        this.powered = powered;
        this.hidden = hidden;
        this.lights = lights;
        this.loc = loc;
        this.level = level;
        this.light = light;
    }

    public void clickButton() {
        HashMap<String, Object> wherep = new HashMap<>();
        wherep.put("tardis_id", id);
        HashMap<String, Object> setp = new HashMap<>();
        boolean isAdaptive = preset.equals(ChameleonPreset.ADAPTIVE);
        UUID uuid = player.getUniqueId();
        if (powered) {
            if (isTravelling(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_NO");
                return;
            }
            TARDISSounds.playTARDISSound(loc, "power_down");
            // power down
            setp.put("powered_on", 0);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_OFF");
            long delay = 0;
            // if hidden, rebuild
            if (hidden) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + player.getName() + " rebuild");
                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_FAIL");
                delay = 20L;
            }
            // police box lamp, delay it incase the TARDIS needs rebuilding
            if (isAdaptive || preset.usesArmourStand()) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, false, preset), delay);
            }
            // if lights are on, turn them off
            if (lights) {
                new TARDISLampToggler(plugin).flickSwitch(id, uuid, true, light);
            }
            // if beacon is on turn it off
            new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, false);
            // turn force field off
            if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(uuid)) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(uuid);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "FORCE_FIELD", "OFF");
            }
        } else {
            // don't power up if there is no power
            if (level <= plugin.getArtronConfig().getInt("standby")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_LOW");
                return;
            }
            TARDISSounds.playTARDISSound(loc, "power_up");
            // power up
            setp.put("powered_on", 1);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_ON");
            // if lights are off, turn them on
            if (lights) {
                new TARDISLampToggler(plugin).flickSwitch(id, uuid, false, light);
            }
            // determine beacon prefs
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            boolean beacon_on = true;
            if (rsp.resultSet()) {
                beacon_on = rsp.isBeaconOn();
            }
            // if beacon is off turn it on
            if (beacon_on) {
                new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, true);
            }
            // police box lamp
            if (isAdaptive || preset.usesArmourStand()) {
                new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, true, preset);
            }
        }
        // toggle the power sensor
        new PowerSensor(plugin, id).toggle();
        plugin.getQueryFactory().doUpdate("tardis", setp, wherep);
        // get light level switches
        // interior
        ResultSetLightLevelLocation rslls = new ResultSetLightLevelLocation(plugin, id, 50); // interior light level
        if (rslls.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rslls.getLocation());
            ItemFrame frame = getFrame(location.getBlock());
            if (frame != null) {
                setFrame(frame, powered, Control.LIGHT_LEVEL);
            }
        }
        // exterior
        ResultSetLightLevelLocation rsllx = new ResultSetLightLevelLocation(plugin, id, 49); // exterior lamp level
        if (rsllx.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsllx.getLocation());
            ItemFrame frame = getFrame(location.getBlock());
            if (frame != null) {
                setFrame(frame, powered, Control.EXTERIOR_LAMP);
            }
        }
    }

    private boolean isTravelling(int id) {
        return (plugin.getTrackerKeeper().getDematerialising().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getInVortex().contains(id));
    }

    private ItemFrame getFrame(Block block) {
        BoundingBox box = new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 1, block.getZ() + 1).expand(0.1d);
        for (Entity e : block.getWorld().getNearbyEntities(box, (d) -> d.getType() == EntityType.ITEM_FRAME)) {
            if (e instanceof ItemFrame frame) {
                return frame;
            }
        }
        return null;
    }

    private void setFrame(ItemFrame frame, boolean on, Control control) {
        ItemStack is = frame.getItem();
        ItemMeta im = is.getItemMeta();
        NamespacedKey model = im.getItemModel();
        String key;
        if (control == Control.LIGHT_LEVEL) {
            key = (model == null) ? on ? "block/control/light_0" : "block/control/light_0_off" : model.getKey();
        } else {
            key = (model == null) ? on ? "block/control/lamp_0" : "block/control/lamp_0_off" : model.getKey();
        }
        NamespacedKey nsk = on ? new NamespacedKey(plugin, key.replace("_off", "")) : new NamespacedKey(plugin, key + "_off");
        im.setItemModel(nsk);
        is.setItemMeta(im);
        frame.setItem(is);
    }
}
