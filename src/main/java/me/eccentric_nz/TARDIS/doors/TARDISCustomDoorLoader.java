package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.Tag;

public class TARDISCustomDoorLoader {

    private final TARDIS plugin;

    public TARDISCustomDoorLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addDoors() {
        // create plugin doors
        Door police_box = new Door("Police Box", Material.IRON_DOOR, new int[]{0,1,2,3,4}, 6, true, "tardis_door_open", "tardis_door_close", false);
        Door classic = new Door("Classic", Material.CHERRY_DOOR, new int[]{0,1,2,3,4,5,6}, 4, false, "classic_door", "classic_door", false);
        Door.byMaterial.put(Material.IRON_DOOR, police_box);
        Door.byName.put("DOOR_POLICE_BOX", police_box);
        Door.byMaterial.put(Material.CHERRY_DOOR, classic);
        for (String r : plugin.getCustomDoorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomDoorsConfig().getString(r + ".material"));
                if (Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(material)) {
                    int[] frames = getFrames(plugin.getCustomDoorsConfig().getString(r + ".animation_sequence", "0,1"));
                    Door door = new Door(r, material, frames, plugin.getCustomDoorsConfig().getInt(r + ".frame_rate"), plugin.getCustomDoorsConfig().getBoolean(r + ".extra"), plugin.getCustomDoorsConfig().getString(r + ".open_sound"), plugin.getCustomDoorsConfig().getString(r + ".close_sound"), true);
                    Door.byMaterial.put(material, door);
                    Door.byName.put("DOOR_" + r.toUpperCase(), door);
                } else {
                    plugin.getMessenger().message(plugin.getConsole(), "Custom door [" + r + "] material must be a pottery sherd!");
                }
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom door item material for " + r + "!");
            }
        }
    }

    private int[] getFrames(String sequence) {
        String[] split = sequence.split(",");
        int[] frames = new int[split.length];
        for (int n = 0; n < split.length; n++) {
            frames[n] = TARDISNumberParsers.parseInt(split[n]);
        }
        return frames;
    }
}
