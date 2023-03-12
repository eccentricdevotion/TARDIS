/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.move;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author macgeek
 */
public class MonsterMoveListener implements Listener {

    private final HashMap<UUID, MoveSession> moveSessions = new HashMap<>();

    @EventHandler
    public void onMonsterMove(EntityMoveEvent event) {
        Entity entity = event.getEntity();
        if (MonsterEquipment.isAnimatedMonster(entity)) {
            // get or create a move session
            MoveSession tms = getMoveSession(entity);
            tms.setStaleLocation(entity.getLocation());
            // get the entity's equipment
            EntityEquipment ee = ((LivingEntity) entity).getEquipment();
            ItemStack helmet = ee.getHelmet();
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta != null && meta.hasCustomModelData()) {
                    boolean hasChanged = false;
                    int cmd = meta.getCustomModelData();
                    // if the location is stale, ie: the entity isn't actually moving xyz coords, they're looking around
                    if (tms.isStaleLocation() || !event.hasChangedPosition()) {
                        if (!entity.getPassengers().isEmpty()) {
                            Guardian guardian = (Guardian) entity.getPassengers().get(0);
                            // show animated ATTACKING model - silent doesn't move when beaming
                            if (guardian.hasLaser() && cmd != 11) {
                                meta.setCustomModelData(11);
                                hasChanged = true;
                            }
                        } else {
                            // show static model
                            if (cmd != 9) {
                                meta.setCustomModelData(9);
                                hasChanged = true;
                            }
                        }
                        // the entity is actually moving
                    } else {
                        Monster monster = (Monster) entity;
                        // is the entity in water
                        if (monster instanceof Drowned && monster.getEyeLocation().getBlock().isLiquid()) {
                            if (cmd != 12) {
                                meta.setCustomModelData(12);
                                hasChanged = true;
                            }
                        } else {
                            if (monster.getTarget() != null) {
                                // show animated ATTACKING model
                                if (cmd != 11) {
                                    meta.setCustomModelData(11);
                                    hasChanged = true;
                                }
                            } else {
                                // show animated WALKING model
                                if (cmd != 10) {
                                    meta.setCustomModelData(10);
                                    hasChanged = true;
                                }
                            }
                        }
                    }
                    if (hasChanged) {
                        helmet.setItemMeta(meta);
                        ee.setHelmet(helmet);
                    }
                }
            }
        }
    }

    /**
     * Gets the Move Session for a monster, this is used to see if they have actually moved
     *
     * @param entity the monster to track
     * @return the session for the monster
     */
    public MoveSession getMoveSession(Entity entity) {
        if (moveSessions.containsKey(entity.getUniqueId())) {
            return moveSessions.get(entity.getUniqueId());
        }
        MoveSession session = new MoveSession(entity);
        moveSessions.put(entity.getUniqueId(), session);
        return session;
    }
}
