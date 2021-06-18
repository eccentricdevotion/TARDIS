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
package me.eccentric_nz.tardis.api;

import java.io.Serial;

/**
 * @author eccentric_nz
 */
public class TardisException extends Exception {

    @Serial
    private static final long serialVersionUID = -5152771166812835530L;

    public TardisException() {

    }

    public TardisException(String message) {
        super(message);
    }

    public TardisException(Throwable cause) {
        super(cause);
    }

    public TardisException(String message, Throwable cause) {
        super(message, cause);
    }
}
