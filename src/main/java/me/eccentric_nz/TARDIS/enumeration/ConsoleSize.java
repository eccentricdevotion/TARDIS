/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum ConsoleSize {

	SMALL("small", "16 x 16 x 16 blocks", 600L),
	MEDIUM("medium", "32 x 16 x 32 blocks", 1460L),
	TALL("tall", "32 x 32 x 32 blocks", 3072L),
	MASSIVE("massive", "48 x 32 x 48 blocks", 3760L);

	private final String configPath;
	private final String blocks;
	private final long delay;

	ConsoleSize(String configPath, String blocks, long delay) {
		this.configPath = configPath;
		this.blocks = blocks;
		this.delay = delay;
	}

	public static ConsoleSize getByWidthAndHeight(int w, int h) {
		if (w < 17 && h < 17) {
			return SMALL;
		} else if (w > 16 && h < 17) {
			return MEDIUM;
		} else if (w < 48) {
			return TALL;
		} else {
			return MASSIVE;
		}
	}

	public String getConfigPath() {
		return configPath;
	}

	public String getBlocks() {
		return blocks;
	}

	public long getDelay() {
		return delay;
	}
}
