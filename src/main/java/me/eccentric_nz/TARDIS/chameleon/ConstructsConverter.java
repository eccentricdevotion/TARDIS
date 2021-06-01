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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardis.database.converters.TARDISMaterialIDConverter;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsConverter {

	private final TARDISPlugin plugin;
	private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
	private final Connection connection = service.getConnection();
	private final String prefix;

	public ConstructsConverter(TARDISPlugin plugin) {
		this.plugin = plugin;
		prefix = this.plugin.getPrefix();
	}

	public void convertConstructs() {
		TARDISMaterialIDConverter tmic = new TARDISMaterialIDConverter(plugin);
		PreparedStatement query = null;
		PreparedStatement update = null;
		ResultSet rs = null;
		int i = 0;
		try {
			service.testConnection(connection);
			connection.setAutoCommit(false);
			query = connection.prepareStatement(
					"SELECT chameleon_id, blueprintID, blueprintData, glassID FROM " + prefix + "chameleon");
			update = connection.prepareStatement("UPDATE " + prefix +
												 "chameleon set blueprintID = '', blueprintData = ?, stainID = '', stainData = ?, glassID = '', glassData = ? WHERE chameleon_id = ?");
			rs = query.executeQuery();
			Pattern p = Pattern.compile("^\\[\\[[0-9]+");
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					int id = rs.getInt("chameleon_id");
					String bpID = rs.getString("blueprintID");
					String bpData = rs.getString("blueprintData");
					String glID = rs.getString("glassID");
					if (bpID != null) {
						Matcher m = p.matcher(bpID);
						if (!m.find()) {
							// no IDs found, already converted
							continue;
						}
						String[][] bpGrid = new String[10][4];
						String[][] stGrid = new String[10][4];
						String[][] glGrid = new String[10][4];
						JsonArray bpIDJson = JsonParser.parseString(bpID).getAsJsonArray();
						JsonArray bpDataJson = JsonParser.parseString(bpData).getAsJsonArray();
						JsonArray glIDJson = JsonParser.parseString(glID).getAsJsonArray();
						for (int y = 0; y < 10; y++) {
							JsonArray bpIDX = bpIDJson.get(y).getAsJsonArray();
							JsonArray bpDATAX = bpDataJson.get(y).getAsJsonArray();
							JsonArray glIDX = glIDJson.get(y).getAsJsonArray();
							for (int x = 0; x < 4; x++) {
								Material material = tmic.LEGACY_ID_LOOKUP.get(bpIDX.get(x).getAsInt());
								bpGrid[y][x] = material.createBlockData().getAsString();
								switch (material) {
									case WHITE_CARPET:
									case WHITE_STAINED_GLASS:
									case WHITE_STAINED_GLASS_PANE:
									case WHITE_WOOL:
									case GREEN_TERRACOTTA:
										// get correct colour
										String[] split = material.toString().split("_");
										split[0] = tmic.COLOUR_LOOKUP.get(bpDATAX.get(x).getAsInt());
										StringBuilder sb = new StringBuilder();
										for (int s = 0; s < split.length; s++) {
											sb.append(split[s]);
											if (s != split.length - 1) {
												sb.append("_");
											}
										}
										String implode = sb.toString();
										material = Material.valueOf(implode);
										bpGrid[y][x] = material.createBlockData().getAsString();
										stGrid[y][x] = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).createBlockData().getAsString();
										break;
									case ACACIA_DOOR:
									case BIRCH_DOOR:
									case DARK_OAK_DOOR:
									case IRON_DOOR:
									case JUNGLE_DOOR:
									case OAK_DOOR:
									case SPRUCE_DOOR:
										BlockData dbd = material.createBlockData();
										Door door = (Door) dbd;
										// set facing / hinge
										door.setFacing(BlockFace.EAST);
										door.setHinge(Door.Hinge.RIGHT);
										if (x == 0) {
											// bottom door
											door.setHalf(Bisected.Half.BOTTOM);
										}
										if (x == 1) {
											// top door
											door.setHalf(Bisected.Half.TOP);
										}
										String doorData = door.getAsString();
										bpGrid[y][x] = doorData;
										stGrid[y][x] = doorData;
										break;
									case ACACIA_SIGN:
									case ACACIA_WALL_SIGN:
									case AIR:
									case BIRCH_SIGN:
									case BIRCH_WALL_SIGN:
									case DARK_OAK_SIGN:
									case DARK_OAK_WALL_SIGN:
									case JUNGLE_SIGN:
									case JUNGLE_WALL_SIGN:
									case OAK_SIGN:
									case OAK_WALL_SIGN:
									case REDSTONE_TORCH:
									case REDSTONE_WALL_TORCH:
									case SPRUCE_SIGN:
									case SPRUCE_WALL_SIGN:
									case TORCH:
									case WALL_TORCH:
										stGrid[y][x] = tmic.LEGACY_ID_LOOKUP.get(bpIDX.get(x).getAsInt()).createBlockData().getAsString();
										break;
									default:
										try {
											stGrid[y][x] = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).createBlockData().getAsString();
										} catch (NullPointerException ex) {
											plugin.debug(material.toString());
										}
										break;
								}
								glGrid[y][x] = tmic.LEGACY_ID_LOOKUP.get(glIDX.get(x).getAsInt()).createBlockData().getAsString();
							}
						}
						Gson gson = new GsonBuilder().disableHtmlEscaping().create();
						JsonArray bpArr = JsonParser.parseString(gson.toJson(bpGrid)).getAsJsonArray();
						JsonArray stArr = JsonParser.parseString(gson.toJson(stGrid)).getAsJsonArray();
						JsonArray glArr = JsonParser.parseString(gson.toJson(glGrid)).getAsJsonArray();
						update.setString(1, bpArr.toString());
						update.setString(2, stArr.toString());
						update.setString(3, glArr.toString());
						update.setInt(4, id);
						update.addBatch();
						i++;
					}
				}
			}
			if (i > 0) {
				update.executeBatch();
				connection.commit();
				plugin.getConsole().sendMessage(
						plugin.getPluginName() + "Converted " + i + " Chameleon Construct records");
			}
			plugin.getConfig().set("conversions.constructs", true);
			plugin.saveConfig();
		} catch (SQLException ex) {
			plugin.debug("ResultSet error for chameleon table! " + ex.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (query != null) {
					query.close();
				}
				if (update != null) {
					update.close();
				}
			} catch (SQLException e) {
				plugin.debug("Error closing chameleon table! " + e.getMessage());
			}
		}
	}
}
