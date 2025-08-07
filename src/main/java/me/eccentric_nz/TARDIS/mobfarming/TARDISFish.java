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
package me.eccentric_nz.TARDIS.mobfarming;

import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TropicalFish;

public class TARDISFish extends TARDISMob {

    public TARDISFish() {
        super.setType(EntityType.TROPICAL_FISH);
    }

    private TropicalFish.Pattern pattern;
    private DyeColor bodyColour;
    private DyeColor patternColour;

    public TropicalFish.Pattern getPattern() {
        return pattern;
    }

    public void setPattern(TropicalFish.Pattern pattern) {
        this.pattern = pattern;
    }

    public DyeColor getBodyColour() {
        return bodyColour;
    }

    public void setBodyColour(DyeColor bodyColour) {
        this.bodyColour = bodyColour;
    }

    DyeColor getPatternColour() {
        return patternColour;
    }

    void setPatternColour(DyeColor patternColour) {
        this.patternColour = patternColour;
    }
}
