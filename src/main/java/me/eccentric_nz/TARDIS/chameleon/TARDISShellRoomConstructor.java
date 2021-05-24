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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.advanced.TARDISCircuitChecker;
import me.eccentric_nz.tardis.advanced.TARDISCircuitDamager;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetChameleon;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISMaterials;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISShellRoomConstructor {

	private final TARDISPlugin plugin;
	private final int[] orderx;
	private final int[] orderz;
	private final String GLASS = addQuotes(TARDISConstants.GLASS.getAsString());
	private Block sign;

	public TARDISShellRoomConstructor(TARDISPlugin plugin) {
		this.plugin = plugin;
		orderx = new int[]{0, 1, 2, 2, 2, 1, 0, 0, 1, -1};
		orderz = new int[]{0, 0, 0, 1, 2, 2, 2, 1, 1, 1};
	}

	public void createShell(Player player, int id, Block block) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
		if (rs.resultSet()) {
			Location block_loc = block.getLocation();
			World w = block_loc.getWorld();
			int fx = block_loc.getBlockX() + 2;
			int fy = block_loc.getBlockY() + 1;
			int fz = block_loc.getBlockZ() - 1;
			// do a check to see if there are actually any blocks there
			// must be at least one block and one door
			boolean hasBlock = false;
			boolean hasDoor = false;
			for (int c = 0; c < 10; c++) {
				for (int y = fy; y < fy + 4; y++) {
					assert w != null;
					Block fb = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
					if (!fb.getType().isAir()) {
						if (TARDISMaterials.doors.contains(fb.getType())) {
							hasDoor = true;
						} else {
							hasBlock = true;
						}
					}
				}
			}
			if (!hasBlock && !hasDoor) {
				TARDISMessage.send(player, "SHELL_MIN_BLOCKS");
				return;
			}
			TARDIS tardis = rs.getTardis();
			TARDISMessage.send(player, "PRESET_SCAN");
			StringBuilder sb_blue_data = new StringBuilder("[");
			StringBuilder sb_stain_data = new StringBuilder("[");
			StringBuilder sb_glass_data = new StringBuilder("[");
			for (int c = 0; c < 10; c++) {
				sb_blue_data.append("[");
				sb_stain_data.append("[");
				sb_glass_data.append("[");
				for (int y = fy; y < (fy + 4); y++) {
					Block b = w.getBlockAt(fx + orderx[c], y, fz + orderz[c]);
					Material material = b.getType();
					BlockData data = b.getBlockData();
					String dataStr = addQuotes(data.getAsString());
					if (Tag.WALL_SIGNS.isTagged(material)) {
						sign = b;
					}
					if (y == (fy + 3)) {
						sb_blue_data.append(addQuotes(data.getAsString()));
						if (TARDISMaterials.not_glass.contains(material)) {
							sb_stain_data.append(dataStr);
							sb_glass_data.append(dataStr);
						} else {
							Material colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material);
							sb_stain_data.append(addQuotes(colour.createBlockData().getAsString()));
							sb_glass_data.append(GLASS);
						}
					} else {
						sb_blue_data.append(addQuotes(data.getAsString())).append(",");
						if (TARDISMaterials.not_glass.contains(material)) {
							sb_stain_data.append(dataStr).append(",");
							sb_glass_data.append(dataStr).append(",");
						} else {
							Material colour = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material);
							sb_stain_data.append(addQuotes(colour.createBlockData().getAsString())).append(",");
							sb_glass_data.append(GLASS).append(",");
						}
					}
				}
				if (c == 9) {
					sb_blue_data.append("]");
					sb_stain_data.append("]");
					sb_glass_data.append("]");
				} else {
					sb_blue_data.append("],");
					sb_stain_data.append("],");
					sb_glass_data.append("],");
				}
			}
			sb_blue_data.append("]");
			sb_stain_data.append("]");
			sb_glass_data.append("]");
			String jsonBlue = sb_blue_data.toString();
			String jsonStain = sb_stain_data.toString();
			String jsonGlass = sb_glass_data.toString();
			// save chameleon construct
			HashMap<String, Object> wherec = new HashMap<>();
			wherec.put("tardis_id", id);
			ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
			HashMap<String, Object> set = new HashMap<>();
			set.put("blueprintData", jsonBlue);
			set.put("stainData", jsonStain);
			set.put("glassData", jsonGlass);
			// read the sign
			if (sign != null) {
				Sign s = (Sign) sign.getState();
				String s1 = (s.getLine(0).contains("&PLAYER")) ? player.getName() + "'s" : s.getLine(0);
				String s2 = (s.getLine(1).contains("&PLAYER")) ? player.getName() + "'s" : s.getLine(1);
				String s3 = (s.getLine(2).contains("&PLAYER")) ? player.getName() + "'s" : s.getLine(2);
				String s4 = (s.getLine(3).contains("&PLAYER")) ? player.getName() + "'s" : s.getLine(3);
				set.put("line1", s1);
				set.put("line2", s2);
				set.put("line3", s3);
				set.put("line4", s4);
			}
			if (rsc.resultSet()) {
				// update
				HashMap<String, Object> whereu = new HashMap<>();
				whereu.put("tardis_id", id);
				plugin.getQueryFactory().doUpdate("chameleon", set, whereu);
			} else {
				// insert
				set.put("tardis_id", id);
				plugin.getQueryFactory().doInsert("chameleon", set);
			}
			buildConstruct(tardis.getPreset().toString(), id, player);
		}
	}

	private void buildConstruct(String preset, int id, Player player) {
		// take their artron energy
		HashMap<String, Object> wheree = new HashMap<>();
		wheree.put("tardis_id", id);
		plugin.getQueryFactory().alterEnergyLevel("tardis", plugin.getArtronConfig().getInt("shell") * -1, wheree, player);
		TARDISMessage.send(player, "PRESET_CONSTRUCTED");
		// update tardis table
		HashMap<String, Object> sett = new HashMap<>();
		sett.put("chameleon_preset", "CONSTRUCT");
		sett.put("chameleon_demat", preset);
		HashMap<String, Object> wheret = new HashMap<>();
		wheret.put("tardis_id", id);
		plugin.getQueryFactory().doUpdate("tardis", sett, wheret);
		// update chameleon sign
		HashMap<String, Object> whereh = new HashMap<>();
		whereh.put("tardis_id", id);
		whereh.put("type", 31);
		ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
		if (rsc.resultSet()) {
			for (HashMap<String, String> map : rsc.getData()) {
				TARDISStaticUtils.setSign(map.get("location"), 3, "CONSTRUCT", player);
			}
		}
		new TARDISChameleonFrame(plugin).updateChameleonFrame(id, PRESET.CONSTRUCT);
		TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Construct");
		// rebuild
		player.performCommand("tardis rebuild");
		// damage the circuit if configured
		if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
			TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
			tcc.getCircuits();
			// decrement uses
			int uses_left = tcc.getChameleonUses();
			new TARDISCircuitDamager(plugin, DiskCircuit.CHAMELEON, uses_left, id, player).damage();
		}
	}

	private String addQuotes(String s) {
		return "\"" + s + "\"";
	}
}
