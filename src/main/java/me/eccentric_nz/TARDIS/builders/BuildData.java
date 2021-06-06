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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;

/**
 * Data class for building the TARDIS exterior.
 *
 * @author eccentric_nz
 */
public final class BuildData extends MaterialisationData {

    private boolean addSign = true;
    private boolean malfunction;
    private boolean minecartSounds = false;
    private boolean rebuild;

    public BuildData(String uuid) {
        setPlayerDefaults(uuid);
    }

    boolean shouldAddSign() {
        return addSign;
    }

    public boolean isMalfunction() {
        return malfunction;
    }

    public void setMalfunction(boolean malfunction) {
        this.malfunction = malfunction;
    }

    boolean useMinecartSounds() {
        return minecartSounds;
    }

    public boolean isRebuild() {
        return rebuild;
    }

    public void setRebuild(boolean rebuild) {
        this.rebuild = rebuild;
    }

    private void setPlayerDefaults(String uuid) {
        if (uuid == null) {
            // sane defaults
            addSign = true;
            minecartSounds = false;
        } else {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(TARDIS.plugin, uuid);
            if (rsp.resultSet()) {
                addSign = rsp.isSignOn();
                minecartSounds = rsp.isMinecartOn();
            }
        }
    }
}
