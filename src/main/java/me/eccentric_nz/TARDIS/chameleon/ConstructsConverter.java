package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.TARDISMaterialIDConverter;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsConverter {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;

    public ConstructsConverter(TARDIS plugin) {
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
            query = connection.prepareStatement("SELECT chameleon_id, blueprintID, blueprintData, glassID FROM " + prefix + "chameleon");
            update = connection.prepareStatement("UPDATE " + prefix + "chameleon set blueprintID = '', blueprintData = ?, stainID = '', stainData = ?, glassID = '', glassData = ? WHERE chameleon_id = ?");
            rs = query.executeQuery();
            Pattern p = Pattern.compile("^\\[\\[[0-9]+");
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("chameleon_id");
                    String bpID = rs.getString("blueprintID");
                    String bpData = rs.getString("blueprintData");
                    String glID = rs.getString("glassID");
                    Matcher m = p.matcher(bpID);
                    if (!m.find()) {
                        // no IDs found, already converted
                        plugin.debug("doesn't match");
                        continue;
                    }
                    String[][] bpGrid = new String[10][4];
                    String[][] stGrid = new String[10][4];
                    String[][] glGrid = new String[10][4];
                    JSONArray bpIDJson = new JSONArray(bpID);
                    JSONArray bpDataJson = new JSONArray(bpData);
                    JSONArray glIDJson = new JSONArray(glID);
                    for (int y = 0; y < 10; y++) {
                        JSONArray bpIDX = bpIDJson.getJSONArray(y);
                        JSONArray bpDATAX = bpDataJson.getJSONArray(y);
                        JSONArray glIDX = glIDJson.getJSONArray(y);
                        for (int x = 0; x < 4; x++) {
                            Material material = tmic.LEGACY_ID_LOOKUP.get(bpIDX.getInt(x));
                            bpGrid[y][x] = material.createBlockData().getAsString();
                            switch (material) {
                                case WHITE_CARPET:
                                case WHITE_STAINED_GLASS:
                                case WHITE_STAINED_GLASS_PANE:
                                case WHITE_WOOL:
                                case GREEN_TERRACOTTA:
                                    // get correct colour
                                    String[] split = material.toString().split("_");
                                    split[0] = tmic.COLOUR_LOOKUP.get(bpDATAX.getInt(x));
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
                                case AIR:
                                case BIRCH_DOOR:
                                case DARK_OAK_DOOR:
                                case IRON_DOOR:
                                case JUNGLE_DOOR:
                                case OAK_DOOR:
                                case REDSTONE_TORCH:
                                case REDSTONE_WALL_TORCH:
                                case SIGN:
                                case SPRUCE_DOOR:
                                case TORCH:
                                case WALL_SIGN:
                                case WALL_TORCH:
                                    stGrid[y][x] = tmic.LEGACY_ID_LOOKUP.get(bpIDX.getInt(x)).createBlockData().getAsString();
                                    break;
                                default:
                                    try {
                                        stGrid[y][x] = plugin.getBuildKeeper().getStainedGlassLookup().getStain().get(material).createBlockData().getAsString();
                                    } catch (NullPointerException ex) {
                                        plugin.debug(material.toString());
                                    }
                                    break;
                            }
                            glGrid[y][x] = tmic.LEGACY_ID_LOOKUP.get(glIDX.getInt(x)).createBlockData().getAsString();
                        }
                    }
                    JSONArray bpArr = new JSONArray(bpGrid);
                    JSONArray stArr = new JSONArray(stGrid);
                    JSONArray glArr = new JSONArray(glGrid);
                    update.setString(1, bpArr.toString());
                    update.setString(2, stArr.toString());
                    update.setString(3, glArr.toString());
                    update.setInt(4, id);
                    update.addBatch();
                    i++;
                }
            }
            if (i > 0) {
                update.executeBatch();
                connection.commit();
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Converted " + i + " Chameleon Construct records");
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
