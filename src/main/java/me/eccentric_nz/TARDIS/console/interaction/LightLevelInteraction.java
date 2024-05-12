package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.InteractionResponse;
import me.eccentric_nz.TARDIS.console.models.LightLevelModel;
import me.eccentric_nz.TARDIS.control.actions.LightLevelAction;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class LightLevelInteraction {

    private final TARDIS plugin;

    public LightLevelInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setInterior(int state, int id, Interaction interaction, Player player) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        int unary = interaction.getPersistentDataContainer().getOrDefault(plugin.getUnaryKey(), PersistentDataType.INTEGER, 1);
        int setLevel = state + unary;
        if (setLevel > 7) {
            setLevel = 6;
            unary = -1;
        }
        if (setLevel < 0) {
            setLevel = 1;
            unary = 1;
        }
        interaction.getPersistentDataContainer().set(plugin.getUnaryKey(), PersistentDataType.INTEGER, unary);
        // set custom model data for light level switch item display
        UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
        if (uuid != null) {
            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
            new LightLevelModel().setState(display, setLevel, true);
        }
        // get light level record
        ResultSetLightLevel rs = new ResultSetLightLevel(plugin, interaction.getLocation().toString());
        if (rs.resultSet()) {
            new LightLevelAction(plugin).illuminate(setLevel - 1, rs.getControlId(), rs.isPowered(), 50, rs.isPoliceBox(), id, rs.isLightsOn());
            new InteractionStateSaver(plugin).write("INTERIOR_LIGHT_LEVEL_SWITCH", setLevel, id);
            plugin.getMessenger().announceRepeater(player, "Light level: " + InteractionResponse.levels.get(setLevel));
            // set control record
            HashMap<String, Object> set = new HashMap<>();
            set.put("secondary", setLevel);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 50);
            plugin.getQueryFactory().doSyncUpdate("controls", set, where);
        }
    }
}
