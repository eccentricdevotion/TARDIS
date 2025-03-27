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
package me.eccentric_nz.TARDIS.database.data;

import org.bukkit.NamespacedKey;

import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class Sonic {

    private final UUID uuid;
    private final boolean activated;
    private final List<Float> model;
    private final boolean bio;
    private final boolean diamond;
    private final boolean emerald;
    private final boolean redstone;
    private final boolean painter;
    private final boolean ignite;
    private final boolean arrow;
    private final boolean knockback;
    private final boolean brush;
    private final boolean conversion;

    public Sonic(UUID uuid, boolean activated, List<Float> model, boolean bio, boolean diamond, boolean emerald, boolean redstone, boolean painter, boolean ignite, boolean arrow, boolean knockback, boolean brush, boolean conversion) {
        this.uuid = uuid;
        this.activated = activated;
        this.model = model;
        this.bio = bio;
        this.diamond = diamond;
        this.emerald = emerald;
        this.redstone = redstone;
        this.painter = painter;
        this.ignite = ignite;
        this.arrow = arrow;
        this.knockback = knockback;
        this.brush = brush;
        this.conversion = conversion;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isActivated() {
        return activated;
    }

    public List<Float> getModel() {
        return model;
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

    public boolean hasBrush() {
        return brush;
    }

    public boolean hasConversion() {
        return conversion;
    }
}
