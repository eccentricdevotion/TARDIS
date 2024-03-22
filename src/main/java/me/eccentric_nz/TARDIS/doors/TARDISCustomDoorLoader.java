package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;

public class TARDISCustomDoorLoader {

    private final TARDIS plugin;
    /*
    ANGLER_POTTERY_SHERD,
    ARCHER_POTTERY_SHERD,
    ARMS_UP_POTTERY_SHERD,
    BLADE_POTTERY_SHERD,
    BREWER_POTTERY_SHERD,
    BURN_POTTERY_SHERD,
    DANGER_POTTERY_SHERD,
    EXPLORER_POTTERY_SHERD,
    FRIEND_POTTERY_SHERD,
    HEART_POTTERY_SHERD,
    HEARTBREAK_POTTERY_SHERD,
    HOWL_POTTERY_SHERD,
    MINER_POTTERY_SHERD,
    MOURNER_POTTERY_SHERD,
    PLENTY_POTTERY_SHERD,
    PRIZE_POTTERY_SHERD,
    SHEAF_POTTERY_SHERD,
    SHELTER_POTTERY_SHERD,
    SKULL_POTTERY_SHERD,
    SNORT_POTTERY_SHERD
     */

    public TARDISCustomDoorLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addDoors() {
        // create plugin doors
        Door police_box = new Door("Police Box", Material.IRON_DOOR, new int[]{1}, 1, true, "tardis_door_open", "tardis_door_close", false);
        Door classic = new Door("Classic", Material.CHERRY_DOOR, new int[]{0, 1, 2, 3, 4, 5, 6}, 4, false, "classic_door", "classic_door", false);
        Door.byMaterial.put(Material.IRON_DOOR, police_box);
        Door.byName.put("DOOR_POLICE_BOX", police_box);
        Door.byMaterial.put(Material.CHERRY_DOOR, classic);
        for (String r : plugin.getCustomDoorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomDoorsConfig().getString(r + ".material"));
                int[] frames = getFrames(plugin.getCustomDoorsConfig().getString(r + ".animation_sequence", "0,1"));
                Door door = new Door(
                        r,
                        material,
                        frames,
                        plugin.getCustomDoorsConfig().getInt(r + ".frame_rate"),
                        plugin.getCustomDoorsConfig().getBoolean(r + ".extra"),
                        plugin.getCustomDoorsConfig().getString(r + ".open_sound"),
                        plugin.getCustomDoorsConfig().getString(r + ".close_sound"),
                        true
                );
                Door.byMaterial.put(material, door);
                Door.byName.put("DOOR_" + r.toUpperCase(), door);
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
