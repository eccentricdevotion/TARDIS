/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.data;

import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class Sonic {

    private final UUID uuid;
    private final boolean activated;
    private final ChatColor sonicType;
    private final int customModelData;
    private final boolean bio;
    private final boolean diamond;
    private final boolean emerald;
    private final boolean redstone;
    private final boolean painter;
    private final boolean ignite;
    private final boolean arrow;
    private final boolean knockback;

    public Sonic(UUID uuid, boolean activated, ChatColor sonicType, int customModelData, boolean bio, boolean diamond, boolean emerald, boolean redstone, boolean painter, boolean ignite, boolean arrow, boolean knockback) {
        this.uuid = uuid;
        this.activated = activated;
        this.sonicType = sonicType;
        this.customModelData = customModelData;
        this.bio = bio;
        this.diamond = diamond;
        this.emerald = emerald;
        this.redstone = redstone;
        this.painter = painter;
        this.ignite = ignite;
        this.arrow = arrow;
        this.knockback = knockback;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isActivated() {
        return activated;
    }

    public ChatColor getSonicType() {
        return sonicType;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean hasBio() {
        return bio;
    }

    public boolean hasDiamond() {
        return diamond;
    }

    public boolean hasEmerald() {
        return emerald;
    }

    public boolean hasRedstone() {
        return redstone;
    }

    public boolean hasPainter() {
        return painter;
    }

    public boolean hasIgnite() {
        return ignite;
    }

    public boolean hasArrow() {
        return arrow;
    }

    public boolean hasKnockback() {
        return knockback;
    }
}
