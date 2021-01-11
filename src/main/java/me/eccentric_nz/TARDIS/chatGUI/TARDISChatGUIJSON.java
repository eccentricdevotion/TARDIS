/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chatGUI;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISChatGUIJSON {

    private final String EGG = "{\"text\":\"[TARDIS] \",\"color\":\"gold\",\"extra\":[{\"text\":\"Look at these eyebrows. These are attack eyebrows! They could take off bottle caps!\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis egg\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}]}";

    private final String TRANSMAT_LOCATION = "{\"text\":\"%s \",\"color\":\"green\",\"extra\":[{\"text\":\"X: %.2f, Y: %.2f, Z: %.2f, Yaw %.2f\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis transmat tp %s\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Transmat to this location\"}}}]}";

    private final List<String> sections = Arrays.asList(
            "{\"text\":\"1. TARDIS Controls\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section controls\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"2. TARDIS User Interfaces\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section interfaces\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"3. TARDIS Internal Spawn Locations\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section locations\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"4. Others\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section others\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}"
    );

    private final List<String> controls = Arrays.asList(
            "{\"text\":\"1. Artron Energy Capacitor button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update artron\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"2. Previous Location button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update back\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"3. Random Location button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update button\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"4. Handbrake\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update handbrake\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"5. World Type selector\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update world-repeater\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"6. Random x coordinate setter\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update x-repeater\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"7. Distance multipler\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update y-repeater\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"8. Random z coordinate setter\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update z-repeater\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"9. TARDIS Control Menu\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update control\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"10. Flight Mode button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update flight\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}"
    );

    private final List<String> interfaces = Arrays.asList(
            "{\"text\":\"1. TARDIS Advanced Console\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update advanced\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"2. Architectural Reconfiguration System\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update ars\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"3. Chameleon Circuit\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update chameleon\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"4. Direction Item Frame\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update direction\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"5. TARDIS Information System\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update info\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"6. Saved locations and TARDIS areas\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update save-sign\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"7. Disk Storage Container\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update storage\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"8. Temporal Relocator\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update temporal\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"9. Telepathic Circuit\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update telepathic\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"10. Siege Mode button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update siege\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"11. Destination Terminal\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update terminal\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"12. Sonic Generator\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update generator\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"13. TARDIS Weather Menu\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update weather\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}"
    );

    private final List<String> locations = Arrays.asList(
            "{\"text\":\"1. Artron Charged Creeper\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update creeper\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"2. Emergency Programme One\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update eps\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"3. Farm room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update farm\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"4. Rail room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update rail\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"5. Horse Stable room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update stable\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"6. Llama Stall room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update stall\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"7. Vault room drop chest\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update vault\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"8. Village room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update village\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"9. Igloo room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update igloo\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"10. Beacon toggle block\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update beacon\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}"
    );

    private final List<String> others = Arrays.asList(
            "{\"text\":\"1. Artron Energy Condenser\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update condenser\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"2. TARDIS Interior Door\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update door\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"3. TARDIS back doors\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update backdoor\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"4. Keyboard Input sign\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update keyboard\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"5. Exterior Scanner button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update scanner\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"6. TARDIS Scanner Map\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update map\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"7. Console Light switch\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update light\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"8. Toggle Black Wool behind door\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update toggle_wool\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"9. Chameleon Item Frame\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update frame\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"10. Zero room transmat button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update zero\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"11. Custard Cream dispenser\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update dispenser\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"12. TARDIS Force Field button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update forcefield\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"13. TARDIS Cloister bell button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update bell\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}",
            "{\"text\":\"14. Time Rotor item frame\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update rotor\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}}}"
    );

    public List<String> getSections() {
        return sections;
    }

    public List<String> getControls() {
        return controls;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public List<String> getLocations() {
        return locations;
    }

    public List<String> getOthers() {
        return others;
    }

    public String getEgg() {
        return EGG;
    }

    public String getTransmatLocation() {
        return TRANSMAT_LOCATION;
    }
}
