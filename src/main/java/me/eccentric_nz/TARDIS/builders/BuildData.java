/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;

/**
 * Data class for building the TARDIS exterior.
 *
 * @author eccentric_nz
 */
public final class BuildData extends MaterialisationData {

    private boolean CTM = true;
    private boolean addSign = true;
    private boolean malfunction;
    private boolean minecartSounds = false;
    private boolean rebuild;
    private boolean texture = true;

    public BuildData(TARDIS plugin, String uuid) {
        super(plugin, uuid);
        // get player preferences
        setPlayerDefaults(uuid);
    }

    public boolean shouldUseCTM() {
        return CTM;
    }

    public void setCTM(boolean CTM) {
        this.CTM = CTM;
    }

    public boolean shouldAddSign() {
        return addSign;
    }

    public void setAddSign(boolean addSign) {
        this.addSign = addSign;
    }

    public boolean isMalfunction() {
        return malfunction;
    }

    public void setMalfunction(boolean malfunction) {
        this.malfunction = malfunction;
    }

    public boolean useMinecartSounds() {
        return minecartSounds;
    }

    public void setMinecartSounds(boolean minecartSounds) {
        this.minecartSounds = minecartSounds;
    }

    public boolean isRebuild() {
        return rebuild;
    }

    public void setRebuild(boolean rebuild) {
        this.rebuild = rebuild;
    }

    public boolean useTexture() {
        return texture;
    }

    public void setTexture(boolean texture) {
        this.texture = texture;
    }

    @Override
    public void setPlayerDefaults(String uuid) {
        if (uuid == null) {
            // sane defaults
            texture = TARDIS.plugin.getConfig().getBoolean("police_box.set_biome");
            addSign = true;
            CTM = true;
            minecartSounds = false;
        } else {
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("uuid", uuid);
            final ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(TARDIS.plugin, wherep);
            if (rsp.resultSet()) {
                super.setLamp(rsp.getLamp());
                texture = rsp.isPoliceboxTexturesOn();
                addSign = rsp.isSignOn();
                CTM = rsp.isCtmOn();
                minecartSounds = rsp.isMinecartOn();
            }
        }
    }
}
