/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISEntityTracker {

    private final TARDIS plugin;

    public TARDISEntityTracker(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addNPCs(Location exterior, Location interior, UUID uuid) {
        List<Entity> ents = TARDISScannerListener.getNearbyEntities(exterior, 6);
        List<Integer> npcids = new ArrayList<>();
        for (Entity e : ents) {
            if (e instanceof LivingEntity) {
                // create NPC
                NPCRegistry registry = CitizensAPI.getNPCRegistry();
                String name = (e instanceof Player) ? e.getName() : "";
                NPC npc = registry.createNPC(e.getType(), name);
                // determine relative location
                double relx = e.getLocation().getX() - exterior.getX();
                double rely = e.getLocation().getY() - exterior.getY();
                double relz = e.getLocation().getZ() - exterior.getZ();
                double adjx = interior.getX() + relx;
                double adjy = interior.getY() + rely;
                double adjz = interior.getZ() + relz;
                Location l = new Location(interior.getWorld(), adjx, adjy, adjz);
                plugin.setTardisSpawn(true);
                npc.spawn(l);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "npc select " + npc.getId());
                npcids.add(npc.getId());
                if (npc.isSpawned()) {
                    switch (e.getType()) {
                        case DROWNED:
                        case HUSK:
                        case ZOMBIE:
                            Zombie z = (Zombie) e;
                            if (z.isBaby()) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "npc zombiemod -b");
                            }
                            break;
                        case ZOMBIE_VILLAGER:
                            plugin.getServer().dispatchCommand(plugin.getConsole(), "npc zombiemod -v");
                            break;
                        case CHICKEN:
                        case COW:
                        case DONKEY:
                        case HORSE:
                        case LLAMA:
                        case MULE:
                        case OCELOT:
                        case PARROT:
                        case PIG:
                        case POLAR_BEAR:
                        case RABBIT:
                        case SHEEP:
                        case VILLAGER:
                        case WOLF:
                            Ageable a = (Ageable) e;
                            if (!a.isAdult()) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "npc age baby");
                            }
                            break;
                        default:
                            break;
                    }
                    npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, true);
                    npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
                    npc.data().set(NPC.DAMAGE_OTHERS_METADATA, false);
                }
            }
        }
        if (npcids.size() > 0) {
            plugin.getTrackerKeeper().getRenderedNPCs().put(uuid, npcids);
        }
    }

    public void removeNPCs(UUID uuid) {
        plugin.getTrackerKeeper().getRenderedNPCs().get(uuid).forEach((i) -> {
            NPCRegistry registry = CitizensAPI.getNPCRegistry();
            NPC npc = registry.getById(i);
            if (npc != null) {
                npc.destroy();
            }
        });
        plugin.getTrackerKeeper().getRenderedNPCs().remove(uuid);
    }
}
