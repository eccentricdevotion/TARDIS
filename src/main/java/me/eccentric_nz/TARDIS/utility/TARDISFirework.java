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
 *
 * Rocket code borrowed from com.darkblade12.itemslotmachine.rocket.Rocket.java
 */
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TARDISFirework {

	private static final Random RANDOM = new Random();
	private static final Color[] COLORS;

	static {
		DyeColor[] values = DyeColor.values();
		COLORS = new Color[values.length];
		for (int i = 0; i < values.length; i++) {
			COLORS[i] = values[i].getFireworkColor();
		}
	}

	private final FireworkMeta meta;

	private TARDISFirework(FireworkMeta meta, boolean isClean) {
		if (isClean) {
			this.meta = meta;
		} else {
			this.meta = getCleanMeta();
			this.meta.setPower(meta.getPower());
			this.meta.addEffects(meta.getEffects());
		}
	}

	public static TARDISFirework randomize() {
		FireworkMeta meta = getCleanMeta();
		meta.setPower(RANDOM.nextInt(3) + 1);
		meta.addEffects(randomizeEffects(1, 2, 1, 2));
		return new TARDISFirework(meta, true);
	}

	private static int calculateRandom(int min, int max) throws IllegalArgumentException {
		if (min < 0) {
			throw new IllegalArgumentException("Min value can not be lower than 0");
		} else if (max < 1) {
			throw new IllegalArgumentException("Min value can not be lower than 1");
		} else if (max < min) {
			throw new IllegalArgumentException("Max value can not be lower than min value");
		}
		return min == max ? min : RANDOM.nextInt((max - min) + 1) + min;
	}

	private static List<Color> randomizeColors(int min, int max) {
		List<Color> colors = new ArrayList<>();
		for (int a = 1; a <= calculateRandom(min, max); a++) {
			Color c = COLORS[RANDOM.nextInt(COLORS.length)];
			if (!colors.contains(c)) {
				colors.add(c);
			}
		}
		return colors;
	}

	private static List<FireworkEffect> randomizeEffects(int min, int max, int minColors, int maxColors) {
		List<FireworkEffect> effects = new ArrayList<>();
		for (int a = 1; a <= calculateRandom(min, max); a++) {
			effects.add(FireworkEffect.builder().flicker(RANDOM.nextBoolean()).with(Type.values()[RANDOM.nextInt(Type.values().length)]).trail(RANDOM.nextBoolean()).withColor(randomizeColors(minColors, maxColors)).withFade(randomizeColors(minColors, maxColors)).build());
		}
		return effects;
	}

	private static FireworkMeta getCleanMeta() {
		return (FireworkMeta) new ItemStack(Material.FIREWORK_ROCKET).getItemMeta();
	}

	private Firework launch(Location l) {
		Firework f = Objects.requireNonNull(l.getWorld()).spawn(l, Firework.class);
		f.setFireworkMeta(meta);
		return f;
	}

	public void displayEffects(TARDISPlugin plugin, Location l) {
		Firework f = launch(l);
		new BukkitRunnable() {
			@Override
			public void run() {
				f.detonate();
			}
		}.runTaskLater(plugin, 1);
	}
}