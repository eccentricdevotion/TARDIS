package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum SonicConfig {

	NOT_UPGRADED(1, Material.GRAY_WOOL),
	ENABLED(2, Material.LIME_WOOL),
	DISABLED(2, Material.RED_WOOL);

	private final int customModelData;
	private final Material material;

	SonicConfig(int customModelData, Material material) {
		this.customModelData = customModelData;
		this.material = material;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return TARDISStringUtils.sentenceCase(toString());
	}
}
