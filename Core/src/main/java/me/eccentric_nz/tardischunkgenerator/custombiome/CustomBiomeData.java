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
package me.eccentric_nz.tardischunkgenerator.custombiome;

import net.minecraft.core.particles.SimpleParticleType;

public class CustomBiomeData {

    private final String minecraftName;
    private final String customName;
    private final float temperature;
    private final float downfall;
    private final int fogColour;
    private final int waterColour;
    private final int waterFogColour;
    private final int skyColour;
    private final int foliageColour;
    private final int grassColour;
    private final SimpleParticleType particle;
    private final float ambience;
    private final boolean frozen;

    public CustomBiomeData(String minecraftName, String customName, float temperature, float downfall, int fogColour, int waterColour, int waterFogColour, int skyColour, int foliageColour, int grassColour, SimpleParticleType particle, float ambience, boolean frozen) {
        this.minecraftName = minecraftName;
        this.customName = customName;
        this.temperature = temperature;
        this.downfall = downfall;
        this.fogColour = fogColour;
        this.waterColour = waterColour;
        this.waterFogColour = waterFogColour;
        this.skyColour = skyColour;
        this.foliageColour = foliageColour;
        this.grassColour = grassColour;
        this.particle = particle;
        this.ambience = ambience;
        this.frozen = frozen;
    }

    public static int fromHex(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public String getCustomName() {
        return customName;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getDownfall() {
        return downfall;
    }

    public int getFogColour() {
        return fogColour;
    }

    public int getWaterColour() {
        return waterColour;
    }

    public int getWaterFogColour() {
        return waterFogColour;
    }

    public int getSkyColour() {
        return skyColour;
    }

    public int getFoliageColour() {
        return foliageColour;
    }

    public int getGrassColour() {
        return grassColour;
    }

    public SimpleParticleType getParticle() {
        return particle;
    }

    public float getAmbience() {
        return ambience;
    }

    public boolean isFrozen() {
        return frozen;
    }
}
