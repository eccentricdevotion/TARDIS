/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public Lstainnse as published by
 * the Free Software Foundation, either version 3 of the Lstainnse, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public Lstainnse for more details.
 *
 * You should have received a copy of the GNU General Public Lstainnse
 * along with this program. If not, see <http://www.gnu.org/lstainnses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.chameleon.utils.CustomPreset;
import me.eccentric_nz.TARDIS.chameleon.utils.ChameleonColumn;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISCustomPreset;
import me.eccentric_nz.TARDIS.chameleon.utils.StainedGlassLookup;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

/**
 * Within the first nanosecond of landing in a new location, the TARDIS chameleon circuit analyses the surrounding area,
 * calculates a twelve-dimensional data map of all objects within a thousand-mile radius and then determines which outer
 * shell would best blend in with the environment.
 *
 * @author eccentric_nz
 */
public class TARDISMakePresetListener implements Listener {

    private final TARDIS plugin;
    private final int[] orderx;
    private final int[] orderz;
    private final String GLASS = TARDISConstants.GLASS.getAsString();

    public TARDISMakePresetListener(TARDIS plugin) {
        this.plugin = plugin;
        orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
        orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
    }

    /**
     * Listens for player clicking blocks. If the player's name is contained in various tracking HashMaps then we know
     * that they are trying to create a TARDIS area.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAreaInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        if (block != null) {
            if (plugin.getTrackerKeeper().getPreset().containsKey(uuid)) {
                String name = plugin.getTrackerKeeper().getPreset().get(uuid);
                JsonArray blueprint = new JsonArray();
                JsonArray stained = new JsonArray();
                JsonArray glass = new JsonArray();
                Location block_loc = block.getLocation();
                World w = block_loc.getWorld();
                int fx = block_loc.getBlockX();
                int fy = block_loc.getBlockY();
                int fz = block_loc.getBlockZ();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PRESET_SCAN");
                for (int c = 0; c < 10; c++) {
                    JsonArray b = new JsonArray();
                    JsonArray s = new JsonArray();
                    JsonArray g = new JsonArray();
                    for (int y = fy; y < (fy + 4); y++) {
                        Block at = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
                        Material material = at.getType();
                        BlockData data = at.getBlockData();
                        String dataStr = data.getAsString();
                        b.add(dataStr);
                        if (TARDISMaterials.not_glass.contains(material)) {
                            s.add(dataStr);
                            g.add(dataStr);
                        } else {
                            Material colour = StainedGlassLookup.stainedGlassFromMaterial(w, material);
                            s.add(colour.createBlockData().getAsString());
                            g.add(GLASS);
                        }
                    }
                    blueprint.add(b);
                    stained.add(s);
                    glass.add(g);
                }
                JsonObject custom = new JsonObject();
                custom.add("blueprint", blueprint);
                custom.add("stained", stained);
                custom.add("glass", glass);
                JsonArray sign = new JsonArray();
                sign.add(" ");
                sign.add(" ");
                custom.add("sign", sign);
                custom.addProperty("icon", "ENDER_CHEST");
                plugin.getTrackerKeeper().getPreset().remove(uuid);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PRESET_DONE", "custom_presets.json");
                // write to the custom presets json file
                File file = new File(TARDIS.plugin.getDataFolder() + File.separator + "custom_presets.json");
                try (FileReader reader = new FileReader(file)) {
                    JsonObject root = JsonParser.parseReader(new JsonReader(reader)).getAsJsonObject();
                    root.add(name, custom);
                    // write to file
                    try (FileWriter writer = new FileWriter(file)) {
                        new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(root, writer);
                        // load custom preset in game
                        EnumMap<COMPASS, ChameleonColumn> blueprints = new EnumMap<>(COMPASS.class);
                        EnumMap<COMPASS, ChameleonColumn> stains = new EnumMap<>(COMPASS.class);
                        EnumMap<COMPASS, ChameleonColumn> glasses = new EnumMap<>(COMPASS.class);
                        for (COMPASS d : COMPASS.values()) {
                            blueprints.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, blueprint));
                            stains.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, stained));
                            glasses.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, glass));
                        }
                        TARDISCustomPreset.CUSTOM_PRESETS.put(name, new CustomPreset(blueprints, stains, glasses, List.of(" ", " "), Material.ENDER_CHEST));
                    }
                } catch (IOException io) {
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not read custom presets file! " + io.getMessage());
                }
            }
        }
    }
}
