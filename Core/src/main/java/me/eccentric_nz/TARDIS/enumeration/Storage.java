/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum Storage {

    AREA("Area Storage", "areas", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBz
            cQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1z
            cQB+AA4AAAi2cQB+ACJzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiWgXBwcHBwcHBwcHBwcHBwcHBwcHBw
            cHBwcHBwcHBwcHA=
            """),

    BIOME_1("Biome Storage 1", "biomes_one", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBz
            cQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1z
            cQB+AA4AAAi2cQB+ADpzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACEJpb21lcyAxc3EAfgAOAJiWgXBwcHNxAH4AAHNxAH4AA3Vx
            AH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4ADgAACLZ0AARC
            T1dMc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVxAH4AFnEAfgAXdXEAfgAGAAAABHEA
            fgAZcQB+ABp0AAlOZXh0IFBhZ2VzcQB+AA4AAABWcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw
            """),

    BIOME_2("Biome Storage 2", "biomes_two", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHNxAH4A
            AHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4A
            DgAACLZ0AARCT1dMc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVxAH4AFnEAfgAXdXEA
            fgAGAAAABHEAfgAZcQB+ABp0AA1QcmV2aW91cyBQYWdlc3EAfgAOAAAAV3BwcHNxAH4AAHNxAH4A
            A3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4ADgAACLZx
            AH4AOnNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZxAH4AF3VxAH4ABgAAAARx
            AH4AGXEAfgAadAAIQmlvbWVzIDJzcQB+AA4AmJaBcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw
            cHBwcA==
            """),

    CIRCUIT("Circuit Storage", "circuits", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBz
            cQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1z
            cQB+AA4AAAi2cQB+AFJzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBw
            cHBwcHBwcHBwcHBwcHA=
            """),

    PLAYER("Player Storage", "players", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBz
            cQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1z
            cQB+AA4AAAi2cQB+AC5zcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBcHBwcHBwcHBwcHBwcHBwcHBw
            cHBwcHBwcHBwcHBwcA==
            """),

    PRESET_1("Preset Storage 1", "presets_one", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBz
            cQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1z
            cQB+AA4AAAi2cQB+AEZzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACVByZXNldHMgMXNxAH4ADgCYloFwcHBzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAE
            Qk9XTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZxAH4AF3VxAH4ABgAAAARx
            AH4AGXEAfgAadAAJTmV4dCBQYWdlc3EAfgAOAAAAVnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw
            cA==
            """),

    PRESET_2("Preset Storage 2", "presets_two", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHNxAH4A
            AHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4A
            DgAACLZ0AARCT1dMc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVxAH4AFnEAfgAXdXEA
            fgAGAAAABHEAfgAZcQB+ABp0AA1QcmV2aW91cyBQYWdlc3EAfgAOAAAAV3BwcHNxAH4AAHNxAH4A
            A3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4ADgAACLZx
            AH4ARnNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZxAH4AF3VxAH4ABgAAAARx
            AH4AGXEAfgAadAAJUHJlc2V0cyAyc3EAfgAOAJiWgXBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw
            cHBwcHA=
            """),

    SAVE_1("Save Storage 1", "saves_one", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHBwcHBz
            cQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1z
            cQB+AA4AAAi2cQB+ABFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1NhdmVzIDFzcQB+AA4AmJaBcHBwc3EAfgAAc3EAfgADdXEA
            fgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAItnQABEJP
            V0xzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+
            ABlxAH4AGnQACU5leHQgUGFnZXNxAH4ADgAAAFZwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHA=
            """),

    SAVE_2("Save Storage 2", "saves_two", """
            rO0ABXcEAAAANnNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw
            dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi
            bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl
            Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA
            AAR0AAI9PXQAAXZ0AAR0eXBldAAEbWV0YXVxAH4ABgAAAAR0AB5vcmcuYnVra2l0LmludmVudG9y
            eS5JdGVtU3RhY2tzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGph
            dmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAItnQAEE1VU0lDX0RJU0NfQ0hJUlBzcQB+AABz
            cQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0ABFjdXN0b20t
            bW9kZWwtZGF0YXVxAH4ABgAAAAR0AAhJdGVtTWV0YXQAClVOU1BFQ0lGSUN0AAVTYXZlc3NxAH4A
            DgCYloFzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAE
            cQB+AA1zcQB+AA4AAAi2dAARTVVTSUNfRElTQ19CTE9DS1NzcQB+AABzcQB+AAN1cQB+AAYAAAAE
            cQB+AAhxAH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQABUFyZWFzc3EAfgAOAJiW
            gXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4A
            DXNxAH4ADgAACLZ0AA9NVVNJQ19ESVNDX1dBSVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhx
            AH4AFXEAfgAWcQB+ABd1cQB+AAYAAAAEcQB+ABlxAH4AGnQAB1BsYXllcnNzcQB+AA4AmJaBc3EA
            fgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EA
            fgAOAAAItnQADk1VU0lDX0RJU0NfQ0FUc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVx
            AH4AFnEAfgAXdXEAfgAGAAAABHEAfgAZcQB+ABp0AAZCaW9tZXNzcQB+AA4AmJaBc3EAfgAAc3EA
            fgADdXEAfgAGAAAABHEAfgAIcQB+AAlxAH4ACnEAfgALdXEAfgAGAAAABHEAfgANc3EAfgAOAAAI
            tnQAD01VU0lDX0RJU0NfTUFMTHNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZx
            AH4AF3VxAH4ABgAAAARxAH4AGXEAfgAadAAHUHJlc2V0c3NxAH4ADgCYloFzcQB+AABzcQB+AAN1
            cQB+AAYAAAAEcQB+AAhxAH4ACXEAfgAKcQB+AAt1cQB+AAYAAAAEcQB+AA1zcQB+AA4AAAi2dAAO
            R0xPV1NUT05FX0RVU1RzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AFXEAfgAWcQB+ABd1
            cQB+AAYAAAAEcQB+ABlxAH4AGnQACENpcmN1aXRzc3EAfgAOAJieQXBwcHBwcHBwcHBwcHNxAH4A
            AHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4A
            DgAACLZ0AARCT1dMc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIcQB+ABVxAH4AFnEAfgAXdXEA
            fgAGAAAABHEAfgAZcQB+ABp0AA1QcmV2aW91cyBQYWdlc3EAfgAOAAAAV3BwcHNxAH4AAHNxAH4A
            A3VxAH4ABgAAAARxAH4ACHEAfgAJcQB+AApxAH4AC3VxAH4ABgAAAARxAH4ADXNxAH4ADgAACLZx
            AH4AEXNxAH4AAHNxAH4AA3VxAH4ABgAAAARxAH4ACHEAfgAVcQB+ABZxAH4AF3VxAH4ABgAAAARx
            AH4AGXEAfgAadAAHU2F2ZXMgMnNxAH4ADgCYloFwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw
            cHBw
            """);

    final String title;
    final String table;
    final String empty;

    Storage(String title, String table, String empty) {
        this.title = title;
        this.table = table;
        this.empty = empty;
    }

    public String getTitle() {
        return title;
    }

    public String getTable() {
        return table;
    }

    public String getEmpty() {
        return empty;
    }
}
