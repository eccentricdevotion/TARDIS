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

import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;

import java.util.UUID;

public class StandbyData {

    private final int level;
    private final UUID uuid;
    private final boolean hidden;
    private final boolean lights;
    private final ChameleonPreset preset;
    private final TardisLight lightType;

    public StandbyData(int level, UUID uuid, boolean hidden, boolean lights, ChameleonPreset preset, TardisLight lightType) {
        this.level = level;
        this.uuid = uuid;
        this.hidden = hidden;
        this.lights = lights;
        this.preset = preset;
        this.lightType = lightType;
    }

    public int getLevel() {
        return level;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isLights() {
        return lights;
    }

    public ChameleonPreset getPreset() {
        return preset;
    }

    public TardisLight getLightType() {
        return lightType;
    }
}
