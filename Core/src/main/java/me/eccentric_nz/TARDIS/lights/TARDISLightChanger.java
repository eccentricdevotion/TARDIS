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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.VariableLight;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISLightChanger implements Runnable {

    private final TARDIS plugin;
    private final TardisLight light;
    private final boolean lightsOn;
    private final Material material;
    private final UUID uuid;
    List<Chunk> chunks = new ArrayList<>();
    int index = 0;
    int taskID;
    private BossBar bb;

    public TARDISLightChanger(TARDIS plugin, TardisLight light, String chunk, boolean lightsOn, Material material, Player player) {
        this.plugin = plugin;
        this.light = light;
        this.lightsOn = lightsOn;
        this.material = material;
        uuid = player.getUniqueId();
        // track player
        plugin.getTrackerKeeper().getLightChangers().add(uuid);
        // get the TARDIS console chunk
        String[] tc = chunk.split(":");
        int cx = TARDISNumberParsers.parseInt(tc[1]);
        int cz = TARDISNumberParsers.parseInt(tc[2]);
        World world = TARDISAliasResolver.getWorldFromAlias(tc[0]);
        if (world != null) {
            // get ARS chunks - 3 high x 9 wide x 9 deep
            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    Chunk c = world.getChunkAt(cx + x, cz + z);
                    // only add chunks with entities in them
                    if (c.getEntities().length > 0) {
                        chunks.add(c);
                    }
                }
            }
            // start a progress bar
            bb = Bukkit.createBossBar("TARDIS Light Changer Progress", BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
            bb.setProgress(0);
            bb.addPlayer(player);
            bb.setVisible(true);
        }
    }

    @Override
    public void run() {
        // process one chunk
        for (Entity e : chunks.get(index).getEntities()) {
            if (e instanceof Interaction interaction) {
                // is there a light block at this location?
                ItemDisplay display = TARDISDisplayItemUtils.get(interaction);
                if (display != null) {
                    TARDISDisplayItem tdi = TARDISDisplayItemUtils.get(display);
                    if (tdi != null && tdi.isLight()) {
                        Block block = interaction.getLocation().getBlock();
                        // remove the current light
                        block.setType(Material.AIR);
                        TARDISDisplayItemUtils.remove(block);
                        // set new light - delay as interaction may be removed by the above
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            if (light.getOn().isVariable()) {
                                new VariableLight(material, block.getLocation().add(0.5, 0.5, 0.5)).set(lightsOn ? light.getOn().getCustomModel() : light.getOff().getCustomModel(), lightsOn ? 15 : 0);
                            } else {
                                TARDISDisplayItemUtils.set(lightsOn ? light.getOn() : light.getOff(), block, -1);
                            }
                        }, 3L);
                    }
                }
            }
        }
        index++;
        double progress = index / (chunks.size() * 1.0d);
        bb.setProgress(progress);
        if (index > chunks.size() - 1) {
            // cancel task
            plugin.getServer().getScheduler().cancelTask(taskID);
            // remove progress bar
            bb.setProgress(1);
            bb.setVisible(false);
            bb.removeAll();
            plugin.getTrackerKeeper().getLightChangers().remove(uuid);
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
