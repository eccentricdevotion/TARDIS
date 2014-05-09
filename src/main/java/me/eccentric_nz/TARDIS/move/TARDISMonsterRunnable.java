/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMonsterRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<EntityType> monsters = new ArrayList<EntityType>();

    public TARDISMonsterRunnable(TARDIS plugin) {
        this.plugin = plugin;
        monsters.add(EntityType.CAVE_SPIDER);
        monsters.add(EntityType.CREEPER);
        monsters.add(EntityType.ENDERMAN);
        monsters.add(EntityType.PIG_ZOMBIE);
        monsters.add(EntityType.SILVERFISH);
        monsters.add(EntityType.SKELETON);
        monsters.add(EntityType.SLIME);
        monsters.add(EntityType.SPIDER);
        monsters.add(EntityType.WITCH);
        monsters.add(EntityType.ZOMBIE);
    }

    @Override
    public void run() {
        // get open portals
        for (Map.Entry<Location, TARDISTeleportLocation> map : plugin.getTrackerKeeper().getTrackPortals().entrySet()) {
            // only portals in police box worlds
            if (!map.getKey().getWorld().getName().contains("TARDIS")) {
                Entity ent = map.getKey().getWorld().spawnEntity(map.getKey(), EntityType.EXPERIENCE_ORB);
                List<Entity> entities = ent.getNearbyEntities(16, 16, 16);
                ent.remove();
                for (Entity e : entities) {
                    EntityType type = e.getType();
                    TARDISMonster tm = new TARDISMonster();
                    if (monsters.contains(type)) {
                        switch (type) {
                            case CAVE_SPIDER:

                                break;
                            case CREEPER:
                                Creeper creeper = (Creeper) e;
                                if (creeper.isPowered()) {
                                    tm.setCharged(true);
                                }
                                break;
                            case ENDERMAN:
                                Enderman enderman = (Enderman) e;
                                if (enderman.getCanPickupItems() && enderman.getCarriedMaterial() != null) {
                                    tm.setCarried(enderman.getCarriedMaterial());
                                }
                                break;
                            case PIG_ZOMBIE:
                                PigZombie pigzombie = (PigZombie) e;
                                if (pigzombie.isAngry()) {
                                    tm.setAggressive(true);
                                    tm.setAnger(pigzombie.getAnger());
                                }
                                break;
                            case SILVERFISH:

                                break;
                            case SKELETON:

                                break;
                            case SLIME:

                                break;
                            case SPIDER:

                                break;
                            case WITCH:

                                break;
                            case ZOMBIE:

                                break;
                        }
                    }
                }
            }
        }
    }
}
