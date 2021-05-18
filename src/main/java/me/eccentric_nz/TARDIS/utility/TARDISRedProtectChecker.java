package me.eccentric_nz.TARDIS.utility;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TARDISRedProtectChecker {

	/**
	 * Checks to see whether the specified location is within a GriefPrevention claim.
	 *
	 * @param player   the player to check access for.
	 * @param location the location to check.
	 * @return true or false depending on whether the player has rights to build within the region
	 */
	public static boolean canBuild(Player player, Location location) {
		boolean canbuild = true;
		Region region = RedProtect.get().getRegionManager().getTopRegion(location);
		if (!region.canBuild(player)) {
			canbuild = false;
		}
		return canbuild;
	}

	public static boolean canSonic(Player player, Block block) {
		boolean cansonic;
		Region region = RedProtect.get().getRegionManager().getTopRegion(block.getLocation());
		switch (block.getType()) {
			case ENDER_CHEST:
				cansonic = region.canEnderChest(player);
				break;
			case CHEST:
			case TRAPPED_CHEST:
				cansonic = region.canChest(player);
				break;
			case LEVER:
				cansonic = region.canLever(player);
				break;
			case DETECTOR_RAIL:
			case MUSHROOM_STEM:
			case PISTON:
			case POLISHED_BLACKSTONE_BUTTON:
			case POWERED_RAIL:
			case REDSTONE_LAMP:
			case REDSTONE_WIRE:
			case STICKY_PISTON:
				cansonic = region.canRedstone(player);
				break;
			case ACACIA_BUTTON:
			case BIRCH_BUTTON:
			case CRIMSON_BUTTON:
			case DARK_OAK_BUTTON:
			case JUNGLE_BUTTON:
			case OAK_BUTTON:
			case SPRUCE_BUTTON:
			case STONE_BUTTON:
			case WARPED_BUTTON:
				cansonic = region.canButton(player);
				break;
			case ACACIA_DOOR:
			case ACACIA_TRAPDOOR:
			case BIRCH_DOOR:
			case BIRCH_TRAPDOOR:
			case CRIMSON_DOOR:
			case CRIMSON_TRAPDOOR:
			case DARK_OAK_DOOR:
			case DARK_OAK_TRAPDOOR:
			case IRON_DOOR:
			case IRON_TRAPDOOR:
			case JUNGLE_DOOR:
			case JUNGLE_TRAPDOOR:
			case OAK_DOOR:
			case OAK_TRAPDOOR:
			case SPRUCE_DOOR:
			case SPRUCE_TRAPDOOR:
			case WARPED_DOOR:
			case WARPED_TRAPDOOR:
				cansonic = region.canDoor(player);
				break;
			case CAMPFIRE:
			case NETHERRACK:
			case OBSIDIAN:
			case SOUL_CAMPFIRE:
			case SOUL_SAND:
			case SOUL_SOIL:
				cansonic = region.canFire();
				break;
			default:
				cansonic = region.canBreak(block.getType());
				break;
		}
		return cansonic;
	}

	public static boolean shouldToggleDoor(Block block) {
		Region region = RedProtect.get().getRegionManager().getTopRegion(block.getLocation());
		if (region == null) {
			return true;
		}
		return !region.getFlagBool("smart-door");
	}
}
