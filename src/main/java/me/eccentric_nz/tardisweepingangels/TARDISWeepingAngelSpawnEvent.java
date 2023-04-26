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
 */package me.eccentric_nz.tardisweepingangels;

import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public final class TARDISWeepingAngelSpawnEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity entity;
    private final EntityType entityType;
    private final Monster monsterType;
    private final Location location;

    public TARDISWeepingAngelSpawnEvent(Entity entity, EntityType entityType, Monster monsterType, Location location) {
        this.entity = entity;
        this.entityType = entityType;
        this.monsterType = monsterType;
        this.location = location;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Returns the Entity involved in this event
     *
     * @return Entity that is involved in this event
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets the EntityType of the Entity involved in this event.
     *
     * @return EntityType of the Entity involved in this event
     */
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Gets the the Monster type for the TARDISWeepingAngels entity involved in this event.
     *
     * @return Monster of the TARDISWeepingAngels entity involved in this event
     */
    public Monster getMonsterType() {
        return monsterType;
    }

    /**
     * Gets the location at which the monster is spawning.
     *
     * @return The location at which the monster is spawning
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
