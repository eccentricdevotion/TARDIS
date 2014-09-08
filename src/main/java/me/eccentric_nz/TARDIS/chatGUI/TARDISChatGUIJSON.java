/*
 * Copyright (C) 2014 eccentric_nz
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
 *
 * @author eccentric_nz
 */
public class TARDISChatGUIJSON {

    private final List<String> sections = Arrays.asList(
            "{\"text\":\"1. TARDIS Controls\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section controls\"}}",
            "{\"text\":\"2. TARDIS User Interfaces\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section interfaces\"}}",
            "{\"text\":\"3. TARDIS Internal Spawn Locations\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section locations\"}}",
            "{\"text\":\"4. Others\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis section others\"}}"
    );
    private final List<String> controls = Arrays.asList(
            "{\"text\":\"1. Artron Energy Capacitor button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update artron\"}}",
            "{\"text\":\"2. Previous Location button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update back\"}}",
            "{\"text\":\"3. Random Location button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update button\"}}",
            "{\"text\":\"4. Handbrake\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update handbrake\"}}",
            "{\"text\":\"5. World Type selector\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update world-repeater\"}}",
            "{\"text\":\"6. Random x coordinate setter\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update x-repeater\"}}",
            "{\"text\":\"7. Distance multipler\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update y-repeater\"}}",
            "{\"text\":\"8. Random z coordinate setter\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update z-repeater\"}}"
    );
    private final List<String> interfaces = Arrays.asList(
            "{\"text\":\"1. TARDIS Advanced Console\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update advanced\"}}",
            "{\"text\":\"2. Architectural Reconfiguration System\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update ars\"}}",
            "{\"text\":\"3. Chameleon Circuit\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update chameleon\"}}",
            "{\"text\":\"4. Direction Item Frame\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update direction\"}}",
            "{\"text\":\"5. TARDIS Information System\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update info\"}}",
            "{\"text\":\"6. Saved locations and TARDIS areas\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update save-sign\"}}",
            "{\"text\":\"7. Disk Storage Container\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update storage\"}}",
            "{\"text\":\"8. Temporal Relocator\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update temporal\"}}",
            "{\"text\":\"9. Destination Terminal\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update terminal\"}}"
    );
    private final List<String> locations = Arrays.asList(
            "{\"text\":\"1. Artron Charged Creeper\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update creeper\"}}",
            "{\"text\":\"2. Emergency Programme One\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update eps\"}}",
            "{\"text\":\"3. Farm room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update farm\"}}",
            "{\"text\":\"4. Rail room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update rail\"}}",
            "{\"text\":\"5. Stable room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update stable\"}}",
            "{\"text\":\"6. Village room\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update village\"}}"
    );
    private final List<String> others = Arrays.asList(
            "{\"text\":\"1. Artron Energy Condenser\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update condenser\"}}",
            "{\"text\":\"2. TARDIS Interior Door\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update door\"}}",
            "{\"text\":\"3. TARDIS back doors\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update backdoor\"}}",
            "{\"text\":\"4. Keyboard Input sign\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update keyboard\"}}",
            "{\"text\":\"5. Exterior Scanner button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update scanner\"}}",
            "{\"text\":\"6. Console Light switch\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update light\"}}",
            "{\"text\":\"7. Toggle Black Wool behind door\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update toggle_wool\"}}",
            "{\"text\":\"8. Zero room transmat button\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardis update zero\"}}"
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

}
