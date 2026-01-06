package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import org.bukkit.entity.Player;

import java.util.Locale;

public class DamageUtility {

    public static void run(TARDIS plugin, DiskCircuit circuit, int id, Player player) {
        if (!plugin.getConfig().getBoolean("circuits.damage")) {
            return;
        }
        if (plugin.getConfig().getInt("circuits.uses." + circuit.toString().toLowerCase(Locale.ROOT)) == 0) {
            return;
        }
        CircuitChecker tcc = new CircuitChecker(plugin, id);
        tcc.getCircuits();
        // decrement uses
        int uses_left = tcc.getUses(circuit);
        new CircuitDamager(plugin, circuit, uses_left, id, player).damage();
    }
}
