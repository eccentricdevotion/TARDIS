package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.InteractionResponse;
import me.eccentric_nz.TARDIS.control.actions.LightLevelAction;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class LampLevelInteraction {

    private final TARDIS plugin;

    public LampLevelInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setExterior(int state, int id, Interaction interaction, Player player) {
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
        // TODO set custom model data for lamp level switch item display
        // get light level record
        ResultSetLightLevel rs = new ResultSetLightLevel(plugin, interaction.getLocation().toString());
        if (rs.resultSet()) {
            new LightLevelAction(plugin).illuminate(setLevel - 1, rs.getControlId(), rs.isPowered(), 49, rs.isPoliceBox(), id, rs.isLightsOn());
            new InteractionStateSaver(plugin).write("EXTERIOR_LAMP_LEVEL_SWITCH", setLevel, id);
            plugin.getMessenger().announceRepeater(player, "Lamp level: " + InteractionResponse.levels.get(setLevel));
        }
    }
}
